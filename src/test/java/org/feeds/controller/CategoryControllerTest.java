package org.feeds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.feeds.service.CategoryServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.*;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private CategoryServiceImpl categoryService;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void Should_ReturnCategoryName_When_SingleCategory() throws Exception {
        String categoryName = "IT";
        when(categoryService.readCategoryNames()).thenReturn(List.of(categoryName));

        MvcResult result = mockMvc.perform(get("/category")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody  = result.getResponse().getContentAsString();

        then(categoryService).should().readCategoryNames();
        assertEquals(objectMapper.writeValueAsString(List.of(categoryName)),responseBody);
    }
}