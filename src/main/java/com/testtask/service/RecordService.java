package com.testtask.service;

import com.testtask.model.RecordItem;
import java.util.List;

public interface RecordService {
    void addRecord(RecordItem record);

    List<RecordItem> getRecords();
}

