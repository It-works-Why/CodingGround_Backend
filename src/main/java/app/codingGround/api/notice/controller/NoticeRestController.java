package app.codingGround.api.notice.controller;

import app.codingGround.api.entity.Notice;
import app.codingGround.api.notice.dto.NoticeListDto;
import app.codingGround.api.notice.service.NoticeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "공지사항 관련 API")
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeRestController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public Page<Notice> getNoticeList(
            @PageableDefault(size = 10, sort = "noticeNum", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Notice> noticeList = noticeService.getNoticeList(pageable);
        return noticeList;
    }

    @GetMapping("/detail/{noticeNum}")
    public NoticeListDto getNoticeDetail(@PathVariable Long noticeNum) {
        return noticeService.getNoticeDetail(noticeNum);
    }
}
