package com.testtask.service;

import com.testtask.model.DataItem;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface DataParserService {
    List<DataItem> parseFromFile(Path path) throws IOException;

    List<DataItem> parseFromString(String inputString);
}
