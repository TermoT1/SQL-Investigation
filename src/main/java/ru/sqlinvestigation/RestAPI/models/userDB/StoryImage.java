package ru.sqlinvestigation.RestAPI.models.userDB;

import javax.persistence.*;

@Entity
@Table(name = "stories_images")
public class StoryImage {
    @Id
    @Column(name = "id_stories")
    private long id_stories;

    @Column(name = "content_type")
    private String content_type;
    @Column(name = "size")
    private Long size;

    @Basic(fetch = FetchType.LAZY)
    private byte[] bytes;

    public StoryImage() {
    }

    public long getId_stories() {
        return id_stories;
    }

    public void setId_stories(long id_stories) {
        this.id_stories = id_stories;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
