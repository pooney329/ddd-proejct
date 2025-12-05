package org.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalApiItemDto {

    private String id;        // 외부 아이템 ID
    private String name;      // 아이템 이름
    private String status;    // ACTIVE/INACTIVE 등 상태
}
