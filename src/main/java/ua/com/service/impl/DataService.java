package ua.com.service.impl;

import java.util.ArrayList;
import java.util.List;
import ua.com.model.DataItem;
import ua.com.model.QueryItem;
import ua.com.model.RecordItem;
import ua.com.service.QueryService;
import ua.com.service.RecordService;

public class DataService {
    private static final double INVALID_AVERAGE_TIME = -1.0;
    private static final String NO_AVERAGE_TIME_INDICATOR = "-";

    private final RecordService recordService;
    private final QueryService queryService;

    public DataService(RecordService recordService, QueryService queryService) {
        this.recordService = recordService;
        this.queryService = queryService;
    }

    public List<String> process(List<DataItem> items) {
        List<String> results = new ArrayList<>();
        for (DataItem item : items) {
            if (item instanceof RecordItem) {
                recordService.addRecord((RecordItem) item);
            } else if (item instanceof QueryItem) {
                double averageTime = queryService
                        .processQuery((QueryItem) item, recordService.getRecords());
                results.add(averageTime == INVALID_AVERAGE_TIME
                        ? NO_AVERAGE_TIME_INDICATOR : String.valueOf(Math.round(averageTime)));
            }
        }
        return results;
    }
}
