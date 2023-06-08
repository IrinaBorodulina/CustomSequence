package com.xdemo.sequence.api;

import com.xdemo.sequence.entity.TicketNum;
import com.xdemo.sequence.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @AutoConfigureMockMvc - отвечает за создание MockMvc в контексте.
 * @WebMvcTest - создает часть контекста.
 * @SpringBootTest - создает весь контекст.
 *
 * @AutoConfigureMockMvc + @SpringBootTest, либо @AutoConfigureMockMvc + @WebMvcTest (с необходимыми бинами).
 */
//@WebMvcTest({SequenceGenerator.class, TicketController.class})
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketControllerMockRepoTest {

    @MockBean
    private TicketRepository ticketRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getTicketNum() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-");
        String currentDate = simpleDateFormat.format(Date.from(Instant.now()));
        String ticketNumId = String.format("%s-%04d", currentDate, 1);
        when(ticketRepository.save(Mockito.any(TicketNum.class))).thenReturn(
                new TicketNum(ticketNumId));

        this.mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().string(ticketNumId));
    }
}