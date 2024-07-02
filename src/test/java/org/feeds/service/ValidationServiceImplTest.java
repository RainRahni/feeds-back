package org.feeds.service;

import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.model.Feed;
import org.feeds.repository.ArticleRepository;
import org.feeds.repository.FeedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ValidationServiceImplTest {
    @Mock
    private FeedRepository feedRepository;
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ValidationServiceImpl validationService;
    private final String feedLink = "https://flipboard.com/@raimoseero/feed-nii8kd0sz.rss";
    private final Long feedId = 1L;
    @Test
    void Should_ValidateRequestingFeed_When_CorrectInput() {
        FeedCreationDTO feedCreationDTO = new FeedCreationDTO(feedLink);

        validationService.validateRequestingFeed(feedCreationDTO);

        verify(feedRepository, times(1)).existsByLink(feedLink);
    }

    @Test
    void Should_ThrowException_When_EmptyLink() {
        String link = "";
        FeedCreationDTO feedCreationDTO = new FeedCreationDTO(link);

        assertThrows(ValidationException.class,
                () -> validationService.validateRequestingFeed(feedCreationDTO));
    }

    @Test
    void Should_ThrowException_When_DuplicateLink() {
        FeedCreationDTO feedCreationDTO = new FeedCreationDTO(feedLink);
        when(feedRepository.existsByLink(feedLink)).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> validationService.validateRequestingFeed(feedCreationDTO));
    }

    @Test
    void Should_ThrowException_When_NotRessLink() {
        String link = "https://www.google.com/";
        FeedCreationDTO feedCreationDTO = new FeedCreationDTO(link);

        assertThrows(ValidationException.class,
                () -> validationService.validateRequestingFeed(feedCreationDTO));
    }

    @Test
    void Should_ValidateUpdatingFeed_When_CorrectInput() {
        Feed feed = Feed.builder()
                .id(feedId)
                .title("Tester")
                .link(feedLink)
                .build();

        FeedUpdateDTO feedUpdateDTO = FeedUpdateDTO.builder()
                .title("Tester Update")
                .build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        validationService.validateUpdatingFeed(feedUpdateDTO, feedId);

        verify(feedRepository, times(1)).findById(feedId);
    }

    @Test
    void Should_ThrowException_When_FeedNull() {
        FeedUpdateDTO feedUpdateDTO = FeedUpdateDTO.builder()
                .title("Tester Update")
                .build();

        assertThrows(ValidationException.class,
                () -> validationService.validateUpdatingFeed(feedUpdateDTO, feedId));
    }

    @Test
    void Should_ThrowException_When_FeedEqual() {
        Feed feed = Feed.builder()
                .id(feedId)
                .title("Tester Update")
                .link(feedLink)
                .build();

        FeedUpdateDTO feedUpdateDTO = FeedUpdateDTO.builder()
                .title("Tester Update")
                .link(feedLink)
                .build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        assertThrows(ValidationException.class,
                () -> validationService.validateUpdatingFeed(feedUpdateDTO, feedId));
    }

    @Test
    void Should_ValidateReadingArticleContent_When_CorrectInput() {
        String articleLink
                = "https://www.helpnetsecurity.com/2024/05/22/authelia-open-source-authentication-authorization-server/";

        when(articleRepository.existsByLink(articleLink)).thenReturn(true);

        validationService.validateReadingArticleContent(articleLink);

        verify(articleRepository, times(1)).existsByLink(articleLink);
    }
}
