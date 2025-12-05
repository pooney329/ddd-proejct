package org.example.reader;

import org.example.Service.ExternalApiService;
import org.example.dto.MyItem;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
@StepScope
public class ExternalApiItemReader extends AbstractItemStreamItemReader<MyItem> {

    private final ExternalApiService externalApiService;

    private int currentPage = 0;
    private boolean finished = false;
    private Iterator<MyItem> currentIterator = Collections.emptyIterator();

    public ExternalApiItemReader(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
        setName("externalApiItemReader");
    }

    @Override
    public MyItem read() {
        if (finished) {
            return null;
        }

        if (!currentIterator.hasNext()) {
            List<MyItem> pageItems = externalApiService.fetchDomainPage(currentPage++);

            if (pageItems == null || pageItems.isEmpty()) {
                finished = true;
                return null;
            }

            currentIterator = pageItems.iterator();
        }

        return currentIterator.next();
    }

}
