package com.eddiecwh.NotificationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    String email;
    String phone;

    @Column(name = "created_dt")
    LocalDateTime createdDt;

    @Column(name = "updated_dt")
    LocalDateTime updatedDt;
}
