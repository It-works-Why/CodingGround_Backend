package app.codingGround.api.battle.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class QueueInfoDto {
    private String gameId;
    private String connectType; // failed : 연결불가 , succeed : 연결, reConnect : 재접속 여부 선택
}
