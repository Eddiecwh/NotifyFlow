package com.eddiecwh.NotificationSystem.model;

import com.eddiecwh.NotificationSystem.entity.RequestType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestBody {
    Long userId;
    String subject;
    String body;
    RequestType requestType;
    LocalDateTime scheduledDate;
    LocalDateTime createdDate;
}
