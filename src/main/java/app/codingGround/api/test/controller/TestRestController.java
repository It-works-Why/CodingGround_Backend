package app.codingGround.api.test.controller;

import app.codingGround.api.test.dto.response.TestVo;
import app.codingGround.api.test.service.TestService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.JwtTokenProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "테스트 관련 API")
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestRestController {

    private final TestService testService;

    @GetMapping("/success/test")
    public ResponseEntity<ApiResponse<DefaultResultDto>> successTest() {
        System.out.println("테스트 입니다.!!!!!");
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().success(true).message("성공 하셨습니다.").build()));
    }

    @GetMapping("/fail/test")
    public ResponseEntity<ApiResponse<DefaultResultDto>> failTest() {
        if(true){
            throw new CustomException("실패했지롱", ErrorCode.TEST_ERROR);
        }
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().success(true).message("성공 하셨습니다.").build()));
    }
    @GetMapping("/mybatis/test")
    public ResponseEntity<ApiResponse<TestVo>> mybatisTest(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(new ApiResponse<>(testService.getMybatisTest(JwtTokenProvider.getUserId(accessToken))));
    }


}

