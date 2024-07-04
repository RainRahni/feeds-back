package org.feeds.service;

import org.feeds.dto.FeedUpdateDTO;
import org.feeds.mapper.FeedMapper;
import org.feeds.model.Feed;
import org.feeds.repository.FeedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedServiceImplTest {
    @Mock
    private ValidationServiceImpl validationServiceImpl;
    @Mock
    private FeedMapper feedMapper;
    @Mock
    private ArticleServiceImpl articleServiceImpl;
    @Mock
    private FeedRepository feedRepository;
    @InjectMocks
    private FeedServiceImpl feedServiceImpl;
    private final String expectedLink = "https://flipboard.com/@raimoseero/feed-nii8kd0sz.rss";

    @Test
    void Should_CreateFeed_When_CorrectInput() {
        String expectedTitle = "RandomTitle";
        Feed feed = Feed.builder()
                .title(expectedTitle)
                .link(expectedLink)
                .build();

        doNothing().when(validationServiceImpl).validateCreatingFeed(feed);
        feedServiceImpl.createFeed(feed);

        verify(validationServiceImpl, times(1)).validateCreatingFeed(feed);
        verify(feedRepository, times(1)).findAll();
        verify(feedRepository, times(1)).save(feed);

        ArgumentCaptor<Feed> feedCaptor = ArgumentCaptor.forClass(Feed.class);
        verify(feedRepository).save(feedCaptor.capture());
        Feed savedFeed = feedCaptor.getValue();

        String actualTitle = savedFeed.getTitle();
        String actualLink = savedFeed.getLink();
        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedLink, actualLink);
    }

    @Test
    void Should_UpdateFeed_When_LinkSame() {
        Long feedId = 1L;
        Feed existingFeed = Feed.builder()
                .id(feedId)
                .link(expectedLink)
                .title("SomeTitle")
                .build();

        FeedUpdateDTO updatedFeed = FeedUpdateDTO.builder()
                .link(expectedLink)
                .title("NewTitle")
                .build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(existingFeed));
        doAnswer(invocation -> {
            FeedUpdateDTO dto = invocation.getArgument(0);
            Feed feed = invocation.getArgument(1);
            feed.setTitle(dto.title());
            return null;
        }).when(feedMapper).updateModel(any(FeedUpdateDTO.class), any(Feed.class));

        feedServiceImpl.updateFeed(updatedFeed, feedId);

        ArgumentCaptor<Feed> feedCaptor = ArgumentCaptor.forClass(Feed.class);

        verify(validationServiceImpl, times(1)).validateUpdatingFeed(updatedFeed, feedId);
        verify(feedRepository, times(1)).findById(feedId);
        verify(feedMapper, times(1)).updateModel(updatedFeed, existingFeed);
        verify(feedRepository, times(1)).save(feedCaptor.capture());

        Feed capturedFeed = feedCaptor.getValue();

        String actualTitle = capturedFeed.getTitle();
        String actualLink = capturedFeed.getLink();

        assertEquals("NewTitle", actualTitle);
        assertEquals(expectedLink, actualLink);
    }
}
