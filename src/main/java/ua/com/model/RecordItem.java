package ua.com.model;

import java.time.LocalDate;

public record RecordItem(
        int serviceId,
        Integer variationId,
        int questionTypeId,
        Integer categoryId,
        Integer subCategoryId,
        char responseType,
        LocalDate date,
        int time
) implements DataItem { }
