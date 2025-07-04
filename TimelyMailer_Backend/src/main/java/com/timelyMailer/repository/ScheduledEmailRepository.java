package com.timelyMailer.repository;

import com.timelyMailer.entity.ScheduledEmail;
import com.timelyMailer.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledEmailRepository extends JpaRepository<ScheduledEmail, UUID> {

    // To fetch all emails that are due and still pending

    List<ScheduledEmail> findByScheduledTimeBeforeAndStatus(LocalDateTime currentTime, Status status);
    @EntityGraph(attributePaths = {"receiverMailIds"})
    List<ScheduledEmail> findByStatusAndScheduledTimeLessThanEqual(Status status, LocalDateTime time);

    <S extends ScheduledEmail> List<S> saveAll(Iterable<S> emails);
}
