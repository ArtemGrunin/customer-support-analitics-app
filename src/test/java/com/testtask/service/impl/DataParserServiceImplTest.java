package com.testtask.service.impl;

import com.testtask.model.DataItem;
import com.testtask.model.QueryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataParserServiceImplTest {

    private DataParserServiceImpl parser;

    private static final String SAMPLE_DATA = "7\n"
            + "C 1.1 8.15.1 P 15.10.2012 83\n"
            + "C 1 10.1 P 01.12.2012 65\n"
            + "C 1.1 5.5.1 P 01.11.2012 117\n"
            + "D 1.1 8 P 01.01.2012-01.12.2012\n"
            + "C 3 10.2 N 02.10.2012 100\n"
            + "D 1 * P 8.10.2012-20.11.2012\n"
            + "D 3 10 P 01.12.2012";

    private static final int EXPECTED_SIZE_SAMPLE_DATA = 7;

    private static final String ERROR_INVALID_DATE = "Invalid date format: ";
    private static final String ERROR_INVALID_PARTS = "Invalid number of parts in the line";
    private static final String ERROR_EMPTY_DATA = "Input data cannot be empty";
    private static final String ERROR_INSUFFICIENT_DATA = "Insufficient number of lines provided";
    private static final String ERROR_INVALID_PREFIX = "Invalid line prefix";
    private static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir");
    private static final String INVALID_DATE_LINE = "1\nC 1.1 8.15.1 P 15/10/2012 83";
    private static final String INVALID_PARTS_LINE = "1\nC 1.1 8.15.1 15.10.2012 83";
    private static final String EMPTY_LINE = "\n\n";
    private static final String INSUFFICIENT_DATA_LINE = "1\n";
    private static final String INVALID_PREFIX_LINE = "1\nE 1.1 8.15.1 P 15.10.2012 83";

    @BeforeEach
    void setUp() {
        parser = new DataParserServiceImpl();
    }

    @Test
    void parseFromString() {
        List<DataItem> items = parser.parseFromString(SAMPLE_DATA);
        verifySampleDataItems(items);
    }

    @Test
    void parseFromFile() throws IOException {
        Path tempFile = createTempTestFile(SAMPLE_DATA);
        List<DataItem> items = parser.parseFromFile(tempFile);
        verifySampleDataItems(items);
        Files.delete(tempFile);
    }

    @Test
    void invalidDateFormat() {
        assertParsingError(INVALID_DATE_LINE, ERROR_INVALID_DATE + "15/10/2012");
    }

    @Test
    void invalidNumberOfParts() {
        assertParsingError(INVALID_PARTS_LINE, ERROR_INVALID_PARTS);
    }

    @Test
    void emptyLine() {
        assertParsingError(EMPTY_LINE, ERROR_EMPTY_DATA);
    }

    @Test
    void insufficientData() {
        assertParsingError(INSUFFICIENT_DATA_LINE, ERROR_INSUFFICIENT_DATA);
    }

    @Test
    void invalidLinePrefix() {
        assertParsingError(INVALID_PREFIX_LINE, ERROR_INVALID_PREFIX);
    }

    private void verifySampleDataItems(List<DataItem> items) {
        assertEquals(EXPECTED_SIZE_SAMPLE_DATA, items.size());
        assertTrue(items.get(3) instanceof QueryItem);
        assertTrue(items.get(5) instanceof QueryItem);
    }

    private Path createTempTestFile(String data) throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, data.getBytes());
        return tempFile;
    }

    private void assertParsingError(String data, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseFromString(data);
        });
        assertTrue(exception.getMessage().contains(expectedErrorMessage));
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.list(Path.of(TEMP_DIR_PATH))
                .filter(path -> path.toString().startsWith("test"))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
