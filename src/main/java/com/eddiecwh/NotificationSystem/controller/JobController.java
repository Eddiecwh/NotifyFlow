package com.eddiecwh.NotificationSystem.controller;

import com.eddiecwh.NotificationSystem.entity.Job;
import com.eddiecwh.NotificationSystem.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    // only expose 2 endpoints to consumer
    // GET getJobById/{id}
    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJob(@PathVariable Long jobId) {
        return new ResponseEntity<>(jobService.getJob(jobId), HttpStatus.OK);
    }

    // GET getJobByRequestId{requestId}
    @GetMapping("/request/{requestId}")
    public ResponseEntity<Job> getJobByRequestId(@PathVariable Long requestId) {
        return new ResponseEntity<>(jobService.getJobByRequestId(requestId), HttpStatus.OK);
    }

    // GET getAllJobsByUserId{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Job>> getAllJobsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(jobService.getAllJobsByUserId(userId), HttpStatus.OK);
    }
}
