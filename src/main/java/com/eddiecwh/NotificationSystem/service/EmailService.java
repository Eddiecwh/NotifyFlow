package com.eddiecwh.NotificationSystem.service;

import com.eddiecwh.NotificationSystem.config.RabbitMQConfig;
import com.eddiecwh.NotificationSystem.entity.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;
    private final JobService jobService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void consumeEmailMessage(Job job) {
        String email = job.getRequest().getUser().getEmail();
        String subject = job.getRequest().getSubject();
        String body = job.getRequest().getBody();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            jobService.markJobAsSent(job.getId());
            log.info("Email has been sent for jobId: {}", job.getId());
        } catch (MailParseException | MailAuthenticationException e) {
            jobService.markJobAsFailed(job.getId(), e.getMessage());
            log.error("EmailService::consumeEmailMessage - Permanent job failure for jobId: {}, error: {}", job.getId(), e.getMessage());
        } catch (MailException e) {
            jobService.markJobAsRetry(job.getId(), e.getMessage());
            log.error("EmailService::consumeEmailMessage - Transient job failure for jobId: {}, error: {}", job.getId(), e.getMessage());
        }
        catch (Exception e) {
            jobService.markJobAsRetry(job.getId(), e.getMessage());
            log.error("EmailService::consumeEmailMessage - Unexpected failure for jobId: {}, error: {}", job.getId(), e.getMessage());
        }
    }
}
