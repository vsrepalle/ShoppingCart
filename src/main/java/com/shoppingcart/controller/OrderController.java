package com.shoppingcart.controller;

import com.shoppingcart.dto.OrderDTO;
import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.Cart;
import com.shoppingcart.entity.Order;
import com.shoppingcart.entity.Product;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.CartRepository;
import com.shoppingcart.repository.OrderRepository;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.service.ShoppingCartService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "orders")
public class OrderController {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ShoppingCartService shoppingCartService;

	private final Logger log = Logger.getLogger(OrderController.class.getName());

	@PostMapping(value = "/account/addOrder/{accountId}")
	public ResponseEntity<?> addOrder(@PathVariable("accountId") Integer accountId,
			@RequestBody @Valid Order orderInReq) {
		log.debug("adding orders with accountId "+accountId);

		Optional<Account> account = accountRepository.findById(accountId);
		if (account.isPresent()) {
			Account accountObjInDB = account.get();
			List<Order> orderList = accountObjInDB.getOrdersList();
			orderInReq.setOrderId(java.util.UUID.randomUUID().toString());
            orderInReq.setOrderStatus("PENDING");
			orderInReq.setUserId(accountId);
			if(accountObjInDB.getCart() == null){
				return new ResponseEntity<>("There are no products in cart",HttpStatus.EXPECTATION_FAILED);
			}
			orderInReq.setTotalPrice(calculateCartPrice(accountObjInDB.getCart().getCartId()));
			orderInReq.setProductQuantityMap(accountObjInDB.getCart().getProductQuantityMap());
			orderList.add(orderInReq);
			accountObjInDB.setOrdersList(orderList);
			accountRepository.save(accountObjInDB);
			return new ResponseEntity<>("Order Added Successfully and all products are removed from Cart", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Account Not Found With Id " + accountId, HttpStatus.NOT_FOUND);

	}
	@PostMapping(value = "/account/confirmOrder/{orderId}")
	public ResponseEntity<?> confirmOrder(@PathVariable("orderId") String orderId){
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if(orderOptional.isPresent()){
			Order order = orderOptional.get();
			for(Map.Entry<Integer,Integer> entry:order.getProductQuantityMap().entrySet()){
				Product product = productRepository.findById(entry.getKey()).get();
				product.setStockQty(product.getStockQty()-entry.getValue());
				productRepository.save(product);
			}
			return new ResponseEntity<>("Confirmed Order with id "+orderId,HttpStatus.OK);
		}
		return new ResponseEntity<>("Order not found with id "+orderId,HttpStatus.NOT_FOUND);
	}
	private float calculateCartPrice(int cartId){
       Optional<Cart> cart = cartRepository.findById(cartId);
	   Map<Integer,Integer> productQuantityMap = cart.get().getProductQuantityMap();
	   float result = 0F;
	   for(Map.Entry<Integer,Integer> entry:productQuantityMap.entrySet()){
		   Product product = productRepository.findById(entry.getKey()).get();
		   float priceOfProduct = product.getPrice()*entry.getValue();
		   result+=priceOfProduct;
	   }
	   return result;
	}

	@PostMapping(value = "/account/getOrdersInFutureDate")
	public ResponseEntity<?> getOrdersInFutureDate() {
		log.debug("getOrdersInFutureDate");
		List<Order> orders = orderRepository.findAll();
		orders=orders.stream().filter(order -> {
			Date orderDate = order.getOrderedDate();
			if(orderDate != null){
				Date now = new Date();
				long difference_In_Time
						= orderDate.getTime() - now.getTime();
				long difference_In_Days
						= (difference_In_Time
						/ (1000 * 60 * 60 * 24))
						% 365;
				return order.getOrderStatus().equals("PENDING") && difference_In_Days <= 2;
			}
			return false;
		}).collect(Collectors.toList());
		List<OrderDTO> orderDTOList = orders.stream()
				.map(order -> {
					OrderDTO orderDTO = new OrderDTO();
					orderDTO.setOrder(order);
					orderDTO.setAccount(accountRepository.findById(order.getUserId()).get());
					return orderDTO;
				}).collect(Collectors.toList());
		return new ResponseEntity<>(orderDTOList, HttpStatus.OK);

	}


	@GetMapping(value = "/account/filterOrdersByStatus/{accountId}/{status}")
	public ResponseEntity<?> filterOrdersByStatus(@PathVariable("accountId") Integer accountId,
			@PathVariable("status") String status) {
		log.debug("checking the status of order with "+status+" and with accountId "+accountId);

		Optional<Account> account = accountRepository.findById(accountId);

		if (account.isPresent()) {
			Account accountObjInDB = account.get();
			List<Order> orderList = accountObjInDB.getOrdersList();
			List<Order> filteredOrders = new ArrayList<>();

			if (null != orderList) {
				orderList.stream().filter(o -> status.equals(o.getOrderStatus())).forEach(filteredOrders::add);
			}
			if (filteredOrders.isEmpty()) {
				return new ResponseEntity<>("No Order found with Status--->" + status, HttpStatus.NOT_FOUND);
			}
				return new ResponseEntity<>(filteredOrders, HttpStatus.FOUND);
		}
		return new ResponseEntity<>("No account found with Id-->" + accountId, HttpStatus.NOT_FOUND);
	}
	@GetMapping(value="/account/listOrders/{accountId}")
	public ResponseEntity<?> listOfOrders(@PathVariable("accountId") Integer accountId){
		log.debug("showing the list of orders with accountId "+accountId);
		Optional<Account> account = accountRepository.findById(accountId);
		if(account.isPresent()) {
			if(account.get().getOrdersList().isEmpty()) {
				return new ResponseEntity<>("Orders are empty",HttpStatus.OK);
			}
			return new ResponseEntity<>(account.get().getOrdersList(),HttpStatus.FOUND);
		}
		return new ResponseEntity<>("Account Not Found With Id "+accountId,HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/account/returnOrder/{orderId}")
	public ResponseEntity<?> returnOrder(@PathVariable("orderId") String orderId) {
		log.debug("order was taking return with orderId "+orderId);

		Optional<Order> orderInDB = orderRepository.findById(orderId);
		if (orderInDB.isPresent()) {
			Order order = orderInDB.get();
			Date orderDate = order.getOrderedDate();
			Date currentDate = new Date();
			long difference_In_Time = orderDate.getTime() - currentDate.getTime();

			long differenceInDays = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

			if (differenceInDays > 10) {
				return new ResponseEntity<>("The order can't be returned as it is ordered before ten days--->",
						HttpStatus.NOT_ACCEPTABLE);
			}

			else {
				return new ResponseEntity<>(
						"Your Return Order is taken!! Your money will be Credited to your Account !!",
						HttpStatus.ACCEPTED);
			}
		}
		return new ResponseEntity<>("Order Not Found With Id " + orderId, HttpStatus.NOT_FOUND);

	}

	@DeleteMapping(value = "/account/cancelOrder/{orderId}")
	public ResponseEntity<?> cancelOrder(@PathVariable("orderId") String orderId) {
		log.debug("order cancelling with orderId "+orderId);

		Optional<Order> orderInDB = orderRepository.findById(orderId);
		if (orderInDB.isPresent()) {
			Order order = orderInDB.get();
			order.setOrderStatus("CANCELLED");
			orderRepository.save(order);
			return new ResponseEntity<>("The order is Cancelled !!", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Order Not Found With Id " + orderId, HttpStatus.NOT_FOUND);

	}
}
