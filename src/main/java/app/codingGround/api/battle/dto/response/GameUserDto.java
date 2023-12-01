package app.codingGround.api.battle.dto.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@ToString
public class GameUserDto {
    private String userId;
    private String userNickname;
    private String profileImg;
    private String userGameResult;


    public String getGameUser(String userGameResult) {
        Gson gson = new Gson();
        return gson.toJson(new GameUserDto(this.userId, this.userNickname, this.profileImg, userGameResult));
    }

    public String getGameUser() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static List<GameUserDto> parseToGameUserList(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GameUserDto>>() {}.getType();
        return gson.fromJson(jsonString, listType);
    }


}
