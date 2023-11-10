package app.codingGround.api.test;

import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.config.model.ApiResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "테스트 관련 API")
@RequestMapping("/api/test")
public class TestRestController {
    @GetMapping("/successTest")
    public ResponseEntity<ApiResponse<DefaultResultDto>> successTest() {
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().success(true).message("성공 하셨습니다.").build()));
    }

    @GetMapping("/failTest")
    public ResponseEntity<ApiResponse<DefaultResultDto>> failTest() {
        if(true){
            throw new CustomException("실패했지롱", ErrorCode.TEST_ERROR);
        }
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().success(true).message("성공 하셨습니다.").build()));
    }


}

