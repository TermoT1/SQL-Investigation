package ru.sqlinvestigation.RestAPI.models.userDB.JWT;


public class JwtResponseAccessToken {

    private final String type = "Bearer";
    private final String accessToken;


    public JwtResponseAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
