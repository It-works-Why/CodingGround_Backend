package app.codingGround.api.admin.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
public class ContactDetailDto {

    private Long contactNum;
    private String contactTitle;
    private String contactContent;
    private String userNickname;
    private String userProfileImg;
    private String contactTime;
    private String contactAnswer;
    private int useStatus;
}
