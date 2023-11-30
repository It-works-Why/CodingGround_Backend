package app.codingGround.api.admin.dto.request;

import app.codingGround.api.entity.TestCase;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AdminQuestionRegisterDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String questionTitle;

    private int questionLimitTime;

    private int questionDifficult;

    // 정답률

    @NotBlank(message = "내용을 입력해주세요.")
    private String questionContent;

    @NotBlank(message = "테스트 케이스를 작성해주세요.")
    private String testCaseInput1;

    @NotBlank(message = "테스트 케이스를 작성해주세요.")
    private String testCaseOutput1;

    @NotBlank(message = "테스트 케이스를 작성해주세요.")
    private String testCaseInput2;

    @NotBlank(message = "테스트 케이스를 작성해주세요.")
    private String testCaseOutput2;

    @NotBlank(message = "테스트 케이스를 작성해주세요.")
    private String testCaseInput3;

    @NotBlank(message = "테스트 케이스를 작성해주세요.")
    private String testCaseOutput3;
}
