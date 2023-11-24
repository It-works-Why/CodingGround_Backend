package app.codingGround.api.admin.controller;


import app.codingGround.api.admin.dto.NoticeListDto;
import app.codingGround.api.admin.dto.NoticeRegisterDto;
import app.codingGround.api.admin.service.NoticeService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "어드민 관련 API")
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    NoticeService noticeService;

    @GetMapping("/check/token")
    public ResponseEntity<ApiResponse<DefaultResultDto>> securityAdminTest() {
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().message("어드민페이지 테스트").success(true).build()));
    }

    @GetMapping("/notice/list")
    public ResponseEntity<ApiResponse<List<NoticeListDto>>> getNoticeList() {
        return ResponseEntity.ok(new ApiResponse<>(noticeService.getNoticeList()));
    }

    @PostMapping("/notice/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>> postNotice(@RequestHeader("Authorization") String accessToken, @RequestBody @Validated NoticeRegisterDto noticeRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(noticeService.postNotice(accessToken, noticeRegisterDto)));
    }

}

