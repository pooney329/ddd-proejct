package org.example.job.apijob.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyItem {
    private Long id;

    private String externalId;   // ExternalApiItemDto.id 와 매핑됨

    private String name;

    private String status;
}
