package com.xdemo.sequence.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

public class IdentityGeneratorImpl implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-");
        String currentDate = simpleDateFormat.format(Date.from(Instant.now()));

        String query = String.format("select %s from %s where id like \"%s%%\"",
                session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
                obj.getClass().getSimpleName(),
                currentDate); //выборка всех сохраненных сущностей за сегодня
        Stream<String> todayIds = session.createQuery(query, String.class).stream();

        return buildTicketName(currentDate, todayIds);
    }

    /**
     * После тикета с номером "yyyy-MM-dd-9999" отсчет идет заново с "yyyy-MM-dd-0000".
     *
     * @param currentDate - Текущая дата в формате "yyyy-MM-dd-".
     * @param ids         - Стрим уже сохраненных айдишников в бд за сегодня, в формате "yyyy-MM-dd-XXXX".
     * @return Номер следующего тикета.
     */
    protected String buildTicketName(String currentDate, Stream<String> ids) {
        Long max = ids
                .map(id -> id.replace(currentDate, ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);

        String ticketNum = String.format("%04d", max + 1);
        return currentDate + ticketNum.substring(ticketNum.length() - 4);
    }

}
