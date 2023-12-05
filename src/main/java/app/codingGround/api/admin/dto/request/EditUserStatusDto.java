package app.codingGround.api.admin.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EditUserStatusDto {

    private Long userNum;

    @NotBlank
    private String userStatus;
}
