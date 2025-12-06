package org.example.job.apijob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ExternalApiSyncJobConfig {

    @Bean
    public Job externalApiSyncJob(JobRepository jobRepository, Step externalApiFetchStep) {
        return new JobBuilder("externalApiSyncJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(externalApiFetchStep)
                .build();
    }
}
