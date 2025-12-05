package org.example.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BatchConfig {

    @Bean
    public RateLimiter perSecondLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(5)           // 1초에 5번
                .timeoutDuration(Duration.ofSeconds(10)) // <= 최대 10초까지 대기 허용
                .build();

        return RateLimiter
                .of("externalApiPerSecond", config);
    }

    @Bean
    public RateLimiter perMinuteLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(30)          // 1분에 30번
                .timeoutDuration(Duration.ofSeconds(65)) // <= 최대 10초까지 대기 허용
                .build();

        return RateLimiter
                .of("externalApiPerMinute", config);
    }
}
