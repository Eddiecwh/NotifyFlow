package com.eddiecwh.NotificationSystem.service;

import com.eddiecwh.NotificationSystem.config.RabbitMQConfig;
import com.eddiecwh.NotificationSystem.entity.Job;
import com.eddiecwh.NotificationSystem.entity.JobStatus;
import com.eddiecwh.NotificationSystem.entity.Request;
import com.eddiecwh.NotificationSystem.entity.RequestType;
import com.eddiecwh.NotificationSystem.exception.JobNotFoundException;
import com.eddiecwh.NotificationSystem.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final AmqpTemplate amqpTemplate;

    private static final int MAX_RETRIES = 5;

    public Job getJob(Long jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException("Job not found for job ID: " + jobId));
    }

    public Job getJobByRequestId(Long requestId) {
        return jobRepository.findByRequestId(requestId).orElseThrow(() -> new JobNotFoundException("Job not found for request ID: " + requestId));
    }

    public List<Job> getAllJobsByUserId(Long userId) {
        return jobRepository.findByRequestUserId(userId);
    }

    @Transactional
    public Job createJob(Request request) {
        Job job = new Job();

        job.setRequest(request);
        job.setJobPayload(request.getBody());
        job.setRetryCount(0);
        job.setCreatedDt(LocalDateTime.now());
        job.setUpdatedDt(LocalDateTime.now());

        return processJob(job);
    }

    private Job processJob(Job job) {
        Request request = job.getRequest();

        if (request.getScheduledDate() == null ||
                request.getScheduledDate().isBefore(LocalDateTime.now())) {
            job.setJobStatus(JobStatus.QUEUED);
        } else {
            job.setJobStatus(JobStatus.SCHEDULED);
        }

        Job savedJob = jobRepository.save(job);

        if (savedJob.getJobStatus() == JobStatus.QUEUED) {
            publishJob(savedJob);
        }

        return savedJob;
    }

    private void publishJob(Job job) {
        if (job.getRequest().getRequestType() == RequestType.EMAIL) {
            amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.EMAIL_ROUTING_KEY, job);
        } else {
            amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.TEXT_ROUTING_KEY, job);
        }
    }

    @Transactional
    public void releaseJob(Job job) {
        job.setJobStatus(JobStatus.QUEUED);
        job.setUpdatedDt(LocalDateTime.now());
        Job savedJob = jobRepository.save(job);
        publishJob(savedJob);
    }

    @Transactional
    public Job cancelJobByRequestId(Long requestId) {
        Job job = getJobByRequestId(requestId);
        job.setJobStatus(JobStatus.CANCELLED);
        job.setUpdatedDt(LocalDateTime.now());
        // logic to stop job execution
        return jobRepository.save(job);
    }

    @Transactional
    public Job updateJobStatus(Long jobId, JobStatus jobStatus) {
        Job job = getJob(jobId);
        job.setJobStatus(jobStatus);
        job.setUpdatedDt(LocalDateTime.now());
        return jobRepository.save(job);
    }

    @Transactional
    public Job markJobAsSent(Long jobId) {
        Job job = getJob(jobId);
        job.setJobStatus(JobStatus.SENT);
        job.setErrorMessage(null);
        job.setUpdatedDt(LocalDateTime.now());
        job.setSentDt(LocalDateTime.now());

        return jobRepository.save(job);
    }

    @Transactional
    public Job markJobAsFailed(Long jobId, String error) {
        Job job = getJob(jobId);
        job.setJobStatus(JobStatus.FAILED);
        job.setUpdatedDt(LocalDateTime.now());
        job.setErrorMessage(error);

        return jobRepository.save(job);
    }

    @Transactional
    public Job markJobAsRetry(Long jobId, String error) {
        Job job = getJob(jobId);

        int retryCount = job.getRetryCount();

        if (retryCount >= MAX_RETRIES) {
            job.setJobStatus(JobStatus.FAILED);
            job.setUpdatedDt(LocalDateTime.now());
            job.setErrorMessage("Maximum retry attempts exceeded");
        } else {
            job.setJobStatus(JobStatus.RETRYING);
            job.setUpdatedDt(LocalDateTime.now());
            long backOffMinutes = (long) Math.pow(2, retryCount);
            job.setNextRetryDt(LocalDateTime.now().plusMinutes(backOffMinutes));
            job.setErrorMessage(error);
            retryCount += 1;
            job.setRetryCount(retryCount);
        }

        return jobRepository.save(job);
    }
}
