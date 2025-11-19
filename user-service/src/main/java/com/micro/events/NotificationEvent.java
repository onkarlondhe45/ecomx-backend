package com.micro.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationEvent {

	private String eventId;
	private String email;
	private String subject;
	private String message;
	private LocalDateTime timestamp;
}
