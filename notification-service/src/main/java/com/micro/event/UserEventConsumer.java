package com.micro.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecomx.events.UserEvent;
import com.micro.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

	private final EmailService emailService;

	@KafkaListener(topics = "user.events", groupId = "notification-service-group") 
	public void consume(UserEvent userEvent) {
		log.info("Received event: {}" + userEvent);

		System.out.println(userEvent.getEventType());
		if ("User_Registered".equals(userEvent.getEventType())) {
			handleUserCreated(userEvent);
		}

	}

	private void handleUserCreated(UserEvent event) {
		String email = event.getEmail();
		String username = event.getUsername();

		System.out.println("New user created: " + username + " (" + email + ")");
		emailService.sendWelcomeEmail(email, username);
	}

}
