package app.codingGround.api.admin.dto.response;

import app.codingGround.api.contact.dto.response.ContactListDto;
import app.codingGround.api.user.dto.response.InfoDto;
import app.codingGround.api.user.dto.response.PageNumDto;
import app.codingGround.api.user.dto.response.RankingDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@Builder
@Alias("AdminInquiryDto")
public class AdminInquiryDto {

    private String contactNum;
    private String contactTitle;
    private String userNickname;
    private String contactTime;

}
