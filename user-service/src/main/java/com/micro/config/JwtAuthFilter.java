package com.micro.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	private final CustomUserDetails customUserDetails;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);
		String username = jwtService.getUsername(token);
		String role = jwtService.getRole(token);

		System.out.println("Decoded JWT User: " + username);
		System.out.println("Decoded JWT Role: " + role);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = customUserDetails.loadUserByUsername(username);

			// **Convert role from token to Spring Security Authority**
			SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role);

			// Print to verify:
			System.out.println("Assigned Authority: " + grantedAuthority.getAuthority());

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, List.of(grantedAuthority));

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}

		filterChain.doFilter(request, response);
	}

}
