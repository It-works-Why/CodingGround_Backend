package app.codingGround.api.admin.controller;

import app.codingGround.api.account.dto.response.ContactListDto;
import app.codingGround.api.admin.dto.AdminNoticeListDto;
import app.codingGround.api.admin.dto.AdminNoticeRegisterDto;
import app.codingGround.api.account.service.ContactService;
import app.codingGround.api.admin.dto.request.ContactAnswerEditDto;
import app.codingGround.api.admin.dto.response.ContactDetailDto;
import app.codingGround.api.admin.service.ContactAnswerEditService;
import app.codingGround.api.admin.service.AdminNoticeService;
import app.codingGround.api.entity.Notice;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "어드민 관련 API")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminNoticeService adminNoticeService;
    private final ContactService contactService;
    private final ContactAnswerEditService contactAnswerEditService;

    @GetMapping("/check/token")
    public ResponseEntity<ApiResponse<DefaultResultDto>> securityAdminTest() {
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().message("어드민페이지 테스트").success(true).build()));
    }

    @PostMapping("/notice/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>> postNotice
            (@RequestHeader("Authorization") String accessToken,
             @RequestBody @Validated AdminNoticeRegisterDto adminNoticeRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(adminNoticeService.postNotice(accessToken, adminNoticeRegisterDto)));
    }

    @GetMapping("/notice/list")
    public Page<Notice> getNoticeList(
            @PageableDefault(size = 10, sort = "noticeNum", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Notice> noticeList = adminNoticeService.getNoticeList(pageable);
        return noticeList;
    }

    @GetMapping("/notice/detail/{noticeNum}")
    public AdminNoticeListDto getNoticeDetail(@PathVariable Long noticeNum) {
        return adminNoticeService.getNoticeDetail(noticeNum);
    }

    @PatchMapping("/notice/edit/{noticeNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editNotice
            (@RequestBody @Validated AdminNoticeRegisterDto adminNoticeRegisterDto,
             @PathVariable Long noticeNum) {
        return ResponseEntity.ok(new ApiResponse<>(adminNoticeService.editNotice(adminNoticeRegisterDto, noticeNum)));
    }

    @PatchMapping("/notice/delete/{noticeNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> deleteNotice(@PathVariable Long noticeNum) {
        return ResponseEntity.ok(new ApiResponse<>(adminNoticeService.deleteNotice(noticeNum)));
    }

    @GetMapping("/user/inquiry/list")
    public List<ContactListDto> getContactList(
            @RequestParam(name = "searchInput", defaultValue = "") String searchInput) {
        return contactService.getContactList(searchInput);
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