package com.eddiecwh.NotificationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "request_id")
    Request request;

    @Enumerated(EnumType.STRING)
    JobStatus jobStatus;

    @Column(name="job_payload")
    String jobPayload;

    @Column(name="retry_count")
    Integer retryCount;

    @Column(name="next_retry_dt")
    LocalDateTime nextRetryDt;

    @Column(name="sent_dt")
    LocalDateTime sentDt;

    @Column(name="error_message")
    String errorMessage;

    @Column(name="created_dt")
    LocalDateTime createdDt;

    @Column(name = "updated_dt")
    LocalDateTime updatedDt;
}
