package com.trilogyed.musicstorerecommendations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorerecommendations.model.ArtistRecommendation;
import com.trilogyed.musicstorerecommendations.model.TrackRecommendation;
import com.trilogyed.musicstorerecommendations.repository.TrackRecommendationRepository;
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
@WebMvcTest(TrackRecommendationController.class)
public class TrackRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackRecommendationRepository repo;

    @Autowired
    private ObjectMapper mapper;


    private TrackRecommendation musicStoreTrackRecommendation;

    private String musicStoreJson;

    private List<TrackRecommendation> allTrackRecommendations = new ArrayList<>();
    private String allTrackRecommendationsJson;


    // from Project 1
    @Before
    public void setup() throws Exception {
        // input
        musicStoreTrackRecommendation = new TrackRecommendation();
        musicStoreTrackRecommendation.setTrackId(1);
        musicStoreTrackRecommendation.setUserId(1);
        musicStoreTrackRecommendation.setLiked(new Boolean("False"));

        musicStoreJson = mapper.writeValueAsString(musicStoreTrackRecommendation);

        // output
        TrackRecommendation trackRecommendation = new TrackRecommendation();
        trackRecommendation.setId(1L);
        trackRecommendation.setTrackId(1);
        trackRecommendation.setUserId(1);
        trackRecommendation.setLiked(new Boolean("False"));

        allTrackRecommendations.add(trackRecommendation);
        allTrackRecommendationsJson = mapper.writeValueAsString(allTrackRecommendations);
    }

    // from work done with RSVP-Service
    // Mock create method
    @Test
    public void shouldCreateNewTrackRecommendationOnPostRequest() throws Exception {
        TrackRecommendation inputTrackRecommendation  = new TrackRecommendation();
        inputTrackRecommendation.setId(1L);
        inputTrackRecommendation.setTrackId(1);
        inputTrackRecommendation.setUserId(1);
        inputTrackRecommendation.setLiked(new Boolean("False"));

        String inputJson = mapper.writeValueAsString(inputTrackRecommendation);
        doReturn(musicStoreTrackRecommendation).when(repo).save(inputTrackRecommendation);
        mockMvc.perform(
                        post("/trackRecommendation")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(musicStoreJson));

    }

    // Mock get by ID method
    @Test
    public void shouldReturnTrackRecommendationById() throws Exception {
        doReturn(Optional.of(musicStoreTrackRecommendation)).when(repo).findById(1L);

        ResultActions result = mockMvc.perform(
                        get("/trackRecommendation/1"))
                .andExpect(status().isOk())
                .andExpect((content().json(musicStoreJson))
                );
    }

    // Mock get all method
    @Test
    public void shouldReturnAllTrackRecommendations() throws Exception {
        doReturn(allTrackRecommendations).when(repo).findAll();

        mockMvc.perform(
                        get("/trackRecommendation"))
                .andExpect(status().isOk())
                .andExpect(content().json(allTrackRecommendationsJson)
                );
    }

    // mock update method
    @Test
    public void shouldUpdateByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(
                        put("/trackRecommendation")
                                .content(musicStoreJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    // mock delete method
    @Test
    public void shouldDeleteByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(delete("/trackRecommendation/1")).andExpect(status().isNoContent());
    }
}