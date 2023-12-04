package app.codingGround.api.user.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.contact.dto.response.ContactListDto;
import app.codingGround.api.contact.dto.response.InquiryDetailDto;
import app.codingGround.api.contact.dto.response.UserInquiryRegisterDto;
import app.codingGround.api.entity.Contact;
import app.codingGround.api.entity.User;
import app.codingGround.api.user.dto.response.*;
import app.codingGround.api.user.mapper.UserMapper;
import app.codingGround.api.user.repository.InquiryRepository;
import app.codingGround.api.user.repository.UserRepository;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final AccountRepository accountRepository;
    private final InquiryRepository inquiryRepository;

    private final UserMapper userMapper;
    public InfoDto getUserInfo(String userId) {
        return userMapper.getUserInfo(userId);
    }
    public List<RankingDto> getUserRankings(String userId) {
        return userMapper.getUserRankings(userId);
    }
    public List<GameBadgeDto> getUserBadge(String userId) {
        return userMapper.getUserBadge(userId);
    }
    public List<GameLanguageDto> getUserGameLanguage(String userId) {
        return userMapper.UserGameLanguage(userId);
    }
    public List<GameInfoDto> getUserGameInfo(String userId){
        List<GameInfoDto> gameInfoList = userMapper.getUserGameInfo(userId);

        for (GameInfoDto gameInfoDto : gameInfoList) {
            String userNicknamesString = gameInfoDto.getUserNicknames();
            String userProfileImgString = gameInfoDto.getUserProfileImgs();
            List<String> userProfileImgList = Arrays.asList(userProfileImgString.split(","));
            List<String> userNicknamesList = Arrays.asList(userNicknamesString.split(","));
            gameInfoDto.setUserNicknamesList(userNicknamesList);
            gameInfoDto.setUserProfileImgList(userProfileImgList);
        }
        return  gameInfoList;
    }

    public List<GameInfoDto> getGameRecordInfo(Long gamenum){
        List<GameInfoDto> gameInfoList = userMapper.getGameRecordInfo(gamenum);

        for (GameInfoDto gameInfoDto : gameInfoList) {
            String userNicknamesString = gameInfoDto.getUserNicknames();
            String userProfileImgString = gameInfoDto.getUserProfileImgs();
            List<String> userProfileImgList = Arrays.asList(userProfileImgString.split(","));
            List<String> userNicknamesList = Arrays.asList(userNicknamesString.split(","));
            gameInfoDto.setUserNicknamesList(userNicknamesList);
            gameInfoDto.setUserProfileImgList(userProfileImgList);
        }
        return  gameInfoList;
    }
    public List<GameRecordRoundOneDto>getGameRecordRoundOne(String userId , Long gamenum){
        return userMapper.getGameRecordRoundOne(userId, gamenum);
    }
    public List<GameRecordRoundTwoDto>getGameRecordRoundTwo(String userId , Long gamenum){
        return userMapper.getGameRecordRoundTwo(userId, gamenum);
    }

    public List<ContactListDto> getContactList(String userId , int postNum){
        return userMapper.getContactList(userId, postNum);
    }
    public PageNumDto getPageNum(String userId){
        return userMapper.getPageNum(userId);
    }


    public InquiryDetailDto getmyinquirydetail(Long contactNum){
        InquiryDetailDto inquiryDetailDto = userMapper.getmyinquirydetail(contactNum);
        if (inquiryDetailDto.getUseStatus() == 0) {
            throw new CustomException("이미 삭제된 게시물 입니다.", ErrorCode.NOT_USE_POST);
        }
        System.out.println(inquiryDetailDto);

        return inquiryDetailDto;
    }

    public DefaultResultDto postinquiry(String accessToken , UserInquiryRegisterDto userInquiryRegisterDto){

        Contact contact = new Contact();
        contact.setContactTitle(userInquiryRegisterDto.getInquiryTitle());
        contact.setContactContent(userInquiryRegisterDto.getInquiryContent());

        String userId = JwtTokenProvider.getUserId(accessToken);
        Optional<User> user = accountRepository.findByUserId(userId);
        contact.setUser(user.get());

        inquiryRepository.save(contact);

        return DefaultResultDto.builder().success(true).message("글이 등록되었습니다.").build();
    }


}