package app.codingGround.api.battle.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Message<T> {

    private String type;

    private String gameId;

    private String url;

    private T data;

    public Message(T data) {
        this.data = data;
    }
}
