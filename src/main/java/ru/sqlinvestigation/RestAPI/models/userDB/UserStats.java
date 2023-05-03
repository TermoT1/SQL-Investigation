package ru.sqlinvestigation.RestAPI.models.userDB;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_stats_by_stories")
public class UserStats {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "story_id")
    private long story_id;
    @Column(name = "user_id")
    private long user_id;
    @Column(name = "game_end_date")
    private Timestamp game_end_date;
    @Column(name = "checks_answer")
    private int checks_answer;
    @Column(name = "scores")
    private Integer scores;

    public UserStats() {
    }

    public UserStats(long story_id, long user_id, Timestamp game_end_date, int checks_answer, int scores) {
        this.story_id = story_id;
        this.user_id = user_id;
        this.game_end_date = game_end_date;
        this.checks_answer = checks_answer;
        this.scores = scores;
    }

    public UserStats(long story_id, long user_id, int checks_answer) {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStory_id() {
        return story_id;
    }

    public void setStory_id(long story_id) {
        this.story_id = story_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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
}
