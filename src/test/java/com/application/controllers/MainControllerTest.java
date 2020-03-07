package com.application.controllers;

import com.application.model.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MainControllerTest {

    @Autowired
    private MainController mainController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createModel() throws Exception {
        final Model model = new Model("model 41b");
        final ResultActions resultActions = this.mockMvc.perform(post("/main-controller/model")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    log.info("service response: {}", mvcResult.getResponse().getContentAsString());
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                });
        final Model createdModel = this.deserializeResponse(resultActions.andReturn());
        final Model foundModel = this.findModel(createdModel.getId());
        assertThat(createdModel).isEqualTo(foundModel);
    }

    @Test
    public void notFindModel_httpCodeShouldBe404() throws Exception {
        final UUID uuid = UUID.fromString("bce05b12-e4fe-4a92-98c9-f619b0be562b");
        this.executeFindByIdApi(uuid)
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> {
                    log.info("api return: {}",  mvcResult.getResponse().getContentAsString());
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()));
    }

    private Model findModel(final UUID id) throws Exception {
        Objects.requireNonNull(id, "id cannot be null");
        final ResultActions resultActions = this.executeFindByIdApi(id)
                .andExpect(status().isOk());
        final Model model = this.deserializeResponse(resultActions.andReturn());
        assertThat(model).isNotNull();
        assertThat(model.getId()).isNotNull();
        assertThat(model.getName()).isNotBlank();
        return model;
    }

    private ResultActions executeFindByIdApi(UUID id) throws Exception {
        return this.mockMvc.perform(get("/main-controller/model/" + id));
    }

    private Model deserializeResponse(final MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        final Model model = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Model.class);
        return model;
    }
}
