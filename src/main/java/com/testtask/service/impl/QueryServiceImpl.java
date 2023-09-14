package com.testtask.service.impl;

import com.testtask.model.QueryItem;
import com.testtask.model.RecordItem;
import com.testtask.service.QueryService;
import java.util.List;

public class QueryServiceImpl implements QueryService {

    @Override
    public double processQuery(QueryItem query, List<RecordItem> records) {
        return calculateAverageTime(query, records);
    }

    private double calculateAverageTime(QueryItem query, List<RecordItem> records) {
        int totalRecords = 0;
        int totalTime = 0;

        for (RecordItem record : records) {
            if (matchesQuery(record, query)) {
                totalTime += record.time();
                totalRecords++;
            }
        }
        return totalRecords > 0 ? (double) totalTime / totalRecords : -1;
    }

    private boolean matchesQuery(RecordItem record, QueryItem query) {
        boolean serviceMatch = (query.serviceId() == -1
                || query.serviceId() == record.serviceId());
        boolean variationMatch = (query.variationId() == null
                || query.variationId().equals(record.variationId()));
        boolean questionTypeMatch = (query.questionTypeId() == -1
                || query.questionTypeId() == record.questionTypeId());
        boolean categoryMatch = (query.categoryId() == null
                || query.categoryId().equals(record.categoryId()));
        boolean subCategoryMatch = (query.subCategoryId() == null
                || query.subCategoryId().equals(record.subCategoryId()));
        boolean dateMatch = !(record.date().isBefore(query.dateFrom())
                || record.date().isAfter(query.dateTo()));
        boolean responseTypeMatch = (query.responseType() == record.responseType());

        return serviceMatch
                && variationMatch
                && questionTypeMatch
                && categoryMatch
                && subCategoryMatch
                && dateMatch
                && responseTypeMatch;
    }
}
