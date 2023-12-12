package app.codingGround.api.battle.service;

import app.codingGround.api.admin.repository.AdminQuestionRepository;
import app.codingGround.api.admin.repository.AdminTestCaseRepository;
import app.codingGround.api.battle.dto.request.CodeData;
import app.codingGround.api.battle.dto.request.ConnectGameInfo;
import app.codingGround.api.battle.dto.response.*;
import app.codingGround.api.battle.mapper.QuestionMapper;
import app.codingGround.api.battle.repository.*;
import app.codingGround.api.entity.*;
import app.codingGround.api.schedule.repository.SeasonRepository;
import app.codingGround.api.schedule.repository.UserSeasonRepository;
import app.codingGround.api.user.repository.UserRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.utils.Judge0Util;
import app.codingGround.global.utils.JwtTokenProvider;
import app.codingGround.global.utils.RedisUtil;
import io.lettuce.core.ScriptOutputType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BattleService {
    //jpa
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final GameRepository gameRepository;
    private final GameRuleRepository gameRuleRepository;
    private final AdminQuestionRepository adminQuestionRepository;
    private final RoundRepository roundRepository;
    private final RoundRecordRepository roundRecordRepository;
    private final AdminTestCaseRepository adminTestCaseRepository;
    private final QuestionMapper questionMapper;
    private final UserSeasonRepository userSeasonRepository;
    private final GameRecordRepository gameRecordRepository;
    private final RedisUtil redisUtil;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    private Jedis getJedisInstance() {
        Jedis jedis = new Jedis(getRedisHost(), getRedisPort());
        jedis.auth(getRedisPassword());
        return jedis;
    }

    private void closeJedisInstance(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }



//    userId { gameRoomId }, userId { gameRoomId }

    public List<Language> getLanguage() {
        try {
            return languageRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public QueueInfoDto tryGameConnect(ConnectGameInfo connectGameInfo, String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        User user = userRepository.findByUserId(userId);
        GameUserDto gameUserDto = GameUserDto.builder().userNickname(user.getUserNickname()).profileImg(user.getUserProfileImg()).userId(userId).userGameResult("DEFAULT").memory("500000").build();
        String isReconnect = redisUtil.findConnectedGame(user.getUserId());
        if (isReconnect != null) {
            return QueueInfoDto.builder().gameId(isReconnect).connectType("reConnect").build();
        }

        // 큐를 잡고있는사람이 게임방 입장 할려고 할때 연결 거부
        String isWaiting = redisUtil.findTryConnectSameUserId(userId);
        if (isWaiting != null) {
            return QueueInfoDto.builder().gameId(isWaiting).connectType("failed").build();
        }


        // 여기서부터는 재접속이 필요없는 유저
        String gameId = redisUtil.findGameRoom(connectGameInfo);
        if (gameId == null) {
            gameId = redisUtil.createRoom(connectGameInfo);
        }
        Jedis jedis = null;
        String lockKey = null;
        try {
            jedis = getJedisInstance();
            lockKey = "game_room_lock:" + gameId;
            int maxRetries = 7; // 최대 재시도 횟수
            int waitTimeMs = 3000; // 재시도 간격 (5초)

            String lockResult = null;
            int retries = 0;

            while (lockResult == null && retries < maxRetries) {
                lockResult = jedis.set(lockKey, "LOCKED", SetParams.setParams().nx().ex(10)); // 락의 만료 시간을 설정합니다. (예: 10초)
                if (lockResult == null) {
                    retries++;
                    try {
                        Thread.sleep(waitTimeMs); // 일정 시간 대기 후 재시도
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        // 스레드 인터럽트 발생 시 처리
                    }
                }
            }
            redisUtil.createUserKey(gameId, gameUserDto);
            redisUtil.joinGameRoom(gameId, gameUserDto);
        } finally {
            jedis.del(lockKey);
            closeJedisInstance(jedis);
        }
        return QueueInfoDto.builder().gameId(gameId).connectType("succeed").build();
    }

    public DefaultResultDto acceptReconnect(String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        redisUtil.acceptReconnect(userId);
        return DefaultResultDto.builder().success(true).message("재접속 성공").build();
    }

    public DefaultResultDto denyReconnect(String accessToken) {
        String userId = JwtTokenProvider.getUserId(accessToken);
        redisUtil.denyReconnect(userId);

        return DefaultResultDto.builder().success(true).message("재접속 거절").build();
    }

    public long getUserCount(String gameId) {
        return redisUtil.getUserCount(gameId);
    }


    public List<GameUserDto> getGameUserDtoList(String gameId) {
        return redisUtil.getGameUserDtoList(gameId);
    }

    public String authJoinUser(String gameId, String userId) {
        String isSuccess = "";
        if (redisUtil.authJoinUser(gameId, userId)) {
            isSuccess = "succeed";
        } else {
            isSuccess = "failed";
        }
        return isSuccess;
    }

    public void escapeGame(String userId) {
        PersonalGameDataDto personalGameDataDto = redisUtil.findGameKeyByUserId(userId);
        if (personalGameDataDto.getStatus().equals("PLAYING")) {
            redisUtil.disconnectGameFromIngame(userId);
        }
        if (personalGameDataDto.getStatus().equals("WAITING")) {
            redisUtil.allRemoveRelatedUserId(personalGameDataDto);
        }
    }

    public String getGameId(String userId) {
        return redisUtil.getGameId(userId);
    }

    public String getGameStatus(String gameId) {
        Jedis jedis = null;
        GameDto gameDto;
        String lockKey = null;
        try {
            jedis = getJedisInstance();
            lockKey = "game_room_lock:" + gameId;
            int maxRetries = 7; // 최대 재시도 횟수
            int waitTimeMs = 3000; // 재시도 간격 (5초)

            String lockResult = null;
            int retries = 0;

            while (lockResult == null && retries < maxRetries) {
                lockResult = jedis.set(lockKey, "LOCKED", SetParams.setParams().nx().ex(10)); // 락의 만료 시간을 설정합니다. (예: 10초)
                if (lockResult == null) {
                    retries++;
                    try {
                        Thread.sleep(waitTimeMs); // 일정 시간 대기 후 재시도
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        // 스레드 인터럽트 발생 시 처리
                    }
                }
            }
            gameDto = null;
            if (lockResult != null) {
                gameDto = redisUtil.findGameByGameId(gameId);
            }
        } finally {
            jedis.del(lockKey);
            closeJedisInstance(jedis);
        }
        return gameDto.getGameStatus();
    }

    @Transactional
    public void startGame(String gameId) {
        GameDto gameDto = redisUtil.findGameByGameId(gameId);
        Game game = new Game();
        Language language = languageRepository.findByLanguageCode(gameDto.getGameLanguage());
        Season season = seasonRepository.findFirstByOrderBySeasonNumDesc();

        game.setGameType(gameDto.getGameType());
        game.setLanguageNum(language);
        game.setSeasonNum(season);
        gameRepository.save(game);
        gameDto.setGameNum(game.getGameNum());
        gameDto.setGameStatus("PLAY");
        redisUtil.updateGameData(gameDto);
        List<GameUserDto> usersGameDtoList = redisUtil.getGameUserDtoResult(gameDto.getGameId());
        redisUtil.updateUserGameStatus(usersGameDtoList, "PLAYING", gameDto.getGameId());
    }

    @Transactional
    public List<Question> startRound(String gameId) {
        GameDto gameDto = redisUtil.findGameByGameId(gameId);
        Game game = gameRepository.findByGameNum(gameDto.getGameNum());
        GameRule gameRule = gameRuleRepository.findByRuleNum(1L);
        List<Question> questions = new ArrayList<>();
        Question question = adminQuestionRepository.findRandomRow();
        Round round = new Round();
        round.setGameNum(game);
        round.setGameRule(gameRule);
        round.setQuestionNum(question);
        round.setRound(1);
        roundRepository.save(round);
        GameRule gameRule2 = gameRuleRepository.findByRuleNum(2L);
        Question question2 = adminQuestionRepository.findRandomRow();
        while (question.getQuestionNum() == question2.getQuestionNum()) {
            question2 = adminQuestionRepository.findRandomRow();
        }
        Round round2 = new Round();
        round2.setGameNum(game);
        round2.setGameRule(gameRule2);
        round2.setQuestionNum(question2);
        round2.setRound(2);
        roundRepository.save(round2);
        gameDto.setFirstRoundEndTime(Timestamp.valueOf(LocalDateTime.now().plusMinutes(question.getQuestionLimitTime()).plusSeconds(5)));
        gameDto.setSecondRoundEndTime(Timestamp.valueOf(LocalDateTime.now().plusMinutes(question.getQuestionLimitTime() + question2.getQuestionLimitTime()).plusSeconds(5)));

        redisUtil.updateGameData(gameDto);

        questions.add(question);
        questions.add(question2);

        return questions;
    }

    // gameId 의 현재 라운드수, gameNum 을 redis에서 조회 후 RDS에서 조회후 리턴
    public QuestionDto getQuestion(String gameId) {
        GameDto game = redisUtil.findGameByGameId(gameId);
        QuestionDto questionDto = questionMapper.getQuestion(game);
        questionDto.setLanguageCode(game.getGameLanguage());
        if (game.getGameRound() == 1) {
            questionDto.setEndTime(game.getFirstRoundEndTime());
        }
        if (game.getGameRound() == 2) {
            questionDto.setEndTime(game.getSecondRoundEndTime());
        }
        questionDto.setRound(game.getGameRound());
        return questionDto;
    }

    public List<TestCaseDto> getTestcase(String gameId) {
        GameDto game = redisUtil.findGameByGameId(gameId);
        Round round = roundRepository.findByGameNumAndRound(gameRepository.findByGameNum(game.getGameNum()), game.getGameRound());
        Long questionNum = round.getQuestionNum().getQuestionNum();
        List<TestCase> testCase = adminTestCaseRepository.findAllByQuestion_QuestionNum(questionNum);
        testCase.remove(2);
        List<TestCaseDto> result = new ArrayList<>();
        for (TestCase dto : testCase) {
            TestCaseDto testCaseDto = new TestCaseDto();
            testCaseDto.setInput(dto.getTestCaseInput());
            testCaseDto.setOutput(dto.getTestCaseOutput());
            result.add(testCaseDto);
        }

        return result;
    }

    @Transactional
    public ResultDto runCode(CodeData codeData, String gameId) {

        GameDto game = redisUtil.findGameByGameId(gameId);
        User user = userRepository.findByUserId(codeData.getUserId());
        Round round = roundRepository.findByGameNumAndRound(gameRepository.findByGameNum(game.getGameNum()), game.getGameRound());
        int answerCorrect = 0;
        int count = 0;
        Long questionNum = round.getQuestionNum().getQuestionNum();
        List<TestCase> testCases = adminTestCaseRepository.findAllByQuestion_QuestionNum(questionNum);
        List<TestCaseDto> result = new ArrayList<>();
        for (TestCase dto : testCases) {
            TestCaseDto testCaseDto = new TestCaseDto();
            testCaseDto.setInput(dto.getTestCaseInput());
            testCaseDto.setOutput(dto.getTestCaseOutput());
            result.add(testCaseDto);
        }


        if (codeData.getType().equals("run")) {
            result.remove(2);
        }
        List<String> tokens = Judge0Util.runCode(codeData, result);
        List<TestCaseResultDto> testCaseResultDtos = null;
        ResultDto resultDto = new ResultDto();
        if (codeData.getType().equals("run")) {
            testCaseResultDtos = Judge0Util.readTokens(tokens, result);
        } else {
            testCaseResultDtos = Judge0Util.readTokens(tokens, result);
            RoundRecord roundRecord = roundRecordRepository.findByRoundNumAndUserNum(round, user);
            if (roundRecord == null) {
                roundRecord = new RoundRecord();
                roundRecord.setRoundNum(round);
                roundRecord.setUserNum(user);
            }

            if (testCaseResultDtos.get(0).getIsCorrect() && testCaseResultDtos.get(1).getIsCorrect() && testCaseResultDtos.get(2).getIsCorrect()) {
                answerCorrect = 1;
                if (game.getGameRound() == 1) {
                    List<GameUserDto> gameUserDtos = redisUtil.getGameUserDtoResult(gameId);
                    for (GameUserDto dto : gameUserDtos) {
                        if (dto.getUserGameResult().equals("5")) {
                            count++;
                        }
                    }
                    if (count < 5) {
                        redisUtil.updateUserStatus(gameId, codeData.getUserId(), "5");
                        count++;
                    }
                } else {
                    Jedis jedis = null;
                    jedis = getJedisInstance();
                    String lockKey = "game_user_lock:" + gameId; // gameId에 따른 락 키 생성
                    int maxRetries = 7; // 최대 재시도 횟수
                    int waitTimeMs = 3000; // 재시도 간격 (5초)

                    String lockResult = null;
                    int retries = 0;

                    while (lockResult == null && retries < maxRetries) {
                        lockResult = jedis.set(lockKey, "LOCKED", SetParams.setParams().nx().ex(10)); // 락의 만료 시간을 설정합니다. (예: 10초)
                        if (lockResult == null) {
                            retries++;
                            try {
                                Thread.sleep(waitTimeMs); // 일정 시간 대기 후 재시도
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                // 스레드 인터럽트 발생 시 처리
                            }
                        }
                    }
                    if (lockResult != null) {
                        // 락을 획득한 경우에만 로직 수행
                        try {
                            List<GameUserDto> gameUserDtos = redisUtil.getGameUserDtoList(gameId);
                            for (GameUserDto dto : gameUserDtos) {
                                if (dto.getUserId().equals(codeData.getUserId())) {
                                    dto.setMemory(testCaseResultDtos.get(2).getMemory());
                                }
                            }//500000
                            Collections.sort(gameUserDtos, Comparator.comparingInt(dto -> Integer.parseInt(dto.getMemory())));

                            for (GameUserDto gameUserDto : gameUserDtos) {
                                // 정렬된 순서의 인덱스를 이용하여 순위를 부여합니다.
                                if(!gameUserDto.getMemory().equals("500000")){
                                    int rank = gameUserDtos.indexOf(gameUserDto) + 1;
                                    redisUtil.updateUserStatus(gameId, gameUserDto.getUserId(), rank + "", gameUserDto.getMemory());
                                }
                            }
                        } finally {
                            jedis.del(lockKey); // 작업 완료 후 락을 해제합니다.
                            closeJedisInstance(jedis);
                        }
                    }
                }
            }
            if (testCaseResultDtos.get(testCaseResultDtos.size() - 1).getMemory() == null) {
                roundRecord.setRoundRecordMemory(null);
            } else {
                roundRecord.setRoundRecordMemory(Integer.valueOf(testCaseResultDtos.get(testCaseResultDtos.size() - 1).getMemory()));
            }
            roundRecord.setRoundRecordAnswer(codeData.getCode());
            roundRecord.setRoundRecordAnswerCorrect(answerCorrect);
            roundRecord.setRoundRecordRanking(null);
            roundRecord.setRoundRecordSubmitTime(Timestamp.valueOf(LocalDateTime.now()));
            roundRecord.setRoundRecordToken(testCaseResultDtos.get(testCaseResultDtos.size() - 1).getToken());
            roundRecordRepository.save(roundRecord);
        }
        if (game.getGameRound() == 1) {
            resultDto.setRound(1);
            if (count == 5) {
                resultDto.setIsRoundEnd(true);
            } else {
                resultDto.setIsRoundEnd(false);
            }
        }
        if (game.getGameRound() == 2) {
            resultDto.setRound(2);
            resultDto.setIsRoundEnd(false);
        }

        resultDto.setTestCaseResultDtos(testCaseResultDtos);
        return resultDto;
    }


    @Transactional
    public ResultDto runCode(CodeData codeData, String gameId, int roundCount) {

        GameDto game = redisUtil.findGameByGameId(gameId);
        User user = userRepository.findByUserId(codeData.getUserId());
        Round round = roundRepository.findByGameNumAndRound(gameRepository.findByGameNum(game.getGameNum()), roundCount);
        int answerCorrect = 0;
        int count = 0;
        Long questionNum = round.getQuestionNum().getQuestionNum();
        List<TestCase> testCases = adminTestCaseRepository.findAllByQuestion_QuestionNum(questionNum);
        List<TestCaseDto> result = new ArrayList<>();
        for (TestCase dto : testCases) {
            TestCaseDto testCaseDto = new TestCaseDto();
            testCaseDto.setInput(dto.getTestCaseInput());
            testCaseDto.setOutput(dto.getTestCaseOutput());
            result.add(testCaseDto);
        }


        if (codeData.getType().equals("run")) {
            result.remove(2);
        }
        List<String> tokens = Judge0Util.runCode(codeData, result);
        List<TestCaseResultDto> testCaseResultDtos = null;
        ResultDto resultDto = new ResultDto();
        if (codeData.getType().equals("run")) {
            testCaseResultDtos = Judge0Util.readTokens(tokens, result);
        } else {
            testCaseResultDtos = Judge0Util.readTokens(tokens, result);
            RoundRecord roundRecord = roundRecordRepository.findByRoundNumAndUserNum(round, user);
            if (roundRecord == null) {
                roundRecord = new RoundRecord();
                roundRecord.setRoundNum(round);
                roundRecord.setUserNum(user);
            }

            if (testCaseResultDtos.get(0).getIsCorrect() && testCaseResultDtos.get(1).getIsCorrect() && testCaseResultDtos.get(2).getIsCorrect()) {
                answerCorrect = 1;
                if (game.getGameRound() == 1) {
                    List<GameUserDto> gameUserDtos = redisUtil.getGameUserDtoResult(gameId);
                    for (GameUserDto dto : gameUserDtos) {
                        if (dto.getUserGameResult().equals("5")) {
                            count++;
                        }
                    }
                    if (count < 5) {
                        redisUtil.updateUserStatus(gameId, codeData.getUserId(), "5");
                        count++;
                    }
                } else {
//                        List<RoundRecord> roundRecords = roundRecordRepository.findAllByRoundNum(round);
                    List<GameUserDto> gameUserDtos = redisUtil.getGameUserDtoList(gameId);
                    for (GameUserDto dto : gameUserDtos) {
                        if (dto.getUserId().equals(codeData.getUserId())) {
                            dto.setMemory(testCaseResultDtos.get(2).getMemory());
                        }
                    }
                    Collections.sort(gameUserDtos, Comparator.comparingInt(dto -> Integer.parseInt(dto.getMemory())));

                    for (GameUserDto gameUserDto : gameUserDtos) {
                        // 정렬된 순서의 인덱스를 이용하여 순위를 부여합니다.
                        if(!gameUserDto.getMemory().equals("500000")){
                            int rank = gameUserDtos.indexOf(gameUserDto) + 1;
                            redisUtil.updateUserStatus(gameId, gameUserDto.getUserId(), rank + "", gameUserDto.getMemory());
                        }
                    }
                }
            }
            if (testCaseResultDtos.get(testCaseResultDtos.size() - 1).getMemory() == null) {
                roundRecord.setRoundRecordMemory(null);
            } else {
                roundRecord.setRoundRecordMemory(Integer.valueOf(testCaseResultDtos.get(testCaseResultDtos.size() - 1).getMemory()));
            }
            roundRecord.setRoundRecordAnswer(codeData.getCode());
            roundRecord.setRoundRecordAnswerCorrect(answerCorrect);
            roundRecord.setRoundRecordRanking(null);
            roundRecord.setRoundRecordSubmitTime(Timestamp.valueOf(LocalDateTime.now()));
            roundRecord.setRoundRecordToken(testCaseResultDtos.get(testCaseResultDtos.size() - 1).getToken());
            roundRecordRepository.save(roundRecord);
        }
        if (roundCount == 1) {
            resultDto.setRound(1);
            if (count == 5) {
                resultDto.setIsRoundEnd(true);
            } else {
                resultDto.setIsRoundEnd(false);
            }
        }
        if (roundCount == 2) {
            resultDto.setRound(2);
            resultDto.setIsRoundEnd(false);
        }

        resultDto.setTestCaseResultDtos(testCaseResultDtos);
        return resultDto;
    }


    public void endRound1(String gameId) {
        redisUtil.updateRound(gameId);
    }

    public Boolean getFailedUser(String gameId, String userId) {
        List<GameUserDto> gameUserDtoList = redisUtil.getGameUserDtoResult(gameId);
        String result = "";
        for (GameUserDto dto : gameUserDtoList) {
            if (dto.getUserId().equals(userId)) {
                result = dto.getUserGameResult();
                break;
            }
        }
        if (!result.equals("5")) {
            redisUtil.updateUserStatus(gameId, userId, "DEFEAT");
            redisUtil.denyReconnect(userId);
            return false;
        }
        return true;
    }


    // 마지막 관련 레디스 삭제
    public void endRound2(String gameId) {
        redisUtil.removeGame(gameId);
    }


    // 게임 레코드는 게임이 끝났을때 일관적으로 레디스 뜯어서 들어가게 만듦 for문돌려서
    @Transactional
    public void addGameRecord(String gameId) {
        List<GameUserDto> gameUserDtoList = redisUtil.getGameUserDtoResult(gameId);
        String userResult = "";
        for (GameUserDto dto : gameUserDtoList) {
            GameDto gameDto = redisUtil.findGameByGameId(gameId);
            Game game = gameRepository.findByGameNum(gameDto.getGameNum());
            User user = userRepository.findByUserId(dto.getUserId());
            UserSeason userSeason = userSeasonRepository.findBySeasonAndUser(game.getSeasonNum(), user);
            GameRecord gameRecord = new GameRecord();
            gameRecord.setLanguage(game.getLanguageNum());
            gameRecord.setGame(game);
            gameRecord.setUser(user);
            gameRecord.setSeason(game.getSeasonNum());
            int score = userSeason.getRankScore();
            System.out.println(dto.getUserGameResult());
            System.out.println("여기 결과값 나열중");
            if(gameDto.getGameType().equals("RANK")){
                switch (dto.getUserGameResult()) {
                    case "1":
                        gameRecord.setGameRecord(1);
                        gameRecord.setChangeScore(10);
                        userSeason.setRankScore(score + 10);
                        break;
                    case "2":
                        gameRecord.setGameRecord(2);
                        gameRecord.setChangeScore(8);
                        userSeason.setRankScore(score + 8);
                        break;
                    case "3":
                        gameRecord.setGameRecord(3);
                        gameRecord.setChangeScore(6);
                        userSeason.setRankScore(score + 6);
                        break;
                    case "4":
                        gameRecord.setGameRecord(4);
                        gameRecord.setChangeScore(4);
                        userSeason.setRankScore(score + 4);
                        break;
                    case "5":
                        gameRecord.setGameRecord(5);
                        gameRecord.setChangeScore(2);
                        userSeason.setRankScore(score + 2);
                        break;
                    case "defeat":
                        gameRecord.setGameRecord(6);
                        gameRecord.setChangeScore(-5);
                        userSeason.setRankScore(score - 5);
                        break;
                }
                userSeasonRepository.save(userSeason);
            }else{
                switch (dto.getUserGameResult()) {
                    case "1":
                        gameRecord.setGameRecord(1);
                        break;
                    case "2":
                        gameRecord.setGameRecord(2);
                        break;
                    case "3":
                        gameRecord.setGameRecord(3);
                        break;
                    case "4":
                        gameRecord.setGameRecord(4);
                        break;
                    case "5":
                        gameRecord.setGameRecord(5);
                        break;
                    case "defeat":
                        gameRecord.setGameRecord(6);
                        break;
                }
                gameRecord.setChangeScore(0);
            }
            gameRecordRepository.save(gameRecord);
        }
    }

    public String getMyRank(String gameId, String userId) {
        List<GameUserDto> gameUserDtoList = redisUtil.getGameUserDtoResult(gameId);
        String userResult = "";
        for (GameUserDto dto : gameUserDtoList) {
            if (dto.getUserId().equals(userId)) {
                userResult = dto.getUserGameResult();
                break;
            }
        }
        return userResult;
    }


    // 리스트에 존재 하지만 RDS에 결과를 안넣은 놈들 그냥 싹! 다 구속시켜!
    @Transactional
    public void addRound1Record(String gameId) {
        try {
            List<GameUserDto> gameUserDtoList = redisUtil.getGameUserDtoResult(gameId);
            GameDto gameDto = redisUtil.findGameByGameId(gameId);
            Game game = gameRepository.findByGameNum(gameDto.getGameNum());
            Round round = roundRepository.findByGameNumAndRound(game, 1);
            for (GameUserDto dto : gameUserDtoList) {
                User user = userRepository.findByUserId(dto.getUserId());
                RoundRecord roundRecord = roundRecordRepository.findByRoundNumAndUserNum(round, user);
                System.out.println(roundRecord == null);
                if (roundRecord == null) {
                    roundRecord = new RoundRecord();
                    roundRecord.setRoundRecordAnswer("EMPTY");
                    roundRecord.setRoundNum(round);
                    roundRecord.setRoundRecordSubmitTime(Timestamp.valueOf(LocalDateTime.now()));
                    roundRecord.setRoundRecordToken(null);
                    roundRecord.setUserNum(user);
                    roundRecord.setRoundRecordAnswerCorrect(0);
                    roundRecord.setRoundRecordRanking(null);
                    roundRecord.setRoundRecordMemory(null);
                    roundRecordRepository.save(roundRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void addRound2Record(String gameId) {
        try {
            List<GameUserDto> gameUserDtoList = redisUtil.getGameUserDtoResult(gameId);
            GameDto gameDto = redisUtil.findGameByGameId(gameId);
            Game game = gameRepository.findByGameNum(gameDto.getGameNum());
            Round round = roundRepository.findByGameNumAndRound(game, 2);
            for (GameUserDto dto : gameUserDtoList) {
                User user = userRepository.findByUserId(dto.getUserId());
                RoundRecord roundRecord = roundRecordRepository.findByRoundNumAndUserNum(round, user);
                System.out.println(roundRecord == null);
                if (roundRecord == null) {
                    roundRecord = new RoundRecord();
                    roundRecord.setRoundRecordAnswer("EMPTY");
                    roundRecord.setRoundNum(round);
                    roundRecord.setRoundRecordSubmitTime(Timestamp.valueOf(LocalDateTime.now()));
                    roundRecord.setRoundRecordToken(null);
                    roundRecord.setUserNum(user);
                    roundRecord.setRoundRecordAnswerCorrect(0);
                    roundRecord.setRoundRecordRanking(null);
                    roundRecord.setRoundRecordMemory(null);
                    roundRecordRepository.save(roundRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}



