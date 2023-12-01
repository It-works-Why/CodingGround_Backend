package app.codingGround.api.battle.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PlayUserInfo {
    public PlayUserInfo() {
    }

    private long userTotalCount;
    private List<GameUserDto> playUsers;
}
