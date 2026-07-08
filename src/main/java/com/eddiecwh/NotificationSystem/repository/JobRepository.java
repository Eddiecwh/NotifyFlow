package com.eddiecwh.NotificationSystem.repository;

import com.eddiecwh.NotificationSystem.entity.Job;
import com.eddiecwh.NotificationSystem.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByRequestId(Long requestId);

    List<Job> findByRequestUserId(Long userId);

    List<Job> findAllByJobStatusAndRequest_ScheduledDateLessThanEqual(JobStatus status, LocalDateTime dateTime);

    List<Job> findAllByJobStatusAndNextRetryDtLessThanEqual(JobStatus status, LocalDateTime dateTime);
}
