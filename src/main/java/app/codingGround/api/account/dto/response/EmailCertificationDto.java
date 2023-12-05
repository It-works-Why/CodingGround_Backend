package app.codingGround.api.account.dto.response;

import app.codingGround.api.entity.User;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.util.Optional;

@Getter
@Alias("SendEmailDto")
public class EmailCertificationDto {

    private String userEmail;

    public EmailCertificationDto(User user) {
        this.userEmail = user.getUserEmail();
    }
}
