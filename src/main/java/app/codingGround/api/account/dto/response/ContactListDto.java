package app.codingGround.api.account.dto.response;

import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Getter
@Alias("ContactListDto")
public class ContactListDto {

    private Long contactNum;
    private String contactTitle;
    private String contactTime;
    private String userNickname;
    private int answerStatus;
}
