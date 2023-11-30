package app.codingGround.api.admin.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.admin.dto.response.AdminNoticeListDto;
import app.codingGround.api.admin.dto.request.AdminNoticeRegisterDto;
import app.codingGround.api.entity.Notice;
import app.codingGround.api.admin.repository.AdminNoticeRepository;
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
public class AdminNoticeService {

    private final AdminNoticeRepository adminNoticeRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public DefaultResultDto postNotice(String accessToken, AdminNoticeRegisterDto adminNoticeRegisterDto) {

        // Client에게 받은 Dto를 Service 에서 Entity로 변환해서 DB에 저장

        Notice notice = new Notice();
        notice.setNoticeTitle(adminNoticeRegisterDto.getNoticeTitle());
        notice.setNoticeContent(adminNoticeRegisterDto.getNoticeContent());

        String userId = JwtTokenProvider.getUserId(accessToken);
        Optional<User> user = accountRepository.findByUserId(userId);
        notice.setUser(user.get());

        adminNoticeRepository.save(notice);

        return DefaultResultDto.builder().success(true).message("글이 등록되었습니다.").build();
    }

    public Page<Notice> getNoticeList(Pageable pageable) {
        return adminNoticeRepository.findAllByUseStatus(pageable, 1);
    }

    public AdminNoticeListDto getNoticeDetail(Long noticeNum) {
        Notice notice = adminNoticeRepository.findByNoticeNumAndUseStatus(noticeNum, 1);
        AdminNoticeListDto adminNoticeListDto = new AdminNoticeListDto(notice);

        return adminNoticeListDto;
    }

    @Transactional
    public DefaultResultDto editNotice(AdminNoticeRegisterDto adminNoticeRegisterDto, Long noticeNum) {
        Notice notice = null;
        try {
            notice = adminNoticeRepository.findByNoticeNumAndUseStatus(noticeNum, 1);
            notice.setNoticeTitle(adminNoticeRegisterDto.getNoticeTitle());
            notice.setNoticeContent(adminNoticeRegisterDto.getNoticeContent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("오류", ErrorCode.TEST_ERROR);
        }

        adminNoticeRepository.save(notice);

        return DefaultResultDto.builder().success(true).message("글이 수정되었습니다.").build();
    }

    @Transactional
    public DefaultResultDto deleteNotice(Long noticeNum) {
        Notice notice = null;
        try {
            notice = adminNoticeRepository.findByNoticeNumAndUseStatus(noticeNum, 1);
            notice.setUseStatus(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("오류", ErrorCode.TEST_ERROR);
        }

        adminNoticeRepository.save(notice);

        return DefaultResultDto.builder().success(true).message("글이 삭제 되었습니다.").build();
    }
}
