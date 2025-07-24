package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihangz
 */
public class BufferReaderUtil {
    public static List<String> readNextKLines(BufferedReader bufferedReader, Integer k) throws IOException {
        List<String> output = new ArrayList<>();
        int i = 0;
        while (i < k) {
            String line = bufferedReader.readLine();
            if (!line.startsWith("$")) {
                output.add(line);
                i++;
            }
        }
        return output;
    }
}
