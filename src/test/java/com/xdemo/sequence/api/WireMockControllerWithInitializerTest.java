package com.xdemo.sequence.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * WireMockServer вместе с тестовыми application.properties конфигурируется в отдельном классе WireMockInitializer.
 * WireMockServer помещается в ConfigurableApplicationContext (см. WireMockInitializer).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
@AutoConfigureMockMvc
class WireMockControllerWithInitializerTest {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MockMvc mockMvc;


    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void get() throws Exception {
        JSONObject body = new JSONObject("{\"fact\": 1,\"length\": 1}");
        wireMockServer.stubFor(
                WireMock.get("/fact")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(body.toString()))
        );

        //Тестовый вебклиент, отправляющий запросы в @RestController
        this.webTestClient
                .get()
                .uri("/api/wiremock")
                .exchange()
                .expectStatus().isOk()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.fact")
                .isEqualTo("1")
                .jsonPath("$.length")
                .isEqualTo("1")
                .jsonPath("$.length()")
                .isEqualTo(2);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/wiremock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fact").value("1"))
                .andExpect(jsonPath("$.length").value("1"));
    }
}