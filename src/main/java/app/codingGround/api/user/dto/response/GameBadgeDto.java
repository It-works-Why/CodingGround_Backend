package app.codingGround.api.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class GameBadgeDto {
    private String BadgeNum;
    private Integer BadgeName;
}
