package com.xdemo.sequence.repository;

import com.xdemo.sequence.entity.TicketNum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<TicketNum, Integer> {
    @Override
    <S extends TicketNum> S save(S entity);
}
