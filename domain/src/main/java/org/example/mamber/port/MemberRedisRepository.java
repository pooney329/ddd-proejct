package org.example.mamber.port;

public interface MemberRedisRepository {

    void saveMember(String key, String value);
}
