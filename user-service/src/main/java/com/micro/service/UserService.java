package com.micro.service;

import java.util.List;

import com.micro.requestdto.UserRequestDto;
import com.micro.responsedto.UserResponseDto;

public interface UserService {

	UserResponseDto createUser(UserRequestDto userRequestDto);

	UserResponseDto getById(long id);

	List<UserResponseDto> getAll();

	void deleteUser(long id);

	UserResponseDto updateUser(long id, UserRequestDto userRequestDto);
}
