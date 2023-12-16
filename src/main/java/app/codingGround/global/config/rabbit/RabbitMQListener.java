package app.codingGround.global.config.rabbit;

import app.codingGround.api.battle.dto.request.Message;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @RabbitListener(queues = "game.queue") // 수신할 큐의 이름
    public void receiveMessage(Message message) {
            System.out.println("hererererere");
            System.out.println("여기들어왔어");
            System.out.println(message.getType());
            messagingTemplate.convertAndSend(message.getUrl(), message.getData());
    }
}
