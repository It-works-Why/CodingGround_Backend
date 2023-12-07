package app.codingGround.api.admin.service;

import app.codingGround.api.admin.dto.response.AdminCommunityListDto;
import app.codingGround.api.admin.repository.AdminCommunityRepository;
import app.codingGround.api.entity.Community;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminCommunityService {


    private final AdminCommunityRepository adminCommunityRepository;
    public Page<Community> getCommunityList(Pageable pageable) {
        return adminCommunityRepository.findAllByUseStatus(pageable, 1);
    }
    public Page<Community> getSearchCommunityList(Pageable pageable, String keyword, String type) {
        
        if (Objects.equals(type, "제목")) {
            return adminCommunityRepository.findAllByUseStatusAndPostTitleContaining(pageable, 1, keyword);
        } else if (Objects.equals(type, "작성자")) {
            return adminCommunityRepository.findAllByUseStatusAndUser_UserNicknameContaining(pageable, 1, keyword);
        } else if (Objects.equals(type, "내용")) {
            return adminCommunityRepository.findAllByUseStatusAndPostContentContaining(pageable, 1, keyword);
        }
        return null;
    }

    public AdminCommunityListDto getcommunityDetail(Long postNum) {
        Community community = adminCommunityRepository.findByPostNumAndUseStatus(postNum, 1);
        AdminCommunityListDto adminCommunityListDto = new AdminCommunityListDto(community);

        return adminCommunityListDto;
    }

    @Transactional
    public DefaultResultDto deleteCommunity(Long postNum) {
        Community community = null;
        try {
            community = adminCommunityRepository.findByPostNumAndUseStatus(postNum, 1);
            community.setUseStatus(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("오류", ErrorCode.TEST_ERROR);
        }

        adminCommunityRepository.save(community);

        return DefaultResultDto.builder().success(true).message("글이 삭제 되었습니다.").build();
    }


}
