package ua.com.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import ua.com.model.DataItem;
import ua.com.model.QueryItem;

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
    void testInvalidDateFormat() {
        String invalidData = "1\nC 1.1 8.15.1 P 15/10/2012 83";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseFromString(invalidData);
        });
        assertTrue(exception.getMessage().contains("Invalid date format: 15/10/2012"));
    }

    @Test
    void testInvalidNumberOfParts() {
        String invalidData = "1\nC 1.1 8.15.1 15.10.2012 83";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseFromString(invalidData);
        });

        assertTrue(exception.getMessage().contains("Invalid number of parts in the line"));
    }

    @Test
    void testEmptyLine() {
        String emptyData = "\n\n";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseFromString(emptyData);
        });
        assertEquals("Input data cannot be empty", exception.getMessage());
    }

    @Test
    void testInsufficientData() {
        String data = "1\n";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseFromString(data);
        });
        assertEquals("Insufficient number of lines provided", exception.getMessage());
    }

    @Test
    void testInvalidLinePrefix() {
        String invalidPrefix = "1\nE 1.1 8.15.1 P 15.10.2012 83";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseFromString(invalidPrefix);
        });

        assertTrue(exception.getMessage().contains("Invalid line prefix"));
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

    @AfterEach
    void cleanUp() throws IOException {
        Files.list(Path.of(System.getProperty("java.io.tmpdir")))
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
