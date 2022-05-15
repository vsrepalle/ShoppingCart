package com.shoppingcart.controller;

import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.Order;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "users")
public class OrderController {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping(value = "/account/addOrder/{accountId}")
	public ResponseEntity<?> addOrder(@PathVariable("accountId") Integer accountId,
			@RequestBody @Valid Order orderInReq) {

		Optional<Account> account = accountRepository.findById(accountId);
		if (account.isPresent()) {
			Account accountObjInDB = account.get();
			List<Order> orderList = accountObjInDB.getOrdersList();
			orderInReq.setOrderId(java.util.UUID.randomUUID().toString());
			orderList.add(orderInReq);
			accountObjInDB.setOrdersList(orderList);
			accountRepository.save(accountObjInDB);
			return new ResponseEntity<>("Order Added Successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Account Not Found With Id " + accountId, HttpStatus.NOT_FOUND);

	}

	@GetMapping(value = "/account/filterOrdersByStatus/{accountId}/{status}")
	public ResponseEntity<?> filterOrdersByStatus(@PathVariable("accountId") Integer accountId,
			@PathVariable("status") String status) {

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
			} else {
				return new ResponseEntity<>(filteredOrders, HttpStatus.FOUND);
			}
		}
		return new ResponseEntity<>("No account found with accountId--->" + accountId, HttpStatus.NOT_FOUND);
	}
	@GetMapping(value="/account/listOrders/{accountId}")
	public ResponseEntity<?> listOfOrders(@PathVariable("accountId") Integer accountId){
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

	@DeleteMapping(value = "/account/cancelOrder/{orderId}/{accountId}")
	public ResponseEntity<?> cancelOrder(@PathVariable("orderId") String orderId,@PathVariable("accountId") Integer accountId) {

		Optional<Order> orderInDB = orderRepository.findById(orderId);
		if (orderInDB.isPresent()) {
			Optional<Account> account = accountRepository.findById(accountId);
            Account accountFromDB = account.get();
			List<Order> orders = accountFromDB.getOrdersList();
			orders=orders.stream().filter(order -> !Objects.equals(order.getOrderId(), orderId)).collect(Collectors.toList());
			accountFromDB.setOrdersList(orders);
			accountRepository.save(accountFromDB);
			orderRepository.deleteById(orderId);
			return new ResponseEntity<>("The order is Cancelled !!", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Order Not Found With Id " + orderId, HttpStatus.NOT_FOUND);

	}
}
