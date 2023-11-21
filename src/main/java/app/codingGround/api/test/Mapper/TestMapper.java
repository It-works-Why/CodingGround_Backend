package app.codingGround.api.test.Mapper;

import app.codingGround.api.test.dto.response.TestVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
    TestVo getMybatisTest(String userId);
}
