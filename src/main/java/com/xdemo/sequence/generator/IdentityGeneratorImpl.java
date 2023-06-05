package com.xdemo.sequence.generator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

public class IdentityGeneratorImpl implements IdentifierGenerator, Configurable {

    public static final String START_VALUE = "startValue";
    private int startValue;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-");
        String date = simpleDateFormat.format(Date.from(Instant.now()));
        String value = date + String.format("%04d", startValue);
        startValue++;
        return value;
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        startValue = Integer.parseInt(properties.getProperty(START_VALUE));
    }
}
