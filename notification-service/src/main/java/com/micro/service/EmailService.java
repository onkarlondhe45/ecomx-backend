package com.micro.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;

	public void sendWelcomeEmail(String toEmail, String name) {
		String subject = "Welcome to MyApp!";
		String body = "Hi " + name + ",\n\n" + "Welcome to EComX! Your account has been successfully created.\n\n"
				+ "Regards,\nEComX Team";

		sendEmail(toEmail, subject, body);
	}

	private void sendEmail(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			mailSender.send(message);

			System.out.println("Email sent to " + to);
		} catch (Exception e) {
			System.out.println("Failed to send email: " + e.getMessage());
		}
	}
}
