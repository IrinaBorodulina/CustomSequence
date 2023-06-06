package com.xdemo.sequence.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IdentityGeneratorImplTest {

    IdentityGeneratorImpl identityGenerator = new IdentityGeneratorImpl();

    @Test
    void buildTicketName() {
        String result = identityGenerator
                .buildTicketName("01-01-2024-", Stream.of("01-01-2024-0001"));
        Assertions.assertEquals("01-01-2024-0002", result);

        //Обнуление после 9999
        result = identityGenerator
                .buildTicketName("01-01-2024-", Stream.of("01-01-2024-9999"));
        Assertions.assertEquals("01-01-2024-0000", result);

        //Переход на следующий день
        result = identityGenerator
                .buildTicketName("02-01-2024-", Stream.empty());
        Assertions.assertEquals("02-01-2024-0001", result);

        //Выбор наибольшего номер из двух
        result = identityGenerator
                .buildTicketName("01-01-2024-", Stream.of("01-01-2024-0001", "01-01-2024-1234"));
        Assertions.assertEquals("01-01-2024-1235", result);
    }
}