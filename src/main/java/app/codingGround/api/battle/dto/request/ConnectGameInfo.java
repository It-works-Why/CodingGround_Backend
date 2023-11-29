package app.codingGround.api.battle.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ConnectGameInfo {
    @NotBlank
    private String gameType;

    @NotBlank
    private String gameLanguage;

}
