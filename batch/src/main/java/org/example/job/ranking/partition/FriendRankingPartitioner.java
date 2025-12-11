package org.example.job.ranking.partition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FriendRankingPartitioner implements Partitioner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> executionContextMap = new HashMap<>();
        String sql = """
                    select MIN(member_idx) as minMemberIndex, MAX(member_idx) as maxMemberIndex from friend
                """;
        FriendMinMaxDto friendMinMaxDto = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FriendMinMaxDto.class))
                .stream()
                .findFirst()
                .orElse(null);

        if (Objects.isNull(friendMinMaxDto) || Objects.isNull(friendMinMaxDto.minMemberIndex) || Objects.isNull(friendMinMaxDto.maxMemberIndex)) {
            return executionContextMap;
        }

        // 나누려는 개수
        long size = (friendMinMaxDto.maxMemberIndex - friendMinMaxDto.minMemberIndex) / gridSize + 1;
        // 시작 index
        long start = friendMinMaxDto.getMinMemberIndex();
        // 종료 index
        long end = start + size - 1;


        for (int i = 0; i < gridSize; i++) {
            if (start > friendMinMaxDto.maxMemberIndex) break;
            if (end > friendMinMaxDto.maxMemberIndex) {
                end = friendMinMaxDto.maxMemberIndex;
            }
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.putLong("startIndex", start);
            executionContext.putLong("endIndex", end);

            executionContextMap.put("partition" + i, executionContext);
            start = end + 1;
            end = start + size - 1;
        }
        return executionContextMap;
    }


    @Setter
    @Getter
    public static class FriendMinMaxDto {
        private Integer minMemberIndex;
        private Integer maxMemberIndex;
    }
}
