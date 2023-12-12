package app.codingGround.api.community.controller;

import app.codingGround.api.community.dto.CommunityListDto;
import app.codingGround.api.community.dto.CommunityRegisterDto;
import app.codingGround.api.community.service.CommunityService;
import app.codingGround.api.entity.Community;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityRestController {

    private final CommunityService communityService;

    @GetMapping("/list")
    public Page<Community> getCommunityList(
            @PageableDefault(size = 10, sort = "postNum", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "searchInput", required = false) String searchInput) {
        if (searchInput == null) {
            Page<Community> communityList = communityService.getCommunityList(pageable);
            return communityList;
        } else {
            Page<Community> communityList = communityService.getSearchCommunityList(pageable, searchInput);
            return communityList;
        }
    }

    @PostMapping("/write")
    public ResponseEntity<ApiResponse<DefaultResultDto>> postCommunity
            (@RequestHeader("Authorization") String accessToken,
             @RequestBody @Validated CommunityRegisterDto communityRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.postCommunity(accessToken, communityRegisterDto)));
    }

    @GetMapping("/detail/{postNum}")
    public CommunityListDto getCommunityDetail(@PathVariable Long postNum) {
        return communityService.getCommunityDetail(postNum);
    }

    @PatchMapping("/edit/{postNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editCommunity
            (@RequestHeader("Authorization") String accessToken,
             @RequestBody @Validated CommunityRegisterDto communityRegisterDto,
             @PathVariable Long postNum) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        CommunityListDto dto = communityService.getUserId(postNum);
        if(userId.equals(dto.getUserId())) {
            return ResponseEntity.ok(new ApiResponse<>(communityService.editCommunity(communityRegisterDto, postNum)));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PatchMapping("/delete/{postNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> deleteCommunity (@PathVariable Long postNum) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.deleteCommunity(postNum)));
    }

}
