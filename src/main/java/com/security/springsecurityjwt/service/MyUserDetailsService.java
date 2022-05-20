package com.security.springsecurityjwt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.springsecurityjwt.model.MyUserDetails;
import com.security.springsecurityjwt.model.User;
import com.security.springsecurityjwt.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(username);
		user.orElseThrow(()->new UsernameNotFoundException("Not Found:"+username));
		return user.map(MyUserDetails :: new).get();
		//return new User("user","pass",new ArrayList<>());
	}

}
