package app.codingGround.global.config.security;


// 로그인이 필요없는 기능들
public enum Endpoint {
    LOGIN("/api/account/login"),
    REGISTER("/api/account/register"),
    SUCCESS_TEST("/api/test/success/test"),
    FAIL_TEST("/api/test/fail/test"),
    BATTLE("/ws/**"),
    RANKING_LIST("/api/ranking/list"),
    GAME_ACCESS_TOKEN("/api/get/accessToken");

    private final String url;

    Endpoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
