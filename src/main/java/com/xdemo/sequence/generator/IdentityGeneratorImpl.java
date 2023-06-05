package com.xdemo.sequence.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

public class IdentityGeneratorImpl implements IdentifierGenerator, Configurable {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-");
        String currentDate = simpleDateFormat.format(Date.from(Instant.now()));

        String query = String.format("select %s from %s where id like \"%s%%\"",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName(),
                currentDate); //выборка всех сохраненных сущностей сегодня
        Stream<String> ids = session.createQuery(query, String.class).stream();

        Long max = ids
                .map(id -> id.replace(currentDate, ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);

        String ticketNum = String.format("%04d", max + 1);
        return currentDate + ticketNum.substring(ticketNum.length() - 4);
    }

}
