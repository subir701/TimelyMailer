package com.timelyMailer.controller;

import com.timelyMailer.DTO.ScheduledEmailRequest;
import com.timelyMailer.entity.ScheduledEmail;
import com.timelyMailer.exception.ApiException;
import com.timelyMailer.exception.EmailNotFound;
import com.timelyMailer.exception.ResourceNotFoundException;
import com.timelyMailer.service.ScheduledEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emails")
@Slf4j
public class ScheduledEmailController {

    @Autowired
    private ScheduledEmailService emailService;

    // 1️⃣ Schedule a new email
    @PostMapping
    public ResponseEntity<ScheduledEmail> scheduleEmail(@RequestBody ScheduledEmailRequest request) throws ApiException {
        log.info("📩 Received request to schedule email: {}", request);
        ScheduledEmail scheduledEmail = emailService.scheduleEmail(request);
        log.info("✅ Email scheduled successfully with ID: {}", scheduledEmail.getEmailId());
        return ResponseEntity.ok(scheduledEmail);
    }

    // 2️⃣ Get all emails
    @GetMapping
    public ResponseEntity<List<ScheduledEmail>> getAllEmails() {
        log.info("📬 Request received to fetch all scheduled emails");
        List<ScheduledEmail> emails = emailService.findAllEmail();
        log.info("📬 Total emails fetched: {}", emails.size());
        return ResponseEntity.ok(emails);
    }

    // 3️⃣ Get one email by ID
    @GetMapping("/{emailId}")
    public ResponseEntity<ScheduledEmail> getEmailById(@PathVariable UUID emailId) throws EmailNotFound {
        log.info("🔍 Fetching scheduled email with ID: {}", emailId);
        ScheduledEmail email = emailService.getEmail(emailId);
        log.info("✅ Email fetched successfully: {}", emailId);
        return ResponseEntity.ok(email);
    }

    // 4️⃣ Update email
    @PutMapping("/{emailId}")
    public ResponseEntity<String> updateScheduledEmail(@PathVariable UUID emailId, @RequestBody ScheduledEmailRequest request) throws EmailNotFound {
        log.info("✏️ Updating email ID: {} with new request: {}", emailId, request);
        emailService.updateScheduledEmail(emailId, request);
        log.info("✅ Email updated successfully for ID: {}", emailId);
        return ResponseEntity.ok("Scheduled email updated successfully");
    }

    // 5️⃣ Delete a scheduled email
    @DeleteMapping("/{emailId}")
    public ResponseEntity<String> deleteScheduledEmail(@PathVariable UUID emailId) throws EmailNotFound {
        log.info("🗑️ Request to delete email ID: {}", emailId);
        emailService.deleteScheduledEmail(emailId);
        log.info("✅ Email deleted successfully: {}", emailId);
        return ResponseEntity.ok("Scheduled email deleted successfully");
    }

    // 6️⃣ Get only receivers list
    @GetMapping("/{emailId}/receivers")
    public ResponseEntity<List<String>> getReceivers(@PathVariable UUID emailId) throws EmailNotFound, ResourceNotFoundException {
        log.info("📥 Fetching receivers for email ID: {}", emailId);
        List<String> receivers = emailService.getAllReceiversMailId(emailId);
        log.info("✅ Receivers fetched successfully for ID: {}. Count: {}", emailId, receivers.size());
        return ResponseEntity.ok(receivers);
    }
}
