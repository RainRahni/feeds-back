package org.feeds.service;

import jakarta.persistence.Id;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.mapper.FeedMapper;
import org.feeds.mapper.FeedMapperImpl;
import org.feeds.model.Category;
import org.feeds.model.Feed;
import org.feeds.repository.FeedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedServiceImplTest {
    @Mock
    private ValidationServiceImpl validationServiceImpl;
    @Mock
    private FeedMapper feedMapper;
    @Mock
    private FeedRepository feedRepository;
    @InjectMocks
    private FeedServiceImpl feedServiceImpl;

    @Test
    void Should_CreateFeed_When_CorrectInput() {
        Feed feed = Feed.builder()
                .title("RandomTitle")
                .link("RandomLink")
                .build();

        doNothing().when(validationServiceImpl).validateCreatingFeed(feed);
        feedServiceImpl.createFeed(feed);

        verify(validationServiceImpl, times(1)).validateCreatingFeed(feed);
        verify(feedRepository, times(1)).findAll();
        verify(feedRepository, times(1)).save(feed);

        ArgumentCaptor<Feed> feedCaptor = ArgumentCaptor.forClass(Feed.class);
        verify(feedRepository).save(feedCaptor.capture());
        Feed savedFeed = feedCaptor.getValue();
        assertEquals("RandomTitle", savedFeed.getTitle());
        assertEquals("RandomLink", savedFeed.getLink());
    }

    @Test
    void Should_UpdateFeed_When_LinkSame() {
        Long feedId = 1L;
        Feed existingFeed = Feed.builder()
                .id(feedId)
                .link("SameLink")
                .title("SomeTitle")
                .build();

        FeedUpdateDTO updatedFeed = FeedUpdateDTO.builder()
                .link("SameLink")
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
        assertEquals("NewTitle", capturedFeed.getTitle());
        assertEquals("SameLink", capturedFeed.getLink());
    }

    /*@Test
    void Should_UpdateFeed_When_LinkDifferent() {
        Long feedId = 1L;
        Feed existingFeed = Feed.builder()
                .id(feedId)
                .link("SameLink")
                .title("SomeTitle")
                .build();

        FeedUpdateDTO updatedFeed = FeedUpdateDTO.builder()
                .link("New link")
                .title("NewTitle")
                .build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(existingFeed));

        feedServiceImpl.updateFeed(updatedFeed, feedId);

        verify(validationServiceImpl, times(1)).validateUpdatingFeed(updatedFeed, feedId);
        verify(feedRepository, times(1)).findById(feedId);
    }*/
}