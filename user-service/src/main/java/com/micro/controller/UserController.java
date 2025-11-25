package com.micro.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.config.JwtService;
import com.micro.enums.Roles;
import com.micro.requestdto.LoginRequest;
import com.micro.requestdto.UserRequestDto;
import com.micro.responsedto.LoginResponse;
import com.micro.responsedto.UserResponseDto;
import com.micro.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		UserDetails user = (UserDetails) authenticate.getPrincipal();

		String authority = user.getAuthorities().stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Role not found")).getAuthority();

		Roles role = Roles.valueOf(authority.replace("ROLE_", ""));
		String token = jwtService.generateToken(user.getUsername(), role.name());
		String refreshToken = jwtService.generateRefreshToken(user.getUsername());

		LoginResponse response = LoginResponse.builder().username(user.getUsername()).roles(role).token(token)
				.refreshToken(refreshToken).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
		String refreshToken = request.get("refreshToken");
		try {
			String newAccessToken = jwtService.refreshAccessToken(refreshToken);
			return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
		}
	}

	@PostMapping("/create")
	public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
		UserResponseDto createUser = userService.createUser(userRequestDto);
		if (createUser != null) {
			return new ResponseEntity<>(createUser, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/userId/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") long id) {
		UserResponseDto user = userService.getById(id);
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<UserResponseDto>> getAllUser() {
		List<UserResponseDto> userList = userService.getAll();
		if (userList != null) {
			return new ResponseEntity<>(userList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/userId/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserResponseDto> updateUserById(@PathVariable("id") long id,
			@RequestBody UserRequestDto userRequestDto) {
		UserResponseDto updateUser = userService.updateUser(id, userRequestDto);

		if (updateUser != null) {
			return new ResponseEntity<>(updateUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/userId/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>("user with user id " + id + " deleted successfully...!", HttpStatus.NO_CONTENT);
	}
}
