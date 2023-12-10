package app.codingGround.api.battle.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ResultDto {
    private List<TestCaseResultDto> testCaseResultDtos;
    private Boolean isRoundEnd;
    private int round;
}
