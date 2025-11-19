package com.micro.serviceimp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.events.NotificationEvent;
import com.micro.globalexception.UserNotFoundException;
import com.micro.model.Address;
import com.micro.model.User;
import com.micro.repository.UserRepository;
import com.micro.requestdto.UserRequestDto;
import com.micro.responsedto.AddressResponseDto;
import com.micro.responsedto.UserResponseDto;
import com.micro.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

	private final UserRepository repository;

	private final KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper;

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {

		User user = User.builder().username(userRequestDto.getUsername()).email(userRequestDto.getEmail())
				.password(userRequestDto.getPassword()).role(userRequestDto.getRole()).build();

		List<Address> addresses = userRequestDto.getAddress().stream()
				.map(address -> Address.builder().village(address.getVillage()).pincode(address.getPincode())
						.city(address.getCity()).state(address.getState()).country(address.getCountry()).user(user)
						.build())
				.toList();

		user.setAddress(addresses);
		User saveUser = repository.save(user);

		String eventId = UUID.randomUUID().toString();

		NotificationEvent event = NotificationEvent.builder().eventId(eventId).email(user.getEmail())
				.subject("Welcome " + user.getUsername()).message("Your registration is successful!")
				.timestamp(LocalDateTime.now()).build();

		try {
			kafkaTemplate.send("notification-topic", eventId, objectMapper.writeValueAsString(event));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return entityToDto(saveUser);
	}

	public List<AddressResponseDto> addressToDto(List<Address> list) {
		return list.stream().map(a -> AddressResponseDto.builder().village(a.getVillage()).pincode(a.getPincode())
				.city(a.getCity()).state(a.getState()).country(a.getCountry()).build()).toList();
	}

	public UserResponseDto entityToDto(User user) {
		return UserResponseDto.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail())
				.address(addressToDto(user.getAddress())).updatedAt(user.getUpdatedAt()).createdAt(user.getCreatedAt())
				.role(user.getRole()).build();
	}

	@Override
	public UserResponseDto getById(long id) {
		User user = repository.findById((int) id)
				.orElseThrow(() -> new UserNotFoundException("user not found with userid : " + id));
		return entityToDto(user);
	}

	@Override
	public List<UserResponseDto> getAll() {
		List<User> userList = repository.findAll();
		return userList.stream().map(this::entityToDto).toList();
	}

	@Override
	public void deleteUser(long id) {

		User user = repository.findById((int) id)
				.orElseThrow(() -> new UserNotFoundException("user not found with userid : " + id));
		repository.delete(user);
	}

	@Override
	public UserResponseDto updateUser(long id, UserRequestDto userRequestDto) {
		User user = repository.findById((int) id)
				.orElseThrow(() -> new UserNotFoundException("user not found with userid : " + id));
		user.setUsername(userRequestDto.getUsername());
		user.setEmail(userRequestDto.getEmail());
		user.setPassword(userRequestDto.getPassword());
		user.setRole(new ArrayList<>(userRequestDto.getRole()));

		user.getAddress().clear();

		List<Address> updatedAddress = userRequestDto
				.getAddress().stream().map(a -> Address.builder().village(a.getVillage()).pincode(a.getPincode())
						.city(a.getCity()).state(a.getState()).country(a.getCountry()).user(user).build())
				.collect(Collectors.toList());

		user.getAddress().addAll(updatedAddress);
		User updateUser = repository.save(user);
		return entityToDto(updateUser);
	}
}
