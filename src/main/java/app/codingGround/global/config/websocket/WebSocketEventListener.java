package app.codingGround.global.config.websocket;

import app.codingGround.api.battle.dto.response.GameUserDto;
import app.codingGround.api.battle.service.BattleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

/**
 * Created by rajeevkumarsingh on 25/07/17.
 */
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final BattleService battleService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        Message<byte[]> message = event.getMessage();
//
//        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
//        GenericMessage generic = (GenericMessage) accessor.getHeader("simpConnectMessage");
//        Map nativeHeaders = (Map) generic.getHeaders().get("nativeHeaders");
//        String gameId = (String) ((List) nativeHeaders.get("gameId")).get(0);
//        long totalUser = battleService.getUserCount(gameId);
//        if(totalUser == 2){
//            messagingTemplate.convertAndSend("/topic/public/gameStart/"+gameId, true);
//        }
        logger.info("Received a new web socket connection");
    }


    // 유저상태가 WAITING이면 유저를 삭제, PLAYING이면 DISCONNECT로 변경
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        if(userId != null) {
            logger.info("User Disconnected : " + userId);
            System.out.println(userId);
            System.out.println("hererererere");
            battleService.escapeGame(userId);
            messagingTemplate.convertAndSend("/topic/public/disconnect", "");
            String gameId = battleService.getGameId(userId);
            List<GameUserDto> gamePlayers = battleService.getGameUserDtoList(gameId);
            messagingTemplate.convertAndSend("/topic/public/refresh/user/" + gameId, gamePlayers);
        }

    }
}
