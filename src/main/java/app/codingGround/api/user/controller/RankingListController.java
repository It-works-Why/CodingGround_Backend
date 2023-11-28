package app.codingGround.api.user.controller;

import app.codingGround.api.user.dto.response.RankListDto;
import app.codingGround.api.user.service.RankingService;
import app.codingGround.global.config.model.ApiResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ranking")
public class RankingListController {

    @Autowired
    RankingService rankingService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<RankListDto>>> getRankingList(){

        return ResponseEntity.ok(new ApiResponse<>( rankingService.getRankingList()));
    }

}
