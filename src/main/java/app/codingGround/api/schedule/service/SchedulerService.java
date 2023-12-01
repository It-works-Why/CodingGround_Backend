package app.codingGround.api.schedule.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.entity.Rank;
import app.codingGround.api.entity.Season;
import app.codingGround.api.entity.User;
import app.codingGround.api.entity.UserSeason;
import app.codingGround.api.schedule.dto.InsertSeasonDto;
import app.codingGround.api.schedule.repository.SeasonRepository;
import app.codingGround.api.schedule.repository.UserSeasonRepository;
import app.codingGround.api.user.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final AccountRepository accountRepository;
    private final SeasonRepository seasonRepository;
    private final UserSeasonRepository userSeasonRepository;
    private final RankingRepository rankingRepository;

    public Long createSeason(InsertSeasonDto insertSeasonDto) {

        Season season = new Season();

        season.setSeasonName(insertSeasonDto.getSeasonName());
        season.setSeasonStartTime(insertSeasonDto.getSeasonStartTime());
        season.setSeasonEndTime(insertSeasonDto.getSeasonEndTime());

        seasonRepository.save(season);

        return season.getSeasonNum();
    }

    public int createUserSeason(Long seasonNum) {

        /*
            유저 시즌 번호 USER_SEASON_NUM : AUTO_INCREMENT
            유저 번호 USER_NUM : USER 를 불러올 때 USER_STATUS 가 'ACTIVE' 상태인 User 만 List<>로 담아서 저장
            랭크 번호 RANK_NUM : 가장 낮은 기본 등급 5
            시즌 번호 SEASON_NUM : 위에서 만든 시즌 번호
            랭크 점수 RANK_SCORE : 0
         */

        List<User> userList = accountRepository.findAllByUserStatus("ACTIVE");
        Season season = seasonRepository.findBySeasonNum(seasonNum);
        Rank rank = rankingRepository.findByRankNum(5L);

        for (User user : userList) {

            UserSeason userSeason = new UserSeason();

            userSeason.setUser(user);
            userSeason.setRank(rank);
            userSeason.setSeason(season);
            userSeason.setRankScore(0);

            userSeasonRepository.save(userSeason);

        }

        return 1;
    }
}
