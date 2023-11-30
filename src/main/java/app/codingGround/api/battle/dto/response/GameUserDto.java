package app.codingGround.api.battle.dto.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class GameUserDto {
    private String userId;
    private String userNickname;
    private String profileImg;
    private String userGameResult;


    public String getGameUser(String userGameResult) {
        // 1, 2, 3, 4, 5
        String returnUser = "{userGameResult : '" + userGameResult + "', userProfileImgUrl : '"+ this.profileImg +"', userNickname : '"+ this.userNickname +"', userId= '"+ this.userId +"'}";
        return returnUser;
}

    public String getGameUser() {
        String returnUser = "{userGameResult : '" + this.userGameResult + "', userProfileImgUrl : '"+ this.profileImg +"', userNickname : '"+ this.userNickname +"', userId= '"+ this.userId +"'}";
        return returnUser;
    }

    public static List<GameUserDto> parseToGameUserList(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GameUserDto>>() {}.getType();
        return gson.fromJson(jsonString, listType);
    }


}
