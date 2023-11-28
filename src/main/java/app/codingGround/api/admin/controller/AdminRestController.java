package app.codingGround.api.admin.controller;


import app.codingGround.api.account.dto.response.ContactListDto;
import app.codingGround.api.admin.dto.NoticeListDto;
import app.codingGround.api.admin.dto.NoticeRegisterDto;
import app.codingGround.api.account.service.ContactService;
import app.codingGround.api.admin.dto.request.ContactAnswerEditDto;
import app.codingGround.api.admin.dto.response.ContactDetailDto;
import app.codingGround.api.admin.service.ContactAnswerEditService;
import app.codingGround.api.admin.service.NoticeService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.domain.common.dto.response.PageResultDto;
import app.codingGround.global.config.model.ApiResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "어드민 관련 API")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final NoticeService noticeService;
    private final ContactService contactService;
    private final ContactAnswerEditService contactAnswerEditService;

    @GetMapping("/check/token")
    public ResponseEntity<ApiResponse<DefaultResultDto>> securityAdminTest() {
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().message("어드민페이지 테스트").success(true).build()));
    }

    @PostMapping("/notice/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>> postNotice
            (@RequestHeader("Authorization") String accessToken,
             @RequestBody @Validated NoticeRegisterDto noticeRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(noticeService.postNotice(accessToken, noticeRegisterDto)));
    }

    @GetMapping("/notice/list")
    public List<NoticeListDto> getNoticeList() {
        return noticeService.getNoticeList();
    }

//    @GetMapping("/notice/list")
//    public ResponseEntity<ApiResponse<PageResultDto<Object>>> getNoticeList(Pageable pageable) {
//        return ResponseEntity.ok(new ApiResponse<>(noticeService.getNoticeList(pageable)));
//    }

    @GetMapping("/notice/detail/{noticeNum}")
    public NoticeListDto getNoticeDetail(@PathVariable Long noticeNum) {
        return noticeService.getNoticeDetail(noticeNum);
    }

    @PatchMapping("/notice/edit/{noticeNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editNotice
            (@RequestBody @Validated NoticeRegisterDto noticeRegisterDto,
             @PathVariable Long noticeNum) {
        return ResponseEntity.ok(new ApiResponse<>(noticeService.editNotice(noticeRegisterDto, noticeNum)));
    }

    @DeleteMapping("/notice/delete/{noticeNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> deleteNotice (@PathVariable Long noticeNum) {
        return ResponseEntity.ok(new ApiResponse<>(noticeService.deleteNotice(noticeNum)));
    }

    @GetMapping("/user/inquiry/list")
    public List<ContactListDto> getContactList() {
        return contactService.getContactList();
    }

    @GetMapping("/user/inquiry/detail/{contactNum}")
    public ContactDetailDto getContactDetail(@PathVariable Long contactNum) {
        return contactService.getContactDetail(contactNum);
    }

    @PatchMapping("/user/inquiry/edit/{contactNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editContactAnswer(
            @RequestBody @Validated ContactAnswerEditDto contactAnswerEditDto,
            @PathVariable Long contactNum) {
        return ResponseEntity.ok(new ApiResponse<>(contactAnswerEditService.editContactAnswer(contactAnswerEditDto, contactNum)));
    }
}