package app.codingGround.api.admin.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class ContactAnswerEditDto {

    @NotBlank(message = "답변을 입력해주세요.")
    private String contactAnswer;
}
