package app.codingGround.api.community.controller;

import app.codingGround.api.community.dto.CommunityListDto;
import app.codingGround.api.community.dto.CommunityRegisterDto;
import app.codingGround.api.community.service.CommunityService;
import app.codingGround.api.entity.Community;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityRestController {

    private final CommunityService communityService;

    @GetMapping("/community/list")
    public Page<Community> getCommunityList(
            @PageableDefault(size = 10, sort = "postNum", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Community> communityList = communityService.getCommunityList(pageable);
        return communityList;
    }

    @PostMapping("/community/write")
    public ResponseEntity<ApiResponse<DefaultResultDto>> postCommunity
            (@RequestHeader("Authorization") String accessToken,
             @RequestBody @Validated CommunityRegisterDto communityRegisterDto) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.postCommunity(accessToken, communityRegisterDto)));
    }

    @GetMapping("/community/detail/{postNum}")
    public CommunityListDto getCommunityDetail(@PathVariable Long postNum) {
        return communityService.getCommunityDetail(postNum);
    }

    @PatchMapping("/community/edit/{postNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> editCommunity
            (@RequestBody @Validated CommunityRegisterDto communityRegisterDto,
             @PathVariable Long postNum) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.editCommunity(communityRegisterDto, postNum)));
    }

    @DeleteMapping("/community/delete/{postNum}")
    public ResponseEntity<ApiResponse<DefaultResultDto>> deleteCommunity (@PathVariable Long postNum) {
        return ResponseEntity.ok(new ApiResponse<>(communityService.deleteCommunity(postNum)));
    }

}
