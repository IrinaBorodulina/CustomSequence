package com.xdemo.sequence.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/wiremock")
public class WireMockController {

    private final WebClient webClient;

    @Autowired
    public WireMockController(WebClient webClient) {
        this.webClient = webClient;
    }


    @GetMapping
    public Object get() {
//        https://catfact.ninja/fact
        return this.webClient
                .get()
                .uri("/fact")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
