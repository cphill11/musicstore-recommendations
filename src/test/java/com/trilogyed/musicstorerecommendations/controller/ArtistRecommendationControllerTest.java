package com.trilogyed.musicstorerecommendations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorerecommendations.model.ArtistRecommendation;
import com.trilogyed.musicstorerecommendations.repository.ArtistRecommendationRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
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
@WebMvcTest(ArtistRecommendationController.class)
public class ArtistRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistRecommendationRepository repo;

    @Autowired
    private ObjectMapper mapper;

    private ArtistRecommendation musicStoreArtistRecommendation;

    private String musicStoreJson;

    private List<ArtistRecommendation> allArtistRecommendations = new ArrayList<>();
    private String allArtistRecommendationsJson;

    // from Project 1
    @Before
    public void setup() throws Exception {
        // input
        musicStoreArtistRecommendation = new ArtistRecommendation();
        musicStoreArtistRecommendation.setArtistId(1);
        musicStoreArtistRecommendation.setUserId(1);
        musicStoreArtistRecommendation.setLiked(new Boolean("False"));

        musicStoreJson = mapper.writeValueAsString(musicStoreArtistRecommendation);

        // output
        ArtistRecommendation artistRecommendation = new ArtistRecommendation();
        artistRecommendation.setId(1L);
        artistRecommendation.setArtistId(1);
        artistRecommendation.setUserId(1);
        artistRecommendation.setLiked(new Boolean("False"));

        allArtistRecommendations.add(artistRecommendation);
        allArtistRecommendationsJson = mapper.writeValueAsString(allArtistRecommendations);
    }

    // from work done with RSVP-Service
    // Mock create method
    @Test
    public void shouldCreateNewArtistRecommendationOnPostRequest() throws Exception {
        ArtistRecommendation inputArtistRecommendation  = new ArtistRecommendation();
        inputArtistRecommendation.setId(1L);
        inputArtistRecommendation.setArtistId(1);
        inputArtistRecommendation.setUserId(1);
        inputArtistRecommendation.setLiked(new Boolean("False"));

        String inputJson = mapper.writeValueAsString(inputArtistRecommendation);
        doReturn(musicStoreArtistRecommendation).when(repo).save(inputArtistRecommendation);
        mockMvc.perform(
                        post("/artistRecommendation")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(musicStoreJson));

    }

    // Mock get by ID method
    @Test
    public void shouldReturnArtistRecommendationById() throws Exception {
        doReturn(Optional.of(musicStoreArtistRecommendation)).when(repo).findById(1L);

        ResultActions result = mockMvc.perform(
                        get("/artistRecommendation/1"))
                .andExpect(status().isOk())
                .andExpect((content().json(musicStoreJson))
                );
    }

    // Mock get all method
    @Test
    public void shouldReturnAllArtistRecommendations() throws Exception {
        doReturn(allArtistRecommendations).when(repo).findAll();

        mockMvc.perform(
                        get("/artistRecommendation"))
                .andExpect(status().isOk())
                .andExpect(content().json(allArtistRecommendationsJson)
                );
    }

    // mock update method
    @Test
    public void shouldUpdateByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(
                        put("/artistRecommendation")
                                .content(musicStoreJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    // mock delete method
    @Test
    public void shouldDeleteByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(delete("/artistRecommendation/1")).andExpect(status().isNoContent());
    }
}