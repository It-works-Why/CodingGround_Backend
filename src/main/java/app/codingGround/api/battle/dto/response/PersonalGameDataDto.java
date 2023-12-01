package app.codingGround.api.battle.dto.response;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class PersonalGameDataDto {
    private String userId;
    private String gameId;
    private String status; // 'WAITING', 'PLAYING', 'DISCONNECT'
}
