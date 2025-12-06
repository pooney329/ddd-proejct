package org.example.job.apijob.client;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.example.job.apijob.dto.ExternalApiItemDto;
import org.example.job.apijob.port.ExternalApiPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Profile({"prd"})
@Component
public class ExternalApiClient implements ExternalApiPort {


    private final RateLimiter perSecondLimiter;
    private final RateLimiter perMinuteLimiter;

    private static final String BASE_URL = "https://real-api.example.com/items";

    public ExternalApiClient(
            @Qualifier("perSecondLimiter") RateLimiter perSecondLimiter,
            @Qualifier("perMinuteLimiter") RateLimiter perMinuteLimiter
    ) {
        this.perSecondLimiter = perSecondLimiter;
        this.perMinuteLimiter = perMinuteLimiter;
    }

    @Override
    public List<ExternalApiItemDto> fetchPage(int page, int size) {
        Supplier<List<ExternalApiItemDto>> supplier = () -> doFetchPage(page, size);

        Supplier<List<ExternalApiItemDto>> decorated =
                RateLimiter.decorateSupplier(perMinuteLimiter,
                        RateLimiter.decorateSupplier(perSecondLimiter, supplier));

        try {
            return supplier.get();
        } catch (Exception e) {
            // log.warn("External API call failed", e);
            return Collections.emptyList();
        }
    }

    private List<ExternalApiItemDto> doFetchPage(int page, int size) {
        return Collections.emptyList();
//        URI uri = URI.create(BASE_URL + "?page=" + page + "&size=" + size);
//
//        RequestEntity<Void> request = new RequestEntity<>(HttpMethod.GET, uri);
//        ResponseEntity<ExternalApiResponse> response =
//                restTemplate.exchange(request, ExternalApiResponse.class);
//
//        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
//            return Collections.emptyList();
//        }
//
//        return response.getBody().getItems();
    }
}
