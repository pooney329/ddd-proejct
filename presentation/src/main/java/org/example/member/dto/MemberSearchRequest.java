package org.example.member.dto;

public record MemberSearchRequest(
    String name,
    Integer page,
    Integer pageSize
) {
}
