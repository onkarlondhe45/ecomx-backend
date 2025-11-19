package com.micro.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponseDto {

	private String village;
	private String pincode;
	private String city;
	private String state;
	private String country;
}
