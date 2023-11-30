package app.codingGround.api.admin.dto.response;

import app.codingGround.api.entity.TestCase;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminQuestionListDto {

    private Long questionNum;
    private String questionTitle;
    private int questionLimitTime;
    private int questionDifficult;
    private String questionContent;
    private Long testCaseNum;
    private String testCaseInput;
    private String testCaseOutput;

    public AdminQuestionListDto(TestCase testCase) {

        this.questionNum = testCase.getQuestion().getQuestionNum();
        this.questionTitle = testCase.getQuestion().getQuestionTitle();
        this.questionLimitTime = testCase.getQuestion().getQuestionLimitTime();
        this.questionDifficult = testCase.getQuestion().getQuestionDifficult();
        this.questionContent = testCase.getQuestion().getQuestionContent();
        this.testCaseNum = testCase.getTestCaseNum();
        this.testCaseInput = testCase.getTestCaseInput();
        this.testCaseOutput = testCase.getTestCaseOutput();
    }
}
