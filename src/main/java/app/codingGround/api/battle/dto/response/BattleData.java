package app.codingGround.api.battle.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class BattleData {
    private QuestionDto questionDto;
    private List<TestCaseDto> testCase;
}
