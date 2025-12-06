package org.example.job.apijob.wrtier;

import lombok.extern.slf4j.Slf4j;
import org.example.job.apijob.dto.MyItem;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LoggingMyItemWriter implements ItemWriter<MyItem> , StepExecutionListener {

    List<MyItem> items = null;
    @Override
    public void write(Chunk<? extends MyItem> chunk) {
        // 실제 DB 저장 대신 로그로만 확인
        items = (List<MyItem>) chunk.getItems();
//        chunk.forEach(item -> log.info("배치 Writer - item = {}", item));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("[LoggingMyItemWriter] items = " + items.size() + ", time = " + System.currentTimeMillis());
        return ExitStatus.COMPLETED;
    }
}
