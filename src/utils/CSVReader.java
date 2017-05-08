package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple wrapper for a BufferedReader that interprets comma-separated values (CSV) files as a
 * series of maps whose keys are defined by the first line of the CSV file.
 */
public class CSVReader {

    private final BufferedReader reader;
    private final List<String> keys;

    public CSVReader(File file) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        keys = readAndSeparateLine();
    }


    public Map<String, String> readLine() throws IOException {

        final List<String> values = readAndSeparateLine();

        if (values == null) {
            return null; // End of file.
        }

        final Map<String, String> map = new HashMap<>();

        for (int i = 0; i < values.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }

        return map;
    }


    public void close() throws IOException {
        reader.close();
    }


    private List<String> readAndSeparateLine() throws IOException {
        final String line = reader.readLine();
        if (line == null) {
            return null; // End of file.
        }
        else {
            return Arrays.asList(line.split(","));
        }
    }
}