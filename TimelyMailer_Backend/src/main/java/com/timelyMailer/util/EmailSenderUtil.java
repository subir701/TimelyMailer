package com.timelyMailer.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Slf4j
public class EmailSenderUtil {

    private JavaMailSender mailSender;

    public EmailSenderUtil(JavaMailSender mailSender){
        this.mailSender= mailSender;
    }

    public void sendEmail(List<String> to, String subject, String content, String filePath)throws MessagingException {
        log.info("Preparing to send email to: {}", to);
        log.debug("Subject: {}", subject);
        log.debug("Initial Content: {}", content);
        if (filePath != null && !filePath.isBlank()) {
            log.info("File link will be added to the content: {}", filePath);
            content += "<br><br><a href=\"" + filePath + "\">Click here to view the attached file</a>";
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(content, true);

            // You can enable this if you add local file support again
            // log.info("Trying to attach file from path: {}", filePath);

            //For Sharing file to local storage
//        if(filePath != null && !filePath.isBlank()) {
//            FileSystemResource file = new FileSystemResource(new File(filePath));
//            if(file.exists()){
//                helper.addAttachment(file.getFilename(), file);
//            }
//        }

            mailSender.send(message);
            log.info("Email successfully sent to: {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
            throw e;
        }


    }
}

//For Sharing file to local storage
////        if(filePath != null && !filePath.isBlank()) {
////            FileSystemResource file = new FileSystemResource(new File(filePath));
////            if(file.exists()){
////                helper.addAttachment(file.getFilename(), file);
////            }
//        }