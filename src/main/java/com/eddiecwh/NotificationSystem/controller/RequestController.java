package com.eddiecwh.NotificationSystem.controller;

import com.eddiecwh.NotificationSystem.entity.Request;
import com.eddiecwh.NotificationSystem.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/{requestId}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long requestId) {
        return new ResponseEntity<>(requestService.getRequestById(requestId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Request> createRequest(@RequestBody com.eddiecwh.NotificationSystem.model.RequestBody requestBody) {
        return new ResponseEntity<>(requestService.createRequest(requestBody), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Request>> getAllRequestsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(requestService.getAllRequestsByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<Request> updateRequest(@PathVariable Long requestId,
                                                 @RequestBody com.eddiecwh.NotificationSystem.model.RequestBody requestBody) {
        return new ResponseEntity<>(requestService.updateRequest(requestId, requestBody), HttpStatus.OK);
    }

    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long requestId) {
        requestService.cancelRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
