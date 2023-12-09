package app.codingGround.api.battle.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CodeData {
    private String userId;
    private String code;
    private String type;
    private Integer languageCode;
}
