package de.hso.badenair.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMail(List<String> recipients, String messageContent) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
            setProperties(mimeMessageHelper, recipients, messageContent);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("An error occured while sending a mail: {}", e.getMessage());
        }
    }

    public void sendMailWithAttachment(List<String> recipients, String messageContent, File file) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
            setProperties(mimeMessageHelper, recipients, messageContent);
            mimeMessageHelper.addAttachment(file.getName(), file);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("An error occured while sending a mail: {}", e.getMessage());
        }
    }

    private void setProperties(MimeMessageHelper mimeMessageHelper, List<String> recipients, String messageContent) throws MessagingException {
        mimeMessageHelper.setTo(recipients.toArray(new String[0]));
        mimeMessageHelper.setFrom(""); // TODO: Get from address
        mimeMessageHelper.setText(messageContent, true);
    }
}
