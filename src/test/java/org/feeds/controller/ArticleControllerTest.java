package org.feeds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.feeds.dto.ArticleRequestDTO;
import org.feeds.service.ArticleServiceImpl;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private ArticleServiceImpl articleService;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void Should_ReadArticles_When_SingleArticle() throws Exception {
        ArticleRequestDTO article = ArticleRequestDTO.builder().build();
        when(articleService.readAllArticles()).thenReturn(List.of(article));

        MvcResult result = mockMvc.perform(get("/article/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(List.of(article));

        then(articleService).should().readAllArticles();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void Should_ReadArticleContent_When_SingleArticle() throws Exception {
        String articleLink
                = "https://www.helpnetsecurity.com/2024/05/22/authelia-open-source-authentication-authorization-server/";
        String expectedResponse = "Article Content";
        when(articleService.readArticleContent(articleLink)).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(get("/article/content")
                        .param("link", articleLink)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();

        then(articleService).should().readArticleContent(articleLink);
        assertEquals(expectedResponse, actualResponse);
    }
}
