package app.codingGround.api.user.mapper;

import app.codingGround.api.contact.dto.response.ContactListDto;
import app.codingGround.api.contact.dto.response.InquiryDetailDto;
import app.codingGround.api.user.dto.response.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    InfoDto getUserInfo(String userId);

    List<RankingDto> getUserRankings(String userId);

    List<GameBadgeDto> getUserBadge(String userId);
    List<GameLanguageDto> UserGameLanguage(String userId);
    List<GameInfoDto> getUserGameInfo(String userId);
    List<GameInfoDto> getGameRecordInfo(Long gamenum);
    List<ContactListDto> getContactList(String userId , int postNum);
    PageNumDto getPageNum(String userId);
    InquiryDetailDto getmyinquirydetail(Long contactNum);

}
