package com.xdemo.sequence.api;

import com.xdemo.sequence.service.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final SequenceGenerator generator;

    @Autowired
    public TicketController(SequenceGenerator generator) {
        this.generator = generator;
    }

    @GetMapping
    public String getTicketNum() {
        return generator.getNextTicketNum();
    }
}
