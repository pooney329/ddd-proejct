package org.example.member.repository;

import lombok.RequiredArgsConstructor;
import org.example.mamber.port.MemberRedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRedisImplRepository implements MemberRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public void saveMember(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
