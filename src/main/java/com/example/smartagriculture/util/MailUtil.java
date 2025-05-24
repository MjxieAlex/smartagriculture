package com.example.smartagriculture.util;

import com.example.smartagriculture.common.ApiResponse;
import com.example.smartagriculture.common.StatusCode;
import com.example.smartagriculture.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Component;


@Component
public class MailUtil {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String from, String subject, String content, String... receiver) {

        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom(from);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            throw new CustomException(StatusCode.SERVER_ERROR.getCode(), "邮件发送失败！");
        }
    }
}
