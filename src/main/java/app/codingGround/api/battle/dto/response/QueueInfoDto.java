package app.codingGround.api.battle.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class QueueInfoDto {
    private String gameId;
    private Boolean isReconnect;
}
