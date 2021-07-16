package br.com.cotiinformatica.senders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

	// injeção de dependência da configuração de email
	// feita na classe 'MailConfiguration'
	@Autowired
	private JavaMailSender javaMailSender;

	// método para realizar o envio do email..
	public void sendMessage(String to, String subject, String text) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom("coti-informatica@gmail.com");
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);

		javaMailSender.send(mailMessage);
	}
}
