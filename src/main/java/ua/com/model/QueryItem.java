package ua.com.model;

import java.time.LocalDate;

public record QueryItem(
        int serviceId,
        Integer variationId,
        int questionTypeId,
        Integer categoryId,
        Integer subCategoryId,
        char responseType,
        LocalDate dateFrom,
        LocalDate dateTo
) implements DataItem { }
