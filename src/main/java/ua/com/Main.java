package ua.com;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import ua.com.model.DataItem;
import ua.com.service.DataParserService;
import ua.com.service.QueryService;
import ua.com.service.RecordService;
import ua.com.service.impl.DataParserServiceImpl;
import ua.com.service.impl.DataService;
import ua.com.service.impl.QueryServiceImpl;
import ua.com.service.impl.RecordServiceImpl;

public class Main {
    private static final DataParserService parserService = new DataParserServiceImpl();
    private static final RecordService recordService = new RecordServiceImpl();
    private static final QueryService queryService = new QueryServiceImpl();
    private static final DataService dataService = new DataService(recordService, queryService);

    public static void main(String[] args) throws IOException {
        List<DataItem> parsedDataFromFile = parserService
                .parseFromFile(Path.of("src/main/resources/input_data.txt"));
        List<String> resultsFromFile = dataService.process(parsedDataFromFile);
        resultsFromFile.forEach(System.out::println);

        String data = "7\n"
                + "C 1.1 8.15.1 P 15.10.2012 83\n"
                + "C 1 10.1 P 01.12.2012 65\n"
                + "C 1.1 5.5.1 P 01.11.2012 117\n"
                + "D 1.1 8 P 01.01.2012-01.12.2012\n"
                + "C 3 10.2 N 02.10.2012 100\n"
                + "D 1 * P 8.10.2012-20.11.2012\n"
                + "D 3 10 P 01.12.2012";
        List<DataItem> parsedDataFromString = parserService.parseFromString(data);
        List<String> resultsFromString = dataService.process(parsedDataFromString);
        resultsFromString.forEach(System.out::println);
    }
}
