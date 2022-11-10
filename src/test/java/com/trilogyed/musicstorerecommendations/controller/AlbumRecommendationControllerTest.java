package com.trilogyed.musicstorerecommendations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorerecommendations.model.AlbumRecommendation;
import com.trilogyed.musicstorerecommendations.repository.AlbumRecommendationRepository;
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
@WebMvcTest(AlbumRecommendationController.class)
public class AlbumRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumRecommendationRepository repo;

    @Autowired
    private ObjectMapper mapper;

    private AlbumRecommendation musicStoreAlbumRecommendation;

    private String musicStoreJson;

    private List<AlbumRecommendation> allAlbumRecommendations = new ArrayList<>();
    private String allAlbumRecommendationsJson;


    // from Project 1
    @Before
    public void setup() throws Exception {
        // input
        musicStoreAlbumRecommendation = new AlbumRecommendation();
        musicStoreAlbumRecommendation.setAlbumId(1);
        musicStoreAlbumRecommendation.setUserId(1);
        musicStoreAlbumRecommendation.setLiked(new Boolean("False"));

        musicStoreJson = mapper.writeValueAsString(musicStoreAlbumRecommendation);

        // output
        AlbumRecommendation albumRecommendation = new AlbumRecommendation();
        albumRecommendation.setId(1L);
        albumRecommendation.setAlbumId(1);
        albumRecommendation.setUserId(1);
        albumRecommendation.setLiked(new Boolean("False"));

        allAlbumRecommendations.add(albumRecommendation);
        allAlbumRecommendationsJson = mapper.writeValueAsString(allAlbumRecommendations);
    }

    // from work done with RSVP-Service
    // Mock create method
    @Test
    public void shouldCreateNewAlbumRecommendationOnPostRequest() throws Exception {
        AlbumRecommendation inputAlbumRecommendation  = new AlbumRecommendation();
        inputAlbumRecommendation.setId(1L);
        inputAlbumRecommendation.setAlbumId(1);
        inputAlbumRecommendation.setUserId(1);
        inputAlbumRecommendation.setLiked(new Boolean("False"));

        String inputJson = mapper.writeValueAsString(inputAlbumRecommendation);
        doReturn(musicStoreAlbumRecommendation).when(repo).save(inputAlbumRecommendation);
        mockMvc.perform(
                        post("/albumRecommendation")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(musicStoreJson));

    }

    // Mock get by ID method
    @Test
    public void shouldReturnAlbumRecommendationById() throws Exception {
        doReturn(Optional.of(musicStoreAlbumRecommendation)).when(repo).findById(1L);

        ResultActions result = mockMvc.perform(
                        get("/albumRecommendation/1"))
                .andExpect(status().isOk())
                .andExpect((content().json(musicStoreJson))
                );
    }

    // Mock get all method
    @Test
    public void shouldReturnAllAlbumRecommendations() throws Exception {
        doReturn(allAlbumRecommendations).when(repo).findAll();

        mockMvc.perform(
                        get("/albumRecommendation"))
                .andExpect(status().isOk())
                .andExpect(content().json(allAlbumRecommendationsJson)
                );
    }

    // mock update method
    @Test
    public void shouldUpdateByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(
                        put("/albumRecommendation")
                                .content(musicStoreJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    // mock delete method
    @Test
    public void shouldDeleteByIdAndReturn204StatusCode() throws Exception {
        mockMvc.perform(delete("/albumRecommendation/1")).andExpect(status().isNoContent());
    }

}