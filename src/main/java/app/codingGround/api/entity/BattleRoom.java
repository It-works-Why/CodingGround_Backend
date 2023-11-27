package app.codingGround.api.entity;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.util.UUID;
import org.springframework.data.annotation.Id;

@Data
@RedisHash("BattleRoom")
public class BattleRoom {

    @Id
    private String roomId;
    private int maxParticipants;
    private int participants;
    private String gameStatus;

    @PrePersist
    protected void onCreate() {
        maxParticipants = 8;
        participants = 0;
        gameStatus = "대기중";
        roomId = UUID.randomUUID().toString();
    }
}