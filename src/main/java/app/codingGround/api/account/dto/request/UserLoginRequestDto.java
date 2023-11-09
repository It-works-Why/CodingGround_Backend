package app.codingGround.api.account.dto.request;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String userId;
    private String password;
}