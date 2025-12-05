package org.example.Service;

import org.example.dto.ExternalApiItemDto;
import org.example.dto.MyItem;
import org.example.port.ExternalApiPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalApiService {

    private final ExternalApiPort externalApiPort;

    private static final int API_PAGE_SIZE = 5;

    public ExternalApiService(ExternalApiPort externalApiPort) {
        this.externalApiPort = externalApiPort;
    }

    public List<MyItem> fetchDomainPage(int page) {
        List<ExternalApiItemDto> dtos = externalApiPort.fetchPage(page, API_PAGE_SIZE);

        return dtos.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private MyItem toDomain(ExternalApiItemDto dto) {
        return MyItem.builder()
                .externalId(dto.getId())
                .name(dto.getName())
                .status(dto.getStatus())
                .build();
    }
}
