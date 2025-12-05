package org.example.client;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.example.dto.ExternalApiItemDto;
import org.example.port.ExternalApiPort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 진짜 HTTP 호출 없이, 메모리 상의 더미 데이터를 페이징으로 잘라주는 구현.
 * "test" 또는 "local" 프로파일에서만 활성화.
 */
@Component
@Profile({"test", "local"})
public class FakeExternalApiClient implements ExternalApiPort {

    private static final int TOTAL_ELEMENTS = 250;

    private final List<ExternalApiItemDto> allItems;
    private final RateLimiter perSecondLimiter;
    private final RateLimiter perMinuteLimiter;

    public FakeExternalApiClient(RateLimiter perSecondLimiter,
                                 RateLimiter perMinuteLimiter) {
        this.perSecondLimiter = perSecondLimiter;
        this.perMinuteLimiter = perMinuteLimiter;
        this.allItems = generateDummyData(TOTAL_ELEMENTS);
    }

    @Override
    public List<ExternalApiItemDto> fetchPage(int page, int size) {
        Supplier<List<ExternalApiItemDto>> supplier =
                () -> fakeApiCall(page, size);

        // 🔥 실제 API 대신 fakeApiCall을 RateLimiter로 감싼다
        Supplier<List<ExternalApiItemDto>> decorated =
                RateLimiter.decorateSupplier(
                        perMinuteLimiter,
                        RateLimiter.decorateSupplier(perSecondLimiter, supplier)
                );

        try {
            return decorated.get(); // 여기서 RateLimiter가 시간 제약을 걸어줌
        } catch (RequestNotPermitted e) {
            // 여기서는 "제약 위반"을 보고 싶을 수 있으니 숨기지 말고 그대로 터뜨리는 게 좋음
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Fake API error", e);
        }
    }

    // 진짜 HTTP 대신, 그냥 메모리 리스트에서 페이징
    private List<ExternalApiItemDto> fakeApiCall(int page, int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allItems.size());

        System.out.println(
                "[FAKE API CALL] page=" + page + ", size=" + size + ", time=" + System.currentTimeMillis()
        );

        if (fromIndex >= allItems.size()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(allItems.subList(fromIndex, toIndex));
    }

    private List<ExternalApiItemDto> generateDummyData(int total) {
        List<ExternalApiItemDto> list = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            list.add(
                    ExternalApiItemDto.builder()
                            .id("FAKE_" + i)
                            .name("Fake Item " + i)
                            .status("ACTIVE")
                            .build()
            );
        }
        return list;
    }
}
