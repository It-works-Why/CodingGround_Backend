package app.codingGround.api.user.dto.response;

import app.codingGround.api.contact.dto.response.ContactListDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@NoArgsConstructor
@Alias("UserInquiryDto")
public class UserInquiryDto {

    private InfoDto userInfo;
    private List<RankingDto> ranking;
    private List<ContactListDto> ContactList;
    private PageNumDto totalPageNum;
    private int pageNum;
}
