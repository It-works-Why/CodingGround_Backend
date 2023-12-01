package app.codingGround.api.battle.controller;


import app.codingGround.api.battle.dto.response.GameUserDto;
import app.codingGround.api.battle.dto.response.PlayUserInfo;
import app.codingGround.api.battle.service.BattleService;
import app.codingGround.global.config.exception.CustomException;
import app.codingGround.global.config.exception.ErrorCode;
import app.codingGround.global.config.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final SimpMessageSendingOperations messagingTemplate;
    @MessageMapping("/join/queue/{gameId}")
    public void sendMessage(@DestinationVariable String gameId, @Payload String userId, SimpMessageHeaderAccessor headerAccessor) {
        PlayUserInfo playUserInfo = new PlayUserInfo();
        if(userId != null){
            headerAccessor.getSessionAttributes().put("userId", userId);
        }
        String failed = battleService.authJoinUser(gameId, userId);
        List<GameUserDto> gameUserDtoList = battleService.getGameUserDtoList(gameId);
        long userCount = gameUserDtoList.size();
        playUserInfo.setPlayUsers(gameUserDtoList);
        playUserInfo.setUserTotalCount(userCount);
        if(failed.equals("failed")){
            messagingTemplate.convertAndSend("/topic/public/getGameUsersData/" + failed + "/" + gameId + "/" + userId, playUserInfo);
        }else{
            messagingTemplate.convertAndSend("/topic/public/getGameUsersData/" + failed + "/" + gameId, playUserInfo);

        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage) {
        // Add username in web socket session



        return chatMessage;
    }





}
