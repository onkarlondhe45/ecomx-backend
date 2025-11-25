package com.ecomx.events;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEvent {

	private UUID eventId;
	private String username;
	private String email;
	private String subject;
	private String message;
	private String eventType;
	private LocalDateTime timestamp;
}
