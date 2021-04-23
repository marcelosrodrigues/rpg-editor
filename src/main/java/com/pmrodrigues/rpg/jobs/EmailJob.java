package com.pmrodrigues.rpg.jobs;

import static java.lang.String.format;

import java.io.StringWriter;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.pmrodrigues.rpg.entities.User;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailJob {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${com.pmrodrigues.rpg.email.from}")
	private String from;
	
	private VelocityEngine velocityEngine = new VelocityEngine();


	@Async
	public void sendTO(@NonNull User user, @NonNull String subject, @NonNull String template, Map model) {

		try {
			
			log.info("send a email to {}", user);
			var message = mailSender.createMimeMessage();

			var helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setTo(user.getEmail());
			helper.setSentDate(Date.from(Instant.now()));

			var body = new StringWriter();
			var context = new VelocityContext(model);
			
			if( log.isDebugEnabled() ) {
				log.debug("prepare email to {} using template {}", user.getEmail() , template);
			}
	
			if (velocityEngine.mergeTemplate(template, "UTF-8", context, body)) {
				helper.setText(body.toString(), true);
				mailSender.send(helper.getMimeMessage());
				log.info("email to {} was sent", user);
			} else {
				log.error("fail to sent a email to user {}. It was impossible to apply the template {} to email", user, template);
			}			
			
			
		} catch (MailException | MessagingException e) {
			log.error(format("fail to send a email to %s - %s", user, e.getMessage()), e);
		}

	}
}
