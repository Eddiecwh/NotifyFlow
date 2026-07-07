package com.eddiecwh.NotificationSystem.repository;

import com.eddiecwh.NotificationSystem.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByUserId(Long userId);
}
