package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalApiResponse {

    private List<ExternalApiItemDto> items;   // 실제 데이터 리스트

    private int page;          // 현재 페이지
    private int size;          // 페이지 크기
    private int totalPages;    // 전체 페이지 수
    private long totalElements; // 전체 데이터 개수
}
