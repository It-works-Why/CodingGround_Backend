package app.codingGround.api.contact.dto.response;

import lombok.Getter;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("InquiryDetailDto")
public class InquiryDetailDto {

    private String contactTitle;
    private String userNickname;
    private String contactContent;
    private String contactAnswer;
    private int useStatus;
}
