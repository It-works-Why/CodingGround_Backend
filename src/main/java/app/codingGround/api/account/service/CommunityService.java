package app.codingGround.api.account.service;

import app.codingGround.api.account.dto.CommunityListDto;
import app.codingGround.api.account.dto.CommunityRegisterDto;
import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.account.repository.CommunityRepositroy;
import app.codingGround.api.entity.Community;
import app.codingGround.api.entity.User;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommunityService {
    private final CommunityRepositroy communityRepositroy;
    private final AccountRepository accountRepository;

    @Transactional
    public DefaultResultDto postCommunity(String accessToken, CommunityRegisterDto communityRegisterDto) {
        Community community = new Community();
        community.setPostTitle(communityRegisterDto.getPostTitle());
        community.setPostContent(communityRegisterDto.getPostContent());

        String userId = JwtTokenProvider.getUserId(accessToken);
        Optional<User> user = accountRepository.findByUserId(userId);
        community.setUser(user.get());

        communityRepositroy.save(community);

        return DefaultResultDto.builder().success(true).message("글이 등록되었습니다.").build();
    }

    public List<CommunityListDto> getCommunityList() {
        List<Community> communities = communityRepositroy.findAll();
        List<CommunityListDto> communityList = new ArrayList<>();

        for (Community community : communities) {
            CommunityListDto communityListDto = new CommunityListDto(community);
            communityList.add(communityListDto);
        }

        return communityList;
    }

    public CommunityListDto getCommunityDetail(Long postNum) {
        Community community = communityRepositroy.findByPostNum(postNum);
        CommunityListDto communityListDto = new CommunityListDto(community);

        return communityListDto;
    }

    @Transactional
    public DefaultResultDto editCommunity(CommunityRegisterDto communityRegisterDto, Long postNum) {
        Community community = null;
        try {
            community = communityRepositroy.findByPostNum(postNum);
            community.setPostTitle(communityRegisterDto.getPostTitle());
            community.setPostContent(communityRegisterDto.getPostContent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("오류", ErrorCode.TEST_ERROR);
        }

        communityRepositroy.save(community);

        return DefaultResultDto.builder().success(true).message("게시물이 수정되었습니다.").build();
    }

    @Transactional
    public DefaultResultDto deleteCommunity(Long postNum) {
        Community community = communityRepositroy.findByPostNum(postNum);
        communityRepositroy.delete(community);

        return DefaultResultDto.builder().success(true).message("게시물이 삭제 되었습니다.").build();
    }
}
