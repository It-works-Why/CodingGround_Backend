package app.codingGround.api.test.service;

import app.codingGround.api.test.Mapper.TestMapper;
import app.codingGround.api.test.dto.response.TestVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestMapper testMapper;

    public TestVo getMybatisTest() {
        return testMapper.getMybatisTest();
    }
}
