package app.codingGround.api.admin.dto.response;

import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@Alias("AdminHomeDataDto")
public class AdminHomeDataDto {

    private int noAnswerCount;
    private int signUpCount;
    private int rankGameCount;
    private int matchingGameCount;
    private List<AdminInquiryDto> adminInquiryDtos;
}
