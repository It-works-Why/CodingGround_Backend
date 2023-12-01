package app.codingGround.api.contact.dto.response;

import app.codingGround.api.contact.dto.response.ContactListDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@NoArgsConstructor
@Alias("UserInquiryRegisterDto")
public class UserInquiryRegisterDto {

    // @NotNull : null만 허용x, "", " "은 허용
    // @NotBlank : null, "", " " 모두 허용 x
    @NotBlank(message = "제목을 입력해주세요.")
    private String inquiryTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String inquiryContent;
}
