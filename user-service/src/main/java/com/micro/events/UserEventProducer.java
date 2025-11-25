package com.micro.events;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecomx.events.UserEvent;
import com.micro.model.User;
import com.micro.utils.EventType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

	private final KafkaTemplate<String, UserEvent> kafkaTemplate;

	public void userRegisteredEvent(User user) {

		UserEvent event = UserEvent.builder().eventId(UUID.randomUUID()).username(user.getUsername()).email(user.getEmail())
				.subject("Welcome to EComX platform!").message("Thanks for registering, " + user.getUsername())
				.eventType(String.valueOf(EventType.User_Registered)).timestamp(LocalDateTime.now()).build();

		kafkaTemplate.send("user.events", event);
		log.info("Published User_Registered event: {}", event);
	}
}
