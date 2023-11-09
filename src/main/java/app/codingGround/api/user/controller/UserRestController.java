package app.codingGround.api.user.controller;

import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "사용자 관련 API")
@RequestMapping("/api/user")
public class UserRestController {


    @GetMapping("/test")
    public ResponseEntity<ApiResponse<DefaultResultDto>> emailDuplicationCheck() {
        return ResponseEntity.ok(new ApiResponse<>(null));
    }

}
