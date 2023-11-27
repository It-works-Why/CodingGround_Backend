package app.codingGround.api.admin.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.admin.dto.NoticeListDto;
import app.codingGround.api.admin.dto.NoticeRegisterDto;
import app.codingGround.api.entity.Notice;
import app.codingGround.api.admin.repository.NoticeRepository;
import app.codingGround.api.entity.User;
import app.codingGround.domain.common.dto.Pagination;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.domain.common.dto.response.PageResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

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

        return DefaultResultDto.builder().success(true).message("글이 등록되었습니다.").build();
    }

    public List<NoticeListDto> getNoticeList() {
        List<Notice> notices = noticeRepository.findAll();
        List<NoticeListDto> noticeList = new ArrayList<>();

        for (Notice notice : notices) {
            NoticeListDto noticeListDto = new NoticeListDto(notice);
            noticeList.add(noticeListDto);
        }

        return noticeList;
    }

//    public PageResultDto<Object> getNoticeList(Pageable pageable) {
//        Page<Notice> notices = noticeRepository.findAll(pageable);
//        List<NoticeListDto> noticeList = new ArrayList<>();
//
//        for (Notice notice : notices) {
//            NoticeListDto noticeListDto = new NoticeListDto(notice);
//            noticeList.add(noticeListDto);
//        }
//
//        Pagination pagination = new Pagination(
//                pageable.getPageNumber() +1,
//                (int) notices.getTotalElements()
//        );
//
//        return PageResultDto.builder().data(noticeList).pagination(pagination).build();
//    }


    public NoticeListDto getNoticeDetail(Long noticeNum) {
        Notice notice = noticeRepository.findByNoticeNum(noticeNum);
        NoticeListDto noticeListDto = new NoticeListDto(notice);

        return noticeListDto;
    }

    @Transactional
    public DefaultResultDto editNotice(NoticeRegisterDto noticeRegisterDto, Long noticeNum) {
        Notice notice = null;
        try {
            notice = noticeRepository.findByNoticeNum(noticeNum);
            notice.setNoticeTitle(noticeRegisterDto.getNoticeTitle());
            notice.setNoticeContent(noticeRegisterDto.getNoticeContent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("오류", ErrorCode.TEST_ERROR);
        }

        noticeRepository.save(notice);

        return DefaultResultDto.builder().success(true).message("글이 수정되었습니다.").build();
    }

    @Transactional
    public DefaultResultDto deleteNotice(Long noticeNum) {
        Notice notice = noticeRepository.findByNoticeNum(noticeNum);
        noticeRepository.delete(notice);

        return DefaultResultDto.builder().success(true).message("글이 삭제 되었습니다.").build();
    }
}
