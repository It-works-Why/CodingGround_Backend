package app.codingGround.api.admin.mapper;

import app.codingGround.api.admin.dto.response.AdminInquiryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminHomeMapper {

    int getNoAnswerCount();
    int getSignUpCount();
    int getRankGameCount();
    int getMatchingGameCount();
    List<AdminInquiryDto> getAdminInquiry();
}
