package app.codingGround.api.admin.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.admin.dto.request.AdminNoticeRegisterDto;
import app.codingGround.api.admin.dto.response.AdminNoticeListDto;
import app.codingGround.api.admin.repository.AdminCommunityRepository;
import app.codingGround.api.admin.repository.AdminNoticeRepository;
import app.codingGround.api.entity.Community;
import app.codingGround.api.entity.Notice;
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
public class AdminCommunityService {


    private final AdminCommunityRepository admincommunityRepository;
    public Page<Community> getcommunityList(Pageable pageable) {
        return admincommunityRepository.findAllByUseStatus(pageable, 1);
    }
}
