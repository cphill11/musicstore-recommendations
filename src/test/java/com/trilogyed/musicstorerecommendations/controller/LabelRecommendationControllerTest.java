package com.trilogyed.musicstorerecommendations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorerecommendations.model.ArtistRecommendation;
import com.trilogyed.musicstorerecommendations.model.LabelRecommendation;
import com.trilogyed.musicstorerecommendations.repository.LabelRecommendationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LabelRecommendationController.class)
public class LabelRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelRecommendationRepository repo;

    @Autowired
    private ObjectMapper mapper;

    private LabelRecommendation musicStoreLabelRecommendation;

    private String musicStoreJson;

    private List<LabelRecommendation> allLabelRecommendations = new ArrayList<>();
    private String allLabelRecommendationsJson;
    // from Project 1
    @Before
    public void setup() throws Exception {
        // input
        musicStoreLabelRecommendation = new LabelRecommendation();
        musicStoreLabelRecommendation.setLabelId(1);
        musicStoreLabelRecommendation.setUserId(1);
        musicStoreLabelRecommendation.setLiked(new Boolean("False"));

        musicStoreJson = mapper.writeValueAsString(musicStoreLabelRecommendation);

        // output
        LabelRecommendation labelRecommendation = new LabelRecommendation();
        labelRecommendation.setId(1L);
        labelRecommendation.setLabelId(1);
        labelRecommendation.setUserId(1);
        labelRecommendation.setLiked(new Boolean("False"));

        allLabelRecommendations.add(labelRecommendation);
        allLabelRecommendationsJson = mapper.writeValueAsString(allLabelRecommendations);
    }

    // from work done with RSVP-Service
    // Mock create method
    @Test
    public void shouldCreateNewLabelRecommendationOnPostRequest() throws Exception {
        LabelRecommendation inputLabelRecommendation  = new LabelRecommendation();
        inputLabelRecommendation.setId(1L);
        inputLabelRecommendation.setLabelId(1);
        inputLabelRecommendation.setUserId(1);
        inputLabelRecommendation.setLiked(new Boolean("False"));

        String inputJson = mapper.writeValueAsString(inputLabelRecommendation);
        doReturn(musicStoreLabelRecommendation).when(repo).save(inputLabelRecommendation);
        mockMvc.perform(
                        post("/labelRecommendation")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(musicStoreJson));

    }

    // Mock get by ID method
    @Test
    public void shouldReturnLabelRecommendationById() throws Exception {
        doReturn(Optional.of(musicStoreLabelRecommendation)).when(repo).findById(1L);

        ResultActions result = mockMvc.perform(
                        get("/labelRecommendation/1"))
                .andExpect(status().isOk())
                .andExpect((content().json(musicStoreJson))
                );
    }

    // Mock get all method
    @Test
    public void shouldReturnAllLabelRecommendations() throws Exception {
        doReturn(allLabelRecommendations).when(repo).findAll();

        mockMvc.perform(
                        get("/labelRecommendation"))
                .andExpect(status().isOk())
                .andExpect(content().json(allLabelRecommendationsJson)
                );
    }

    // mock update method
    @Test
    public void shouldUpdateByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(
                        put("/labelRecommendation")
                                .content(musicStoreJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    // mock delete method
    @Test
    public void shouldDeleteByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(delete("/labelRecommendation/1")).andExpect(status().isNoContent());
    }
}