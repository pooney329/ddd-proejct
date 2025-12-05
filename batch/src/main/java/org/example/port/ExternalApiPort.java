package org.example.port;

import org.example.dto.ExternalApiItemDto;

import java.util.List;

public interface ExternalApiPort {
    /**
     * 외부 API의 page/size 기준으로 데이터 조회
     */
    List<ExternalApiItemDto> fetchPage(int page, int size);
}
