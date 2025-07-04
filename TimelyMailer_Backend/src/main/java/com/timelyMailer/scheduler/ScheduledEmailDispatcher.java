package com.timelyMailer.scheduler;

import com.timelyMailer.entity.ScheduledEmail;
import com.timelyMailer.entity.Status;
import com.timelyMailer.service.ScheduledEmailService;
import com.timelyMailer.util.EmailSenderUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduledEmailDispatcher {

    private ScheduledEmailService emailService;

    private EmailSenderUtil emailSenderUtil;

    public ScheduledEmailDispatcher(ScheduledEmailService emailService, EmailSenderUtil emailSenderUtil){

        this.emailService = emailService;
        this.emailSenderUtil = emailSenderUtil;
    }

    @Transactional
    @Scheduled(fixedRateString = "${scheduler.fixedRateMs:600000}")
    public void dispatcherDueEmails(){
        log.info("⏰ Scheduled Task Triggered: Checking for due emails to send...");

        List<ScheduledEmail> dueEmails = emailService.getDueEmailsForSending();
        log.info("📬 Total emails fetched for dispatch: {}", dueEmails.size());

        for (ScheduledEmail email : dueEmails) {
            log.info("🔄 Processing email ID: {}, Subject: {}, ScheduledTime: {}",
                    email.getEmailId(), email.getSubject(), email.getScheduledTime());

            boolean shouldUpdate = false;

            try {
                emailSenderUtil.sendEmail(
                        email.getReceiverMailIds(),
                        email.getSubject(),
                        email.getContent(),
                        email.getFilePath()
                );
                email.setStatus(Status.SENT);
                shouldUpdate = true;
                log.info("✅ Email successfully sent to: {}", email.getReceiverMailIds());

            } catch (MessagingException ex) {
                int retries = email.getRetryCount() + 1;
                email.setRetryCount(retries);

                if (retries >= 5) {
                    email.setStatus(Status.FAILED);
                    log.error("❌ Failed to send email after {} attempts. Marking as FAILED. ID: {}", retries, email.getEmailId());
                } else {
                    log.warn("⚠️ Send failed (attempt {}/5) for email ID: {}. Retrying in next cycle.",
                            retries, email.getEmailId());
                }

                shouldUpdate = true;
            }

            if (shouldUpdate) {
                emailService.saveOrUpdateEmail(email);
                log.info("📥 Email status updated in DB. ID: {}, Status: {}, Retries: {}",
                        email.getEmailId(), email.getStatus(), email.getRetryCount());
            }
        }

        log.info("✅ Scheduler run complete.");
    }
}
