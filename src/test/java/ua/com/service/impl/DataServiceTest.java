package ua.com.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.model.DataItem;
import ua.com.model.QueryItem;
import ua.com.model.RecordItem;
import ua.com.service.QueryService;
import ua.com.service.RecordService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataServiceTest {
    private DataService dataService;
    private RecordService recordService;
    private QueryService queryService;

    @BeforeEach
    void setUp() {
        recordService = new RecordServiceImpl();
        queryService = new QueryServiceImpl();
        dataService = new DataService(recordService, queryService);
    }

    @Test
    void processRecordsAndQueries() {
        RecordItem record1 = new RecordItem(
                1,
                1,
                8,
                15,
                1,
                'P',
                LocalDate.of(2012, 10, 15),
                83
        );
        QueryItem query1 = new QueryItem(
                1,
                1,
                8,
                15,
                1,
                'P',
                LocalDate.of(2012, 1, 1),
                LocalDate.of(2012, 12, 1)
        );

        List<DataItem> items = Arrays.asList(record1, query1);
        List<String> results = dataService.process(items);

        assertEquals(1, results.size());
        assertEquals("83", results.get(0));
    }
}
