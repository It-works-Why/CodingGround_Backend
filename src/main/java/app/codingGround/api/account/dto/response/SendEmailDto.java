package app.codingGround.api.account.dto.response;

import app.codingGround.api.entity.User;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("SendEmailDto")
public class SendEmailDto {

    private String userEmail;

    public SendEmailDto(User user) {
        this.userEmail = user.getUserEmail();
    }
}
