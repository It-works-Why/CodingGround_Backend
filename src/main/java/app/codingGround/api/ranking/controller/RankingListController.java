package app.codingGround.api.ranking.controller;

import app.codingGround.api.ranking.dto.response.RankingPageDto;
import app.codingGround.api.ranking.service.RankingService;
import app.codingGround.global.config.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ranking")
public class RankingListController {

    @Autowired
    RankingService rankingService;
    @GetMapping("/list/{pageNum}")
    public ResponseEntity<ApiResponse<RankingPageDto>> getRankingList(
            @RequestParam(name = "season", required = false) String season,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PathVariable int pageNum){
        RankingPageDto rankingPage = new RankingPageDto();
        System.out.println(pageNum);
        int rankingPageNum = pageNum * 10;
        if (season == null || season.equals("undefined")){
            rankingPage.setRankListDto(rankingService.getRankingList(rankingPageNum));
            rankingPage.setSeasonListDto(rankingService.getSeasonList());
            rankingPage.setTotalPageNum(rankingService.getTotalPage());
            rankingPage.setPageNum(pageNum);
        } else if (season != null && keyword.equals("")) {
            rankingPage.setRankListDto(rankingService.getSeasonRankingList(season,rankingPageNum));
            rankingPage.setSeasonListDto(rankingService.getSeasonList());
            rankingPage.setTotalPageNum(rankingService.getSeasonTotalPage());
            rankingPage.setPageNum(pageNum);
        }else {
            rankingPage.setRankListDto(rankingService.getKeywordRankingList(season, keyword, rankingPageNum));
            rankingPage.setSeasonListDto(rankingService.getSeasonList());
            rankingPage.setTotalPageNum(rankingService.getKeywordTotalPage());
            rankingPage.setPageNum(pageNum);
        }


        return ResponseEntity.ok(new ApiResponse<>(rankingPage));
    }

 /*   @GetMapping("/list/{pageNum}")
    public ResponseEntity<ApiResponse<RankingPageDto>> getRankingList(@PathVariable int pageNum){
        RankingPageDto rankingPage = new RankingPageDto();
        rankingPage.setRankListDto(rankingService.getRankingList());
        rankingPage.setSeasonListDto(rankingService.getSeasonList());
        rankingPage.setTotalPageNum(rankingService.getTotalPage());
        rankingPage.setPageNum(pageNum);
        return ResponseEntity.ok(new ApiResponse<>(rankingPage));
    }

    @GetMapping("/list/{season}/{pageNum}")
    public ResponseEntity<ApiResponse<RankingPageDto>> getSeasonRankingList(@PathVariable String season){
        RankingPageDto rankingPage = new RankingPageDto();
        rankingPage.setRankListDto(rankingService.getSeasonRankingList(season));
        rankingPage.setSeasonListDto(rankingService.getSeasonList());

        return ResponseEntity.ok(new ApiResponse<>(rankingPage));
    }

    @GetMapping("/list/{keyword}/{season}/{pageNum}")
    public ResponseEntity<ApiResponse<RankingPageDto>> getkeywordRankingList(@PathVariable String keyword, @PathVariable String season){
        RankingPageDto rankingPage = new RankingPageDto();
        rankingPage.setRankListDto(rankingService.getKeywordRankingList(season, keyword));
        rankingPage.setSeasonListDto(rankingService.getSeasonList());

        return ResponseEntity.ok(new ApiResponse<>(rankingPage));
    }*/


}
