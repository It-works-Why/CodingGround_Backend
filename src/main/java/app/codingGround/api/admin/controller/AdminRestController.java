package app.codingGround.api.admin.controller;

import app.codingGround.api.admin.dto.request.EditUserStatusDto;
import app.codingGround.api.admin.dto.response.*;
import app.codingGround.api.admin.service.*;
import app.codingGround.api.admin.dto.request.AdminNoticeRegisterDto;
import app.codingGround.api.contact.dto.response.ContactListWithTotalPageDto;
import app.codingGround.api.contact.service.ContactService;
import app.codingGround.api.admin.dto.request.AdminQuestionRegisterDto;
import app.codingGround.api.admin.dto.request.ContactAnswerEditDto;
import app.codingGround.api.entity.Community;
import app.codingGround.api.entity.Notice;
import app.codingGround.api.notice.service.NoticeService;
import app.codingGround.api.entity.Question;
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
    private final NoticeService noticeService;

    // 문의사항
    private final ContactService contactService;
    private final ContactAnswerEditService contactAnswerEditService;
    private final AdminQuestionService adminQuestionService;

    // 유저관리
    private final UserManageService userManageService;

    //커뮤니티
    private final AdminCommunityService adminCommunityService;

    @GetMapping("/check/token")
    public ResponseEntity<ApiResponse<DefaultResultDto>> securityAdminTest() {
        return ResponseEntity.ok(new ApiResponse<>(DefaultResultDto.builder().message("어드민페이지 테스트").success(true).build()));
    }

    // notice

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


    // 관리자 문의사항 컨트롤러
    // inquiry

    @GetMapping("/user/inquiry/list/{pageNum}")
    public ContactListWithTotalPageDto getContactList(
            @RequestParam(name = "searchInput", defaultValue = "") String searchInput,
            @PathVariable int pageNum) {
        return contactService.getContactList(searchInput, pageNum);
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

    // 관리자 유저관리 컨트롤러
    @GetMapping("/user/list/{pageNum}")
    public UserManageListWithTotalPageDto getUserManageList(
            @RequestParam(name = "searchInput", defaultValue = "") String searchInput,
            @PathVariable int pageNum) {
        return userManageService.getUserManageList(searchInput, pageNum);
    }

    @PatchMapping("/user/changestatus")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editUserStatus
            (@RequestBody @Validated EditUserStatusDto editUserStatusDto) {
        return ResponseEntity.ok(new ApiResponse<>(userManageService.editUserStatus(editUserStatusDto)));
    }

    // question
    @PostMapping("/question/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>> postQuestion(@RequestBody @Validated AdminQuestionRegisterDto adminQuestionRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(adminQuestionService.postQuestion(adminQuestionRegisterDto)));
    }

    @GetMapping("/question/list")
    public Page<Question> getSearchQuestionList(
            @PageableDefault(size = 10, sort = "questionNum", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "keyword", required = false) String keyword) {

        System.out.println("keyword : " + keyword);
        if (keyword == null) {
            Page<Question> questionList = adminQuestionService.getQuestionList(pageable);
            return questionList;
        } else {
            Page<Question> questionList = adminQuestionService.getSearchQuestionList(pageable, keyword);
            return questionList;
        }
    }

    @GetMapping("/question/detail/{questionNum}")
    public List<AdminQuestionListDto> getQuestionDetail(@PathVariable Long questionNum) {
        return adminQuestionService.getQuestionDetail(questionNum);
    }

    @PatchMapping("/question/edit/{questionNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editQuestion
            (@RequestBody @Validated AdminQuestionRegisterDto adminQuestionRegisterDto,
             @PathVariable Long questionNum) {
        return ResponseEntity.ok(new ApiResponse<>(adminQuestionService.editQuestion(adminQuestionRegisterDto, questionNum)));
    }

    @PatchMapping("/question/delete/{questionNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> deleteQuestion(@PathVariable Long questionNum) {
        return ResponseEntity.ok(new ApiResponse<>(adminQuestionService.deleteQuestion(questionNum)));
    }

    //community

    @GetMapping("/community/list")
    public Page<Community> getcommunityList(
            @PageableDefault(size = 10, sort = "postNum", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "type", required = false) String type) {
        if (keyword == null) {
            Page<Community> communityList = adminCommunityService.getCommunityList(pageable);
            return communityList;
        }else {
            Page<Community> communityList =  adminCommunityService.getSearchCommunityList(pageable, keyword, type);
            return communityList;
        }
    }

    @GetMapping("/community/detail/{postNum}")
    public AdminCommunityListDto getcommunityDetail(@PathVariable Long postNum) {
        return adminCommunityService.getcommunityDetail(postNum);
    }

    @PatchMapping("/community/delete/{postNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> deletecommunity(@PathVariable Long postNum) {
        return ResponseEntity.ok(new ApiResponse<>(adminCommunityService.deleteCommunity(postNum)));
    }


}