package com.timelyMailer.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduledEmailRequest {

    @NotEmpty(message = "Subject cannot be empty")
    private String subject;

    @NotEmpty(message = "Content cannot be empty")
    private String content;

    @NotEmpty(message = "Receiver email list cannot be empty")
    private List<@Email(message = "Invalid email address") String> receiverMailIds;

    private String filePath;

    @NotNull(message = "Scheduled time is required")
    @Future(message = "Scheduled time must be in the future")
    private LocalDateTime scheduledTime;
}
