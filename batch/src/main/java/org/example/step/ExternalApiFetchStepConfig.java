package org.example.step;

import org.example.dto.MyItem;
import org.example.reader.ExternalApiItemReader;
import org.example.wrtier.LoggingMyItemWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ExternalApiFetchStepConfig {

    @Bean
    public Step externalApiFetchStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ExternalApiItemReader reader,
            LoggingMyItemWriter writer
    ) {
        return new StepBuilder("externalApiFetchStep", jobRepository)
                .<MyItem, MyItem>chunk(50, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }
}
