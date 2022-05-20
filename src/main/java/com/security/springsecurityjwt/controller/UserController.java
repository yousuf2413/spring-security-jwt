package com.security.springsecurityjwt.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.security.springsecurityjwt.model.AuthenticationRequest;
import com.security.springsecurityjwt.model.AuthenticationResponse;
import com.security.springsecurityjwt.service.MyUserDetailsService;
import com.security.springsecurityjwt.util.JwtTokenUtil;

@RestController
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	AuthenticationManager authManager;
	@Autowired
	MyUserDetailsService userDetailsService;
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@RequestMapping({ "/hello" })
	public String hello() {
		return "Hello World";
	}
	
	@RequestMapping({ "/user" })
	public String getUser() {
		return "Hello User";
	}
	
	@RequestMapping({ "/admin" })
	public String getAdmin() {
		return "Hello Admin";
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> generateAuthToken(@RequestBody AuthenticationRequest request) throws Exception {
		logger.info("UserController:: generateAuthToken() start");
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username and password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		String jwt = jwtTokenUtil.generateToken(userDetails);

		logger.info("UserController:: generateAuthToken() end");

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
