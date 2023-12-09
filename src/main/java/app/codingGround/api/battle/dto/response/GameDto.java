package app.codingGround.api.battle.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class GameDto {
    private String gameId;
    private long gameMaxParticipants;
    private String gameStatus;
    private Integer gameLanguage;
    private String gameType;
    private int gameRound;
    private Long gameNum;
    private Timestamp firstRoundEndTime;
    private Timestamp secondRoundEndTime;
}
