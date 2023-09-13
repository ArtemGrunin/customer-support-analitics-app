package ua.com.service.impl;

import java.util.ArrayList;
import java.util.List;
import ua.com.model.RecordItem;
import ua.com.service.RecordService;

public class RecordServiceImpl implements RecordService {
    private final List<RecordItem> records = new ArrayList<>();

    @Override
    public void addRecord(RecordItem record) {
        records.add(record);
    }

    @Override
    public List<RecordItem> getRecords() {
        return records;
    }
}
