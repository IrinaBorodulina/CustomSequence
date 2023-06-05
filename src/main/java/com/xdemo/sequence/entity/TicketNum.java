package com.xdemo.sequence.entity;

import com.xdemo.sequence.generator.IdentityGeneratorImpl;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "tickets")
public class TicketNum {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @GenericGenerator(
            name = "ticket_seq",
            strategy = "com.xdemo.sequence.generator.IdentityGeneratorImpl")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
