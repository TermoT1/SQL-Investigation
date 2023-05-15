package ru.sqlinvestigation.RestAPI.dto.userDB;
import java.sql.Timestamp;

public class UserStatsDTO {
    private long id;
    private Timestamp game_end_date;
    private int checks_answer;
    private Integer scores;
    private boolean is_completed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getGame_end_date() {
        return game_end_date;
    }

    public void setGame_end_date(Timestamp game_end_date) {
        this.game_end_date = game_end_date;
    }

    public int getChecks_answer() {
        return checks_answer;
    }

    public void setChecks_answer(int checks_answer) {
        this.checks_answer = checks_answer;
    }

    public Integer getScores() {
        return scores;
    }

    public void setScores(Integer scores) {
        this.scores = scores;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }
}
