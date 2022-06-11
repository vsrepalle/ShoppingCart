package com.shoppingcart.controller;

import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.WishList;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.WishListRepository;
import com.shoppingcart.utils.AccountUtil;
import com.shoppingcart.utils.BasicAuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "wishlist")
public class WishListController {

	public WishListController(AccountRepository accountRepository, WishListRepository wishListRepository, AccountUtil accountUtil) {
		this.accountRepository = accountRepository;
		this.wishListRepository = wishListRepository;
		this.accountUtil = accountUtil;
	}
	@Autowired
	private final AccountRepository accountRepository;
	@Autowired
	private final WishListRepository wishListRepository;
	@Autowired
	private final AccountUtil accountUtil;
	
	private final Logger log = LoggerFactory.getLogger(WishListController.class);




	@PostMapping(value = "/account/add/{accountId}")
	public ResponseEntity<?> addWishList(@RequestBody @Valid WishList wishListInReq,
										 @RequestHeader("Authorization") String authorization) {
		Account account = accountUtil.getAccount(BasicAuthenticationUtil.getCredentials(authorization));
		Integer accountId = account.getId();
		log.debug("Adding wishlist items with accountId {}",+accountId);

		if (account != null) {
			log.debug("account is present");
			List<WishList> wishList = account.getWishList();
			wishList.add(wishListInReq);
			account.setWishList(wishList);
			accountRepository.save(account);
			return new ResponseEntity<>("WishList Added Successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Account Not Found With Id " + accountId, HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/account/update/{wishListId}")
	public ResponseEntity<?> updateWishList(@PathVariable("wishListId") Integer wishListId,
											@RequestBody @Valid WishList wishListInReq,
											@RequestHeader("Authorization") String authorization) {
		Account account = accountUtil.getAccount(BasicAuthenticationUtil.getCredentials(authorization));
		log.debug("updating wishlist with wishListId {}",wishListId);
		if(!account.getWishList().stream().anyMatch(wishList -> wishList.getWishId()==wishListId)){
			return new ResponseEntity<>("You cannot update others wishlist",HttpStatus.FORBIDDEN);
		}
		Optional<WishList> wishListOptional = wishListRepository.findById(wishListId);
		if (wishListOptional.isPresent()) {
			WishList wishList = wishListOptional.get();
			wishList.setCategory(wishListInReq.getCategory());
			wishList.setDisplayName(wishListInReq.getDisplayName());
			wishList.setShortDesc(wishListInReq.getShortDesc());
			wishListRepository.save(wishList);
			return new ResponseEntity<>("WishList Updated Successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("WishList Not Found With Id " + wishListId, HttpStatus.NOT_FOUND);

	}

	@DeleteMapping(value = "/account/delete/{wishListId}")
	public ResponseEntity<?> deleteWishList(@PathVariable("wishListId") Integer wishListId,
											@RequestHeader("Authorization") String authorization) {
		Account account = accountUtil.getAccount(BasicAuthenticationUtil.getCredentials(authorization));
		Integer accountId = account.getId();
		log.debug("delete the wishlistId with accountId {}",accountId);
		Optional<WishList> wishListFromDB = wishListRepository.findById(wishListId);
		if (wishListFromDB.isPresent()) {
			List<WishList> wishList = account.getWishList();
			wishList.remove(wishList.stream().filter(wish -> wish.getWishId() == wishListId)
					.collect(Collectors.toList()).get(0));
			account.setWishList(wishList);
			accountRepository.save(account);
			wishListRepository.deleteById(wishListId);
			return new ResponseEntity<>("WishList Deleted Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("WishList Not Found With Id " + wishListId, HttpStatus.NOT_FOUND);
	}
	@GetMapping("/account/getWishList")
	public ResponseEntity<?> getWishList(@RequestHeader("Authorization") String authorization){
		Account account = accountUtil.getAccount(BasicAuthenticationUtil.getCredentials(authorization));
		Integer accountId = account.getId();
		if(account != null){
           return new ResponseEntity<>(account.getWishList(),HttpStatus.OK);
		}
		return new ResponseEntity<>("Account Not Found With id "+accountId,HttpStatus.NOT_FOUND);
	}

}
