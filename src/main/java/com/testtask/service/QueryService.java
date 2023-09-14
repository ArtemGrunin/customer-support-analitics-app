package com.testtask.service;

import com.testtask.model.QueryItem;
import com.testtask.model.RecordItem;
import java.util.List;

public interface QueryService {
    double processQuery(QueryItem query, List<RecordItem> records);
}
