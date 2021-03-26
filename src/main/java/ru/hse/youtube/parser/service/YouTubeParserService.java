package ru.hse.youtube.parser.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hse.youtube.parser.model.Comment;
import ru.hse.youtube.parser.repository.CommentRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Slf4j
@Service
public class YouTubeParserService {

    @Autowired
    private CommentRepository commentRepository;

    private static final String DEVELOPER_KEY = "AIzaSyC0z069CGBhsJZwerXSTd34YgJ8zzvjW8Q";
    private static final String APPLICATION_NAME = "java-youtube-parser";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void saveComments(String videoUrl) throws IOException, GeneralSecurityException {

        String videoId = videoUrl.split("v=|&")[1];

        YouTube youtubeService = getService();

        YouTube.CommentThreads.List request = youtubeService.commentThreads()
                .list("snippet,replies");

        CommentThreadListResponse response = request
                .setKey(DEVELOPER_KEY)
                .setVideoId(videoId)
                .execute();

        Set<Comment> comments = new HashSet<>();

        for (var comment : response.getItems()) {
            comments.add(mapToLocalComment(comment.getSnippet().getTopLevelComment()));

            if (comment.getReplies() != null) {
                for (var repComment : comment.getReplies().getComments()) {
                    comments.add(mapToLocalComment(repComment));
                }
            }
        }

        commentRepository.saveAll(comments);
    }

    public Comment mapToLocalComment(com.google.api.services.youtube.model.Comment commentFromYouTube) {
        CommentSnippet snippet = commentFromYouTube.getSnippet();

        return Comment.builder()
                .id(commentFromYouTube.getId())
                .parentId(snippet.getParentId())
                .videoId(snippet.getVideoId())
                .publishedAt(new Timestamp(snippet.getPublishedAt().getValue()))
                .textDisplay(snippet.getTextDisplay())
                .textOriginal(snippet.getTextOriginal())
                .updatedAt(new Timestamp(snippet.getUpdatedAt().getValue()))
                .likeCount(snippet.getLikeCount())
                .authorChannelUrl(snippet.getAuthorChannelUrl())
                .authorDisplayName(snippet.getAuthorDisplayName())
                .authorProfileImageUrl(snippet.getAuthorProfileImageUrl())
                .build();
    }

    public long getTotalComments() {
        return commentRepository.count();
    }

    public Comment getRandomComment() {
        long total = getTotalComments();

        if (total == 0) {
            return null;
        }

        Random random = new Random();
        int idx = random.nextInt((int) total - 1) + 1;

        Page<Comment> page = commentRepository.findAll(PageRequest.of(idx, 1));
        return page.getContent().get(0);
    }
}
