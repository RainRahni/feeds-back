package org.feeds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedRequestDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.service.FeedServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
class FeedControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private FeedServiceImpl feedService;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void Should_CreateFeed_When_CorrectInput() throws Exception {
        FeedCreationDTO dto = FeedCreationDTO.builder()
                .link("RandomLink")
                .build();

        doNothing().when(feedService).requestFeed(dto);

        mockMvc.perform(post("/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        then(feedService).should().requestFeed(dto);
    }

    @Test
    void Should_ReturnFeeds_When_SingleFeed() throws Exception {
        FeedRequestDTO feedRequestDTO = FeedRequestDTO.builder()
                        .title("Title")
                        .hexColor("#FFFFFF")
                        .link("RandomLink")
                        .id(1L)
                        .build();
        when(feedService.readAllFeeds()).thenReturn(List.of(feedRequestDTO));

        MvcResult result = mockMvc.perform(get("/feed")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(List.of(feedRequestDTO));

        then(feedService).should().readAllFeeds();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void Should_UpdateFeed_When_CorrectInput() throws Exception {
        Long feedId = 1L;
        FeedUpdateDTO dto = FeedUpdateDTO.builder()
                .title("Title")
                .link("RandomLink")
                .build();

        doNothing().when(feedService).updateFeed(dto, feedId);

        mockMvc.perform(put("/feed/" + feedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        then(feedService).should().updateFeed(dto, feedId);
    }

    @Test
    void Should_DeleteFeed_When_CorrectInput() throws Exception {
        Long feedId = 1L;

        doNothing().when(feedService).deleteFeed(feedId);

        mockMvc.perform(delete("/feed/" + feedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        then(feedService).should().deleteFeed(feedId);
    }
}
