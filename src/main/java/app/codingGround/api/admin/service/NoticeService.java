package app.codingGround.api.admin.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.admin.dto.NoticeListDto;
import app.codingGround.api.admin.dto.NoticeRegisterDto;
import app.codingGround.api.entity.Notice;
import app.codingGround.api.admin.repository.NoticeRepository;
import app.codingGround.api.entity.User;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
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
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public DefaultResultDto postNotice(String accessToken, NoticeRegisterDto noticeRegisterDto) {

        // Client에게 받은 Dto를 Service 에서 Entity로 변환해서 DB에 저장

        Notice notice = new Notice();
        notice.setNoticeTitle(noticeRegisterDto.getNoticeTitle());
        notice.setNoticeContent(noticeRegisterDto.getNoticeContent());

        String userId = JwtTokenProvider.getUserId(accessToken);
        Optional<User> user = accountRepository.findByUserId(userId);
        notice.setUser(user.get());

        noticeRepository.save(notice);

        return DefaultResultDto.builder().success(true).message("성공").build();
    }

    public List<NoticeListDto> getNoticeList() {
        List<Notice> notices = noticeRepository.findAll();
        List<NoticeListDto> noticeList = new ArrayList<>();

        for (Notice notice : notices) {
            NoticeListDto noticeListDto = new NoticeListDto(notice);
            noticeList.add(noticeListDto);
        }

        log.info("notice List : " + noticeList);
        return noticeList;
    }

    public Optional<Notice> getNoticeDetail(Long noticeNum) {
        Optional<Notice> notice = noticeRepository.findByNoticeNum(noticeNum);

        return notice;
    }
}
