package com.eddiecwh.NotificationSystem.service;

import com.eddiecwh.NotificationSystem.entity.Job;
import com.eddiecwh.NotificationSystem.entity.JobStatus;
import com.eddiecwh.NotificationSystem.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final JobRepository jobRepository;
    private final JobService jobService;

    @Scheduled(fixedRate = 60000)
    public void processScheduledJobs() {
        List<Job> scheduledJobList = jobRepository.
                findAllByJobStatusAndRequest_ScheduledDateLessThanEqual(JobStatus.SCHEDULED, LocalDateTime.now());

        for (Job job : scheduledJobList) {
            jobService.releaseScheduledJob(job);
            log.info("Adding jobId: {} to active queue", job.getId());
        }
    }
}
