package com.example.demo.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

import com.example.demo.model.persistence.UserProfile;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import javax.servlet.http.HttpServletRequest;

import static com.example.demo.utils.Utils.createSalt;
import static com.example.demo.utils.Utils.get_SecurePassword;

//@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger= LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {

		logger.info("Users: finding a user by id: {}", id);
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		logger.info("Users: processing  user finding by name: {}", username);
		User user = userRepository.findByUsername(username);
		ResponseEntity<User> retUser = user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
		if(user!=null) logger.info("Users: Success returning found user: {}", username);
		else logger.info("Users: Failed returning a user: {}", username);
		return retUser;
	}
	@GetMapping("/userprofile")
	public ResponseEntity<UserProfile> getUserProfile(Authentication currentLoggedIn) {
			User user = userRepository.findByUsername(currentLoggedIn.getName());
		    logger.info("Users: processing  profile user finding for currenly loggedIn: {}", currentLoggedIn.getName());
			ResponseEntity<UserProfile> retUserProfile = !(currentLoggedIn instanceof AnonymousAuthenticationToken) ?  ResponseEntity.ok(user.getProfile()) :ResponseEntity.notFound().build();
			if(retUserProfile!=null) logger.info("Users: Success returning founded user profile : {}", user.getUsername());
			else logger.info("Users: Failed returning user profile");
			return retUserProfile;
	}
	@GetMapping("/cartDetails")
	public ResponseEntity<Cart> getCartDetails(Authentication currentLoggedIn) {
		User user = userRepository.findByUsername(currentLoggedIn.getName());
		logger.info("Users: processing  profile user finding for currenly loggedIn user: {}", currentLoggedIn.getName());
		ResponseEntity<Cart> retCart = !(currentLoggedIn instanceof AnonymousAuthenticationToken) ?  ResponseEntity.ok(user.getCart()) :ResponseEntity.notFound().build();
		if(retCart!=null) logger.info("Users: Success returning cart details : {}", currentLoggedIn.getName());
		else logger.info("Users: Failed returning cart details currenly loggedIn user: {}",currentLoggedIn.getName());
		return retCart;

	}
	// Method to generate a Salt

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		logger.info("Users: processing  creating user: {}", createUserRequest.getUsername());
         // generate salt
		//byte[] salt = createSalt();
		// salt the saved pasword
		//String securePassword = get_SecurePassword(createUserRequest.getPassword(), salt);

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
		    logger.info("Users: Failed creating a  user :{}",createUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
		}
		// Create a hash for a password

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
	    //user.setSalt(salt);
		userRepository.save(user);

		logger.info("users: Success creating user {}: "+ createUserRequest.getUsername());

		return ResponseEntity.ok(user);
	}

	
}
