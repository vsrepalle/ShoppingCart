package com.shoppingcart.controller;

import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.WishList;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.WishListRepository;
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

	public WishListController(AccountRepository accountRepository, WishListRepository wishListRepository) {
		this.accountRepository = accountRepository;
		this.wishListRepository = wishListRepository;
	}
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private WishListRepository wishListRepository;



	@PostMapping(value = "/account/add/{accountId}")
	public ResponseEntity<?> addWishList(@PathVariable("accountId") Integer accountId,
			@RequestBody @Valid WishList wishListInReq) {

		Optional<Account> account = accountRepository.findById(accountId);

		if (account.isPresent()) {
			Account accountObjInDB = account.get();
			List<WishList> wishList = accountObjInDB.getWishList();
			wishList.add(wishListInReq);
			accountObjInDB.setWishList(wishList);
			accountRepository.save(accountObjInDB);
			return new ResponseEntity<>("WishList Added Successfully", HttpStatus.CREATED);

		}
		return new ResponseEntity<>("Account Not Found With Id " + accountId, HttpStatus.BAD_REQUEST);

	}

	@PutMapping(value = "/account/update/{wishListId}")
	public ResponseEntity<?> updateWishList(@PathVariable("wishListId") Integer wishListId,
			@RequestBody @Valid WishList wishListInReq) {
		if (wishListRepository.existsById(wishListId)) {
			WishList wishList = wishListRepository.findById(wishListId).get();
			wishList.setCategory(wishListInReq.getCategory());
			wishList.setDisplayName(wishListInReq.getDisplayName());
			wishList.setShortDesc(wishListInReq.getShortDesc());
			wishListRepository.save(wishList);
			return new ResponseEntity<>("WishList Updated Successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("WishList Not Found With Id " + wishListId, HttpStatus.NOT_FOUND);

	}

	@DeleteMapping(value = "/account/delete/{accountId}/{wishListId}")
	public ResponseEntity<?> deleteWishList(@PathVariable("wishListId") Integer wishListId,
			@PathVariable("accountId") Integer accountId) {
		Optional<WishList> wishListFromDB = wishListRepository.findById(wishListId);
		Optional<Account> accountFromDB = accountRepository.findById(accountId);
		if (wishListFromDB.isPresent() && accountFromDB.isPresent()) {
			Account account = accountFromDB.get();
			List<WishList> wishList = account.getWishList();
			wishList.remove(wishList.stream().filter(wish -> wish.getWishId() == wishListId)
					.collect(Collectors.toList()).get(0));
			account.setWishList(wishList);
			accountRepository.save(account);
			wishListRepository.deleteById(wishListId);
			return new ResponseEntity<>("WishList Deleted Successfully", HttpStatus.OK);
		} else if (!accountFromDB.isPresent()) {
			return new ResponseEntity<>("Account Not Found With Id " + accountId, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("WishList Not Found With Id " + wishListId, HttpStatus.NOT_FOUND);
	}

}