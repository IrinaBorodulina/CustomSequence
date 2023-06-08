package com.xdemo.sequence.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * WireMockServer конфигурируется в BeforeAll.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WireMockControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MockMvc mockMvc;

    private static WireMockServer wireMockServer;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("external-url", wireMockServer::baseUrl);
    }

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

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