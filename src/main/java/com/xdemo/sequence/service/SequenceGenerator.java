package com.xdemo.sequence.service;

import com.xdemo.sequence.entity.TicketNum;
import com.xdemo.sequence.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SequenceGenerator {

    private final TicketRepository repository;

    @Autowired
    public SequenceGenerator(TicketRepository repository) {
        this.repository = repository;
    }

    public String getNextTicketNum() {
        TicketNum ticketNum = repository.save(new TicketNum());
        return ticketNum.getId();
    }
}
