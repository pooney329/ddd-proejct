package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SsPlusBatchApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args);
        // 원하는 작업 수행 후 애플리케이션 종료
        SpringApplication.exit(context, () -> 0);
    }
}
