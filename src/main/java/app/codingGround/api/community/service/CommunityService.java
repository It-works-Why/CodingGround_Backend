package app.codingGround.api.community.service;

import app.codingGround.api.community.dto.CommunityListDto;
import app.codingGround.api.community.dto.CommunityRegisterDto;
import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.community.repository.CommunityRepositroy;
import app.codingGround.api.entity.Community;
import app.codingGround.api.entity.User;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<Community> getCommunityList(Pageable pageable) {
        return communityRepositroy.findAllByUseStatus(pageable, 1);
    }

    public CommunityListDto getCommunityDetail(Long postNum) {
        Community community = communityRepositroy.findByPostNumAndUseStatus(postNum, 1);
        CommunityListDto communityListDto = new CommunityListDto(community);

        return communityListDto;
    }

    @Transactional
    public DefaultResultDto editCommunity(CommunityRegisterDto communityRegisterDto, Long postNum) {
        Community community = null;
        try {
            community = communityRepositroy.findByPostNumAndUseStatus(postNum, 1);
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
        Community community = null;
        try {
            community = communityRepositroy.findByPostNumAndUseStatus(postNum,1);
            community.setUseStatus(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("오류", ErrorCode.TEST_ERROR);
        }

        communityRepositroy.save(community);

        return DefaultResultDto.builder().success(true).message("게시물이 삭제 되었습니다.").build();
    }

    public CommunityListDto getUserId(Long postNum) {
        Community community = communityRepositroy.findByPostNum(postNum);
        CommunityListDto communityListDto = new CommunityListDto(community);
        System.out.println(community.getUser().getUserId());


        return communityListDto;
    }
}
