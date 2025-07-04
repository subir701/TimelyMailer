package com.timelyMailer.service;

import com.timelyMailer.DTO.ScheduledEmailRequest;
import com.timelyMailer.entity.ScheduledEmail;
import com.timelyMailer.entity.Status;
import com.timelyMailer.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.timelyMailer.repository.ScheduledEmailRepository;
import org.mockito.MockitoAnnotations;


import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduledEmailServiceImplTest {

    @InjectMocks
    private ScheduledEmailServiceImpl emailService;

    @Mock
    private ScheduledEmailRepository repository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScheduleEmail_Success() throws ApiException {

        ScheduledEmailRequest request = new ScheduledEmailRequest();

        request.setSubject("Test Email");
        request.setContent("This is a test");
        request.setReceiverMailIds(List.of("test@example.com"));
        request.setScheduledTime(LocalDateTime.now().plusMinutes(10));
        request.setFilePath("https://drive.google.com/test");

        ScheduledEmail expectedEmail = new ScheduledEmail();
        expectedEmail.setSubject(request.getSubject());
        expectedEmail.setContent(request.getContent());
        expectedEmail.setReceiverMailIds(request.getReceiverMailIds());
        expectedEmail.setScheduledTime(request.getScheduledTime());
        expectedEmail.setFilePath(request.getFilePath());
        expectedEmail.setStatus(Status.PENDING);

        when(repository.save(any(ScheduledEmail.class))).thenReturn(expectedEmail);

        ScheduledEmail result = emailService.scheduleEmail(request);

        assertEquals("Test Email", result.getSubject());
        assertEquals(Status.PENDING, result.getStatus());
        verify(repository, times(1)).save(any(ScheduledEmail.class));
    }

    @Test
    public void testScheduleEmail_FailureDueToPastTime(){
        ScheduledEmailRequest request = new ScheduledEmailRequest();

        request.setSubject("Late Email");
        request.setContent("Old content");
        request.setReceiverMailIds(List.of("test@gmail.com"));
        request.setScheduledTime(LocalDateTime.now().minusMinutes(5));

        ApiException exception = assertThrows(ApiException.class, () -> {
            emailService.scheduleEmail(request);
        });

        assertEquals("Scheduled time must be in the future.", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void findAllEmail() {
    }

    @Test
    void testUpdateScheduledEmail_Success() throws Exception {
        UUID emailId = UUID.randomUUID();

        ScheduledEmail existing = new ScheduledEmail();
        existing.setSubject("Old Subject");
        existing.setContent("Old Content");
        existing.setScheduledTime(LocalDateTime.now().plusMinutes(5));
        existing.setStatus(Status.PENDING);
        existing.setReceiverMailIds(List.of("old@example.com"));
        existing.setFilePath(null);

        ScheduledEmailRequest updatedRequest = new ScheduledEmailRequest();
        updatedRequest.setSubject("Updated Subject");
        updatedRequest.setContent("Updated Content");
        updatedRequest.setScheduledTime(LocalDateTime.now().plusMinutes(30));
        updatedRequest.setReceiverMailIds(List.of("updated@example.com"));
        updatedRequest.setFilePath("https://drive.google.com/updated");

        when(repository.findById(emailId)).thenReturn(Optional.of(existing));
        when(repository.save(any(ScheduledEmail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduledEmail result = emailService.updateScheduledEmail(emailId, updatedRequest);

        assertEquals("Updated Subject", result.getSubject());
        assertEquals("Updated Content", result.getContent());
        assertEquals("updated@example.com", result.getReceiverMailIds().get(0));
        assertEquals(Status.PENDING, result.getStatus());

        verify(repository, times(1)).findById(emailId);
        verify(repository, times(1)).save(any(ScheduledEmail.class));
    }

    @Test
    void testGetEmail_Success() {
        ScheduledEmail expected = new ScheduledEmail();
        UUID mockId = UUID.randomUUID();
        expected.setEmailId(mockId);
        expected.setSubject("Test retireving mail");
        expected.setContent("Old content");
        expected.setReceiverMailIds(List.of("test@gmail.com"));
        expected.setScheduledTime(LocalDateTime.now().plusMinutes(5));
        expected.setStatus(Status.PENDING);

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(expected));

        ScheduledEmail result = emailService.getEmail(mockId);
        assertEquals("Test retireving mail",result.getSubject());
        assertEquals(Status.PENDING,result.getStatus());
        verify(repository,times(1)).findById(any(UUID.class));
    }

    @Test
    void getAllReceiversMailId() {
    }

    @Test
    void testDeleteScheduledEmail_Success() throws Exception{
        UUID emailId = UUID.randomUUID();

        ScheduledEmail existing = new ScheduledEmail();
        existing.setEmailId(emailId);

        when(repository.findById(emailId)).thenReturn(Optional.of(existing));

        emailService.deleteScheduledEmail(emailId);

        verify(repository, times(1)).delete(existing);
    }

    @Test
    void getDueEmailsForSending() {
    }

    @Test
    void saveAll() {
    }

    @Test
    void saveOrUpdateEmail() {
    }
}