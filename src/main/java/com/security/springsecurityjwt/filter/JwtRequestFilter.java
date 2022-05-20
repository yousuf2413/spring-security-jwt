package com.security.springsecurityjwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.springsecurityjwt.service.MyUserDetailsService;
import com.security.springsecurityjwt.util.JwtTokenUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = Logger.getLogger(JwtRequestFilter.class);

	@Autowired
	MyUserDetailsService userDetailsService;
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("JwtRequestFilter:: doFilterInternal() start");

		final String athorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		if (athorizationHeader != null && athorizationHeader.startsWith("Bearer ")) {
			jwt = athorizationHeader.substring(7);
			username = jwtTokenUtil.extractUsername(jwt);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
		logger.info("JwtRequestFilter:: doFilterInternal() end");
	}
}
