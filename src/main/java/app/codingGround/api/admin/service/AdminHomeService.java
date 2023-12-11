package app.codingGround.api.admin.service;

import app.codingGround.api.admin.dto.response.AdminCommunityListDto;
import app.codingGround.api.admin.dto.response.AdminHomeDataDto;
import app.codingGround.api.admin.dto.response.AdminInquiryDto;
import app.codingGround.api.admin.mapper.AdminHomeMapper;
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

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminHomeService {

    private final AdminHomeMapper adminHomeMapper;
    public int  getNoAnswerCount() {

        return adminHomeMapper.getNoAnswerCount();
    }
    public int  getSignUpCount() {

        return adminHomeMapper.getSignUpCount();
    }
    public int  getRankGameCount() {

        return adminHomeMapper.getRankGameCount();
    }
    public int  getMatchingGameCount() {

        return adminHomeMapper.getMatchingGameCount();
    }
    public List<AdminInquiryDto> getAdminInquiry(){

        return adminHomeMapper.getAdminInquiry();
    }

}
