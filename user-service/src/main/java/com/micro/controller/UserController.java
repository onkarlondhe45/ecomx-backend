package com.micro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.requestdto.UserRequestDto;
import com.micro.responsedto.UserResponseDto;
import com.micro.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/")
	public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
		UserResponseDto createUser = userService.createUser(userRequestDto);
		if (createUser != null) {
			return new ResponseEntity<>(createUser, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/userId/{id}")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") long id) {
		UserResponseDto user = userService.getById(id);
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<UserResponseDto>> getAllUser() {
		List<UserResponseDto> userList = userService.getAll();
		if (userList != null) {
			return new ResponseEntity<>(userList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/userId/{id}")
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
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>("user with user id " + id + " deleted successfully...!", HttpStatus.NO_CONTENT);
	}
}
