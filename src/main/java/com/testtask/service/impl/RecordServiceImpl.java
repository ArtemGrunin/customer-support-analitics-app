package com.testtask.service.impl;

import com.testtask.model.RecordItem;
import com.testtask.service.RecordService;
import java.util.ArrayList;
import java.util.List;

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
