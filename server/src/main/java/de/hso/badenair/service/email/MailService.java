package de.hso.badenair.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMail(String recipient, String subject, String messageContent) {
        try {
            final MimeMessage message = createMessage(recipient, subject, messageContent);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("An error occured while sending a mail: {}", e.getMessage());
        }
    }

    private MimeMessage createMessage(String recipient, String subject, String messageContent) throws MessagingException {
        final MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setTo(recipient);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom("info@badenair.de");
        mimeMessageHelper.setText(messageContent, true);
        return message;
    }
}
