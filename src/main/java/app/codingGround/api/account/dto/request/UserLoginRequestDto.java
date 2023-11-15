package app.codingGround.api.account.dto.request;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotNull;

@Data
public class UserLoginRequestDto {

    private String userId;
    private String password;
}