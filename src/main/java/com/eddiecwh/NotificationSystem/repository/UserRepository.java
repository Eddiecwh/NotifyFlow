package com.eddiecwh.NotificationSystem.repository;

import com.eddiecwh.NotificationSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
