package app.codingGround.api.entity;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;

@Data
@RedisHash("GameRoom")
public class GameRoom {

    @Id
    private String gameId;
    private String gameType; // RANK, NORMAL
    private String gameLanguage; // JAVA, PYTHON, C++, C#, JAVASCRIPT
    private int gameRound; // 1, 2
    private List<String> gameUser; // {유저 닉네임1, 유저 닉네임2}
    private int gameMaxParticipants; // 8
    private String gameStatus; // WAITING, STARTED



    @PrePersist
    protected void onCreate() {
        gameId = UUID.randomUUID().toString();
        gameRound = 1;
        gameMaxParticipants = 8;
        gameStatus = "대기중";
        gameUser = new ArrayList<>();
    }
}