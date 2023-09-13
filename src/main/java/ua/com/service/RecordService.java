package ua.com.service;

import java.util.List;
import ua.com.model.RecordItem;

public interface RecordService {
    void addRecord(RecordItem record);

    List<RecordItem> getRecords();
}

