package app.codingGround.api.notice.service;

import app.codingGround.api.entity.Notice;
import app.codingGround.api.notice.dto.NoticeListDto;
import app.codingGround.api.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;


    public Page<Notice> getNoticeList(Pageable pageable) {
        return noticeRepository.findAllByUseStatus(pageable, 1);
    }

    public NoticeListDto getNoticeDetail(Long noticeNum) {
        Notice notice = noticeRepository.findByNoticeNumAndUseStatus(noticeNum, 1);
        NoticeListDto noticeListDto = new NoticeListDto(notice);

        return noticeListDto;
    }

}
