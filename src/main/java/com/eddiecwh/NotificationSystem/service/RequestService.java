package com.eddiecwh.NotificationSystem.service;

import com.eddiecwh.NotificationSystem.entity.Request;
import com.eddiecwh.NotificationSystem.entity.User;
import com.eddiecwh.NotificationSystem.model.RequestBody;
import com.eddiecwh.NotificationSystem.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final JobService jobService;

    public Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public List<Request> getAllRequestsByUserId(Long userId) {
        return requestRepository.findAllByUserId(userId);
    }

    @Transactional
    public Request createRequest(RequestBody requestBody) {
        Request request = new Request();
        User user = userService.getUserById(requestBody.getUserId());

        request.setUser(user);
        request.setSubject(requestBody.getSubject());
        request.setBody(requestBody.getBody());
        request.setRequestType(requestBody.getRequestType());

        request.setCreatedDate(LocalDateTime.now());
        request.setUpdateDate(LocalDateTime.now());
        request.setScheduledDate(requestBody.getScheduledDate());

        Request savedRequest = requestRepository.save(request);
        jobService.createJob(savedRequest);

        return savedRequest;
    }

    public Request updateRequest(Long requestId, RequestBody requestBody) {
        Request request = new Request();

        request.setId(requestId);
        request.setBody(requestBody.getBody());
        request.setRequestType(request.getRequestType());
        request.setSubject(requestBody.getSubject());

        request.setScheduledDate(requestBody.getScheduledDate());
        request.setUpdateDate(LocalDateTime.now());

        return requestRepository.save(request);
    }

    public void cancelRequest(Long requestId) {
        jobService.cancelJobByRequestId(requestId);
    }
}
