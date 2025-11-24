package com.micro.responsedto;

import java.time.LocalDate;
import java.util.List;

import com.micro.enums.Roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

	private long id;
	private String username;
	private String email;
	private List<AddressResponseDto> address;
	private Roles role;
	private LocalDate createdAt;
	private LocalDate updatedAt;
}
