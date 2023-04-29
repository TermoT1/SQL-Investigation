package ru.sqlinvestigation.RestAPI.models.userDB;

import javax.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(name = "user_id")
    private long user_id;

    @Column(name = "refresh_token")
    private String refresh_token;

    public RefreshToken() {
    }


    public RefreshToken(long user_id, String refresh_token) {
        this.user_id = user_id;
        this.refresh_token = refresh_token;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}