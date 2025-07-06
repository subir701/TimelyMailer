package com.timelyMailer.service;

import com.timelyMailer.DTO.ScheduledEmailRequest;
import com.timelyMailer.entity.ScheduledEmail;
import com.timelyMailer.entity.Status;
import com.timelyMailer.exception.ApiException;
import com.timelyMailer.exception.EmailNotFound;
import com.timelyMailer.exception.ResourceNotFoundException;
import com.timelyMailer.repository.ScheduledEmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ScheduledEmailServiceImpl implements ScheduledEmailService{

    private ScheduledEmailRepository repository;

    public ScheduledEmailServiceImpl(ScheduledEmailRepository repository){
        this.repository =  repository;
    }
    @Override
    public ScheduledEmail scheduleEmail(ScheduledEmailRequest request) throws ApiException {

        log.info("Scheduling Email request : "+request);

        if(request.getScheduledTime().isBefore(LocalDateTime.now())){
            log.error("Scheduling time : "+ request.getScheduledTime() + "Present Time : "+ LocalDateTime.now());
            throw new ApiException("Scheduled time must be in the future.");
        }

        log.info("Creating ScheduledEmail object");
        ScheduledEmail email = new ScheduledEmail();

        log.info("Setting scheduledEmail properties");
        email.setScheduledTime(request.getScheduledTime());
        email.setSubject(request.getSubject());
        email.setContent(request.getContent());
        email.setReceiverMailIds(request.getReceiverMailIds());
        email.setFilePath(request.getFilePath());
        email.setStatus(Status.PENDING);

        log.info("Adding scheduled email in the database");
        return repository.save(email);
    }

    @Override
    public List<ScheduledEmail> findAllEmail() throws RuntimeException {
        log.info("Getting all the scheduled or already sent email");
        return repository.findAll();
    }

    @Override
    public ScheduledEmail updateScheduledEmail(UUID emailId, ScheduledEmailRequest updatedRequest) throws EmailNotFound,ResourceNotFoundException {

        log.info("Getting email by UUID "+emailId +"for updating any scheduledEmail");
        ScheduledEmail existing = repository.findById(emailId).orElseThrow(() -> new EmailNotFound("Email with ID "+ emailId + " not found."));

        if(existing.getStatus() == Status.SENT || existing.getStatus() == Status.FAILED){
            log.error("Already sent and failed status email cannot updated");
            throw new ResourceNotFoundException("Cannot update already sent email or failed to send");
        }

        log.info("Updating properites of email");
        existing.setSubject(updatedRequest.getSubject());
        existing.setScheduledTime(updatedRequest.getScheduledTime());
        existing.setContent(updatedRequest.getContent());
        existing.setReceiverMailIds(updatedRequest.getReceiverMailIds());
        existing.setFilePath(updatedRequest.getFilePath());
        existing.setUpdatedAt(LocalDateTime.now());

        log.info("Returing updated scheduledEmail object");

        return repository.save(existing);
    }

    @Override
    public ScheduledEmail getEmail(UUID emailId) throws EmailNotFound {
        log.info("Getting scheduled email by Id : "+emailId);
        return repository.findById(emailId).orElseThrow(() -> new EmailNotFound("Email with ID "+ emailId +" not found."));
    }

    @Override
    public List<String> getAllReceiversMailId(UUID emailId) throws EmailNotFound, ResourceNotFoundException {
        log.info("Getting scheduled Email by Id : "+emailId);
        ScheduledEmail email = repository.findById(emailId)
                .orElseThrow(() -> new EmailNotFound("Email with ID " + emailId + " not found."));

        log.info("Getting list of all the reciver gmail Id");
        List<String> receivers = email.getReceiverMailIds();

        if (receivers == null || receivers.isEmpty()) {
            log.error("Receivers list is empty of null.");
            throw new ResourceNotFoundException("No receivers found for email ID " + emailId);
        }

        log.info("Returning list of all the reciver gmail id.");
        return receivers;
    }

    @Override
    public void deleteScheduledEmail(UUID emailId) throws EmailNotFound {
        log.info("Getting scheduled Email by Id : "+emailId);
        ScheduledEmail email = repository.findById(emailId)
                .orElseThrow(() -> new EmailNotFound("Email with ID " + emailId + " not found."));

        log.info("Deleting scheduled email by Id : "+email);
        repository.delete(email);
    }

    @Override
    public List<ScheduledEmail> getDueEmailsForSending() {
        log.info("Returning all the pending scheduled mail");
        return repository.findByStatusAndScheduledTimeLessThanEqual(Status.PENDING, LocalDateTime.now());
    }

    @Override
    public void saveAll(List<ScheduledEmail> emails) {
        log.info("Saving all the mails");
        repository.saveAll(emails);
    }

    @Override
    public void saveOrUpdateEmail(ScheduledEmail email) {
        log.info("saveOrUpdateEmail method Invoked!");
        email.setUpdatedAt(LocalDateTime.now());
        log.info("Updated the last time updated the email");
        repository.save(email);
    }

    @Override
    public List<ScheduledEmail> getAllMailByStatus(Status status) throws ApiException {
        return repository.findByStatus(status).orElseThrow(()-> new ApiException("No Mail found"));
    }
}
