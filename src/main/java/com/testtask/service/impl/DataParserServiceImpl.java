package com.testtask.service.impl;

import com.testtask.model.DataItem;
import com.testtask.model.QueryItem;
import com.testtask.model.RecordItem;
import com.testtask.service.DataParserService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataParserServiceImpl implements DataParserService {

    private static final String DATE_PATTERN = "d[.]M[.]yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern(DATE_PATTERN)
            .toFormatter();
    private static final String RECORD_PREFIX = "C";
    private static final String QUERY_PREFIX = "D";
    private static final String WILDCARD = "*";
    private static final String SPACE_DELIMITER = " ";
    private static final String DOT_DELIMITER = "\\.";
    private static final String DATE_RANGE_DELIMITER = "-";
    private static final String LINE_SEPARATOR = "\n";
    private static final int SERVICE_PART_INDEX = 1;
    private static final int QUESTION_PART_INDEX = 2;
    private static final int RESPONSE_TYPE_INDEX = 3;
    private static final int DATE_INDEX = 4;
    private static final int TIME_INDEX = 5;
    private static final int SERVICE_ID_INDEX = 0;
    private static final int VARIATION_ID_INDEX = 1;
    private static final int QUESTION_TYPE_ID_INDEX = 0;
    private static final int CATEGORY_ID_INDEX = 1;
    private static final int SUB_CATEGORY_ID_INDEX = 2;
    private static final int DATE_FROM_INDEX = 0;
    private static final int DATE_TO_INDEX = 1;
    private static final int EXPECTED_PARTS_FOR_C = 6;
    private static final int EXPECTED_PARTS_FOR_D = 5;

    @Override
    public List<DataItem> parseFromFile(Path path) throws IOException {
        List<String> inputData = Files.readAllLines(path);
        return parseInputData(inputData);
    }

    @Override
    public List<DataItem> parseFromString(String inputString) {
        List<String> inputData = Arrays.asList(inputString.split(LINE_SEPARATOR));
        return parseInputData(inputData);
    }

    private List<DataItem> parseInputData(List<String> inputData) {
        validateInputData(inputData);
        List<String> actualData = extractActualData(inputData);
        return parseData(actualData);
    }

    private List<String> extractActualData(List<String> inputData) {
        int numberOfLines = Integer.parseInt(inputData.get(0));
        return inputData.subList(1, numberOfLines + 1);
    }

    private List<DataItem> parseData(List<String> lines) {
        List<DataItem> result = new ArrayList<>();

        for (String line : lines) {
            validateLineNotEmpty(line);
            validateLinePrefix(line);
            if (line.startsWith(RECORD_PREFIX)) {
                result.add(parseRecord(line));
            } else if (line.startsWith(QUERY_PREFIX)) {
                result.add(parseQuery(line));
            }
        }
        return result;
    }

    private void validateInputData(List<String> inputData) {
        if (inputData == null || inputData.isEmpty()) {
            throw new IllegalArgumentException("Input data cannot be empty");
        }

        int numberOfLines;
        try {
            numberOfLines = Integer.parseInt(inputData.get(0));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "First line must represent the number of subsequent lines", e);
        }

        if (inputData.size() < numberOfLines + 1) {
            throw new IllegalArgumentException("Insufficient number of lines provided");
        }
    }

    private RecordItem parseRecord(String line) {
        String[] parts = splitAndValidate(line, EXPECTED_PARTS_FOR_C);
        String[] serviceParts = parts[SERVICE_PART_INDEX].split(DOT_DELIMITER);
        String[] questionParts = parts[QUESTION_PART_INDEX].split(DOT_DELIMITER);
        int serviceId = Integer.parseInt(serviceParts[SERVICE_ID_INDEX]);
        Integer variationId = serviceParts.length > 1
                ? Integer.parseInt(serviceParts[VARIATION_ID_INDEX]) : null;
        int questionTypeId = Integer.parseInt(questionParts[QUESTION_TYPE_ID_INDEX]);
        Integer categoryId = questionParts.length > 1
                ? Integer.parseInt(questionParts[CATEGORY_ID_INDEX]) : null;
        Integer subCategoryId = questionParts.length > 2
                ? Integer.parseInt(questionParts[SUB_CATEGORY_ID_INDEX]) : null;
        LocalDate date = parseDate(parts[DATE_INDEX]);
        int time = Integer.parseInt(parts[TIME_INDEX]);

        return new RecordItem(
                serviceId,
                variationId,
                questionTypeId,
                categoryId,
                subCategoryId,
                parts[RESPONSE_TYPE_INDEX].charAt(0),
                date,
                time
        );
    }

    private QueryItem parseQuery(String line) {
        String[] parts = splitAndValidate(line, EXPECTED_PARTS_FOR_D);
        String[] serviceParts = parts[SERVICE_PART_INDEX].split(DOT_DELIMITER);
        String[] questionParts = parts[QUESTION_PART_INDEX].split(DOT_DELIMITER);
        int serviceId = WILDCARD.equals(serviceParts[SERVICE_ID_INDEX])
                ? -1 : Integer.parseInt(serviceParts[SERVICE_ID_INDEX]);
        Integer variationId = serviceParts.length > 1
                && !WILDCARD.equals(serviceParts[VARIATION_ID_INDEX])
                ? Integer.parseInt(serviceParts[VARIATION_ID_INDEX]) : null;
        int questionTypeId = WILDCARD.equals(questionParts[QUESTION_TYPE_ID_INDEX])
                ? -1 : Integer.parseInt(questionParts[QUESTION_TYPE_ID_INDEX]);
        Integer categoryId = questionParts.length > 1
                && !WILDCARD.equals(questionParts[CATEGORY_ID_INDEX])
                ? Integer.parseInt(questionParts[CATEGORY_ID_INDEX]) : null;
        Integer subCategoryId = questionParts.length > 2
                && !WILDCARD.equals(questionParts[SUB_CATEGORY_ID_INDEX])
                ? Integer.parseInt(questionParts[SUB_CATEGORY_ID_INDEX]) : null;
        String[] dateParts = parts[DATE_INDEX].split(DATE_RANGE_DELIMITER);
        LocalDate dateFrom = parseDate(dateParts[DATE_FROM_INDEX]);
        LocalDate dateTo = dateParts.length > 1
                ? parseDate(dateParts[DATE_TO_INDEX]) : dateFrom;

        return new QueryItem(
                serviceId,
                variationId,
                questionTypeId,
                categoryId,
                subCategoryId,
                parts[RESPONSE_TYPE_INDEX].charAt(0),
                dateFrom,
                dateTo
        );
    }

    private void validateLineNotEmpty(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Line cannot be empty");
        }
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + date, e);
        }
    }

    private String[] splitAndValidate(String line, int expectedParts) {
        validateLineNotEmpty(line);

        String[] parts = line.split(SPACE_DELIMITER);
        if (parts.length != expectedParts) {
            throw new IllegalArgumentException("Invalid number of parts in the line: " + line);
        }
        return parts;
    }

    private void validateLinePrefix(String line) {
        if (!line.startsWith(RECORD_PREFIX + SPACE_DELIMITER)
                && !line.startsWith(QUERY_PREFIX + SPACE_DELIMITER)) {
            throw new IllegalArgumentException("Invalid line prefix in line: " + line);
        }
    }
}
