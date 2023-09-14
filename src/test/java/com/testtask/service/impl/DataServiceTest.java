package com.testtask.service.impl;

import com.testtask.model.QueryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.testtask.model.DataItem;
import com.testtask.model.RecordItem;
import com.testtask.service.QueryService;
import com.testtask.service.RecordService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataServiceTest {
    private DataService dataService;
    private RecordService recordService;
    private QueryService queryService;
    private RecordItem sampleRecord;
    private QueryItem sampleQuery;

    @BeforeEach
    void setUp() {
        recordService = new RecordServiceImpl();
        queryService = new QueryServiceImpl();
        dataService = new DataService(recordService, queryService);
        sampleRecord = new RecordItem(
                1,
                1,
                8,
                15,
                1,
                'P',
                LocalDate.of(2012, 10, 15),
                83
        );
        sampleQuery = new QueryItem(
                1,
                1,
                8,
                15,
                1,
                'P',
                LocalDate.of(2012, 1, 1),
                LocalDate.of(2012, 12, 1)
        );
    }

    @Test
    void processRecordsAndQueries() {
        List<DataItem> items = Arrays.asList(sampleRecord, sampleQuery);
        List<String> results = dataService.process(items);

        assertEquals(1, results.size());
        assertEquals("83", results.get(0));
    }
}
