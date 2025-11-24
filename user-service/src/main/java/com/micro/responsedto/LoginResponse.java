package com.micro.responsedto;

import com.micro.enums.Roles;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
	private String username;
	private Roles roles;
	private String token; // Access token
}
