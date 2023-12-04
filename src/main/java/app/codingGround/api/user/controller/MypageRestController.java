package app.codingGround.api.user.controller;

import app.codingGround.api.contact.dto.response.InquiryDetailDto;
import app.codingGround.api.contact.dto.response.UserInquiryRegisterDto;
import app.codingGround.api.user.dto.response.GameRecordDto;
import app.codingGround.api.user.dto.response.UserInfoDto;
import app.codingGround.api.user.dto.response.UserInquiryDto;
import app.codingGround.api.user.service.UserService;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.JwtTokenProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "사용자 관련 API")
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageRestController {

   private final UserService userService;


    @GetMapping("/myinfo")
    public ResponseEntity<ApiResponse<UserInfoDto>> getMypageInfo(@RequestHeader("Authorization") String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setUserInfo(userService.getUserInfo(userId));
        userInfo.setRanking(userService.getUserRankings(userId));
        userInfo.setGameBadge(userService.getUserBadge(userId));
        userInfo.setGameLanguage(userService.getUserGameLanguage(userId));
        userInfo.setGameInfoData(userService.getUserGameInfo(userId));

        return ResponseEntity.ok(new ApiResponse<>(userInfo));
    }

    @GetMapping("/gamerecord/{gamenum}")
    public ResponseEntity<ApiResponse<GameRecordDto>>getmyrecord(@PathVariable Long gamenum) {
        GameRecordDto gameRecord = new GameRecordDto();

        gameRecord.setGameInfoData(userService.getGameRecordInfo(gamenum));


        return ResponseEntity.ok(new ApiResponse<>(gameRecord));
    }
    @GetMapping("/myinquiry/{pageNum}")
    public ResponseEntity<ApiResponse<UserInquiryDto>>getmyinquiry(@RequestHeader("Authorization") String accessToken, @PathVariable int pageNum) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        int postNum = pageNum * 7;
        UserInquiryDto userInquiry = new UserInquiryDto();
        userInquiry.setUserInfo(userService.getUserInfo(userId));
        userInquiry.setRanking(userService.getUserRankings(userId));
        userInquiry.setContactList(userService.getContactList(userId, postNum));
        userInquiry.setTotalPageNum(userService.getPageNum(userId));
        userInquiry.setPageNum(pageNum);
        return ResponseEntity.ok(new ApiResponse<>(userInquiry));
    }

    @GetMapping("/myinquiry/detail/{contactNum}")
    public ResponseEntity<ApiResponse<InquiryDetailDto>>getmyinquirydetatil(@PathVariable Long contactNum) {
        return ResponseEntity.ok(new ApiResponse<>(userService.getmyinquirydetail(contactNum)));
    }

    @PostMapping("/inquiry/register")
    public ResponseEntity<ApiResponse<DefaultResultDto>>postinquiry(@RequestHeader("Authorization") String accessToken, @RequestBody @Validated UserInquiryRegisterDto userInquiryRegisterDto) {
        System.out.println(userInquiryRegisterDto.getInquiryContent());
        return ResponseEntity.ok(new ApiResponse<>(userService.postinquiry(accessToken, userInquiryRegisterDto)));
    }



}
