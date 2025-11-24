package com.micro.requestdto;

import java.time.LocalDate;
import java.util.List;

import com.micro.enums.Roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRequestDto {

	private long id;
	private String username;
	private String email;
	private String password;
	private List<AddressRequestDto> address;
	private Roles role;
	private LocalDate createdAt;
	private LocalDate updatedAt;
}
