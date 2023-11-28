package app.codingGround.api.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class GameLanguageDto {
    private Integer Count1;
    private Integer Count2;
    private Integer Count3;
    private Integer Count4;
    private Integer Count5;
}
