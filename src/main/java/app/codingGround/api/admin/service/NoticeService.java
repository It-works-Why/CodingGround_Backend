package app.codingGround.api.admin.service;

import app.codingGround.api.account.controller.AccountRestController;
import app.codingGround.api.account.entitiy.User;
import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.admin.dto.NoticeDto;
import app.codingGround.api.admin.entity.Notice;
import app.codingGround.api.admin.repository.NoticeRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public DefaultResultDto postNotice(String accessToken, NoticeDto noticeDto) {

        // Client에게 받은 Dto를 Service 에서 Entity로 변환해서 DB에 저장

        Notice notice = new Notice();
        notice.setNoticeTitle(noticeDto.getNoticeTitle());
        notice.setNoticeContent(noticeDto.getNoticeContent());

        String userId = JwtTokenProvider.getUserId(accessToken);
        Optional<User> user = accountRepository.findByUserId(userId);
//        String userNum = user.getUserNum;
//
//        user = re
//        notice.setUser(userNum);

        noticeRepository.save(notice);

        return DefaultResultDto.builder().success(true).message("성공").build();
    }

}
