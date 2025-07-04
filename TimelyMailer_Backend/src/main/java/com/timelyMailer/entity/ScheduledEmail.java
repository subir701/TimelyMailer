package com.timelyMailer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "email_id")
    private UUID emailId;

    //Subject of the mail subject
    @Column(nullable = false)
    private String subject;

    //Content of mail
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    //Storing Scheduling time
    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    //List of all receiver email
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "email_receivers", joinColumns =  @JoinColumn(name = "email_id", referencedColumnName = "email_id"))
    @Column(name = "receiver_email")
    private List<String> receiverMailIds;

    //Sharing any file
    private String filePath;

    //For maintaining status
    @Enumerated(value = EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int retryCount;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if(this.status == null){
            this.status= Status.PENDING;
        }
        this.retryCount = 0;
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
