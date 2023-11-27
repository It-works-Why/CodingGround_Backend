package app.codingGround.api.user.controller;

import app.codingGround.api.user.dto.response.RankListDto;
import app.codingGround.api.user.service.RankingService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "사용자 관련 API")
@RequestMapping("/api/user")
public class RankingListController {

    @Autowired
    RankingService rankingService;

    @GetMapping("/ranking/list")
    public List<RankListDto> getRankingList(){
        return rankingService.getRankingList();
    }

}
