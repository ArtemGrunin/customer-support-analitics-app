package ua.com.service;

import java.util.List;
import ua.com.model.QueryItem;
import ua.com.model.RecordItem;

public interface QueryService {
    double processQuery(QueryItem query, List<RecordItem> records);
}
