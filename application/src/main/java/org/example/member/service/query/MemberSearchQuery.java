package org.example.member.service.query;

public record MemberSearchQuery(
        String name,
        Integer page,
        Integer pageSize
) {
}
