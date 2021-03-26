package ru.hse.youtube.parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "parent_id")
    private String parentId;
    
    @Column(name = "video_id")
    private String videoId;

    @Column(name = "published_at")
    private Timestamp publishedAt;

    @Column(name = "text_display")
    private String textDisplay;

    @Column(name = "text_original")
    private String textOriginal;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "author_channel_url")
    private String authorChannelUrl;

    @Column(name = "author_display_name")
    private String authorDisplayName;

    @Column(name = "author_profile_image_url")
    private String authorProfileImageUrl;

}
