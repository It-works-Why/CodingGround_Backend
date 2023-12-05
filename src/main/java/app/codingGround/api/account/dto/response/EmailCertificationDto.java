package app.codingGround.api.account.dto.response;

import app.codingGround.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Optional;

@Getter
@Alias("SendEmailDto")
@NoArgsConstructor
@AllArgsConstructor
public class EmailCertificationDto {

    private String userEmail;
    private String userId;

    public EmailCertificationDto(User user) {
        this.userEmail = user.getUserEmail();
        this.userId = user.getUserId();
    }
}
