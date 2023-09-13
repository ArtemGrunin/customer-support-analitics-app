package ua.com.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import ua.com.model.DataItem;

public interface DataParserService {
    List<DataItem> parseData(List<String> lines);

    List<DataItem> parseFromFile(Path path) throws IOException;

    List<DataItem> parseFromString(String inputString);
}
