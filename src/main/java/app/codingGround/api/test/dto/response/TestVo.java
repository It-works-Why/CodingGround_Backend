package app.codingGround.api.test.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@Alias("testVo")
public class TestVo {
    private String userNickname;
    private String userEmail;
    private String userRole;
}
