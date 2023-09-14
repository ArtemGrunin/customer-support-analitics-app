package com.testtask.service.impl;

import com.testtask.model.QueryItem;
import com.testtask.model.RecordItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryServiceImplTest {
    private QueryServiceImpl queryService;
    private List<RecordItem> records;

    @BeforeEach
    void setUp() {
        queryService = new QueryServiceImpl();

        records = Arrays.asList(
                new RecordItem(
                        1,
                        1,
                        8,
                        15,
                        1,
                        'P',
                        LocalDate.of(2012, 10, 15),
                        83),
                new RecordItem(
                        1,
                        null,
                        10,
                        1,
                        null,
                        'P',
                        LocalDate.of(2012, 12, 1), 65),
                new RecordItem(1,
                        1,
                        5,
                        5,
                        1,
                        'P',
                        LocalDate.of(2012, 11, 1),
                        117),
                new RecordItem(
                        3,
                        null,
                        10,
                        2,
                        null,
                        'N',
                        LocalDate.of(2012, 10, 2),
                        100)
        );
    }

    @Test
    void specificServiceAndQuestion() {
        QueryItem query = new QueryItem(1, 1, 8, 15, 1, 'P',
                LocalDate.of(2012, 1, 1), LocalDate.of(2012, 12, 1));
        double averageTime = queryService.processQuery(query, records);
        assertEquals(83.0, averageTime);
    }

    @Test
    void allQuestionsForService() {
        QueryItem query = new QueryItem(
                1, null, -1, null, null, 'P',
                LocalDate.of(2012, 10, 8),
                LocalDate.of(2012, 11, 20)
        );
        double averageTime = queryService.processQuery(query, records);
        assertEquals((83.0 + 117.0) / 2, averageTime);
    }

    @Test
    void specificServiceWithoutMatches() {
        QueryItem query = new QueryItem(
                3, null, 10, 2, null, 'P',
                LocalDate.of(2012, 12, 1), LocalDate.of(2012, 12, 1)
        );
        double averageTime = queryService.processQuery(query, records);
        assertEquals(-1.0, averageTime);
    }
}
