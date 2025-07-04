package com.timelyMailer.service;

import com.timelyMailer.DTO.ScheduledEmailRequest;
import com.timelyMailer.entity.ScheduledEmail;
import com.timelyMailer.exception.ApiException;
import com.timelyMailer.exception.EmailNotFound;
import com.timelyMailer.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ScheduledEmailService {

    ScheduledEmail scheduleEmail(ScheduledEmailRequest request) throws ApiException;

    List<ScheduledEmail> findAllEmail() throws RuntimeException;

    ScheduledEmail updateScheduledEmail(UUID emailId, ScheduledEmailRequest updatedRequest) throws EmailNotFound, ResourceNotFoundException;

    ScheduledEmail getEmail(UUID emailId) throws EmailNotFound;

    List<String> getAllReceiversMailId(UUID emailId) throws EmailNotFound, ResourceNotFoundException;

    void deleteScheduledEmail(UUID emailId) throws EmailNotFound;

    List<ScheduledEmail> getDueEmailsForSending();

    void saveAll(List<ScheduledEmail> emails);

    void saveOrUpdateEmail(ScheduledEmail email);

}
