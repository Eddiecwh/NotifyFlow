package com.eddiecwh.NotificationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;

    @Enumerated(EnumType.STRING)
    RequestType requestType;

    String subject;
    String body;

    @Column(name = "scheduled_dt")
    LocalDateTime scheduledDate;

    @Column(name = "created_dt")
    LocalDateTime createdDate;

    @Column(name = "update_dt")
    LocalDateTime updateDate;
}
