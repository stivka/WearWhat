package inputOutput;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderWriter {

    public static List readLineFromFileAndSplitByComma(String inputFileAbsPath) {
        List<String> inputLineList = new ArrayList<>();
        List<String> configValuesList = new ArrayList<>();

        String fileName = inputFileAbsPath;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            //br returns as stream and convert it into a List
            inputLineList = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputLineList.forEach(line -> {
            String [] split = line.split(",");
            for (int i = 0; i < split.length; i++) {
                configValuesList.add(String.valueOf(split[i]));
            }
        });

        return configValuesList;
    }

    public static JSONArray readAllLinesFromFile(String inputFileAbsPath) {
        List<String> inputLineList = new ArrayList<>();
        JSONArray arrayOfLines = new JSONArray();

        String fileName = inputFileAbsPath;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            //br returns as stream and convert it into a List
            inputLineList = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputLineList.forEach(line -> {
          //  System.out.println(line);
                arrayOfLines.put(line);
        });

        return arrayOfLines;
    }

    public static void appendToFile(String msg, String outputFileAbsPath) throws IOException {
        Files.write(Paths.get(outputFileAbsPath), msg.getBytes(), StandardOpenOption.APPEND);
    }
}
