package ru.sqlinvestigation.RestAPI.models.userDB;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stories")
public class Story {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "description")
    private String description;

    @Column(name = "story_text")
    private String story_text;

    @Column(name = "answer")
    private String answer;


    public Story() {
    }

    public Story(Long id, String title, String difficulty, String description, String story_text) {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStory_text() {
        return story_text;
    }

    public void setStory_text(String story_text) {
        this.story_text = story_text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
