import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ChangeInput {

    public static String fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/input.txt";

    public static int changeInputFile(String cityName, String countryCode) throws IOException {
        List<String> inputLineList = new ArrayList<>();
        List<String> configValuesList = new ArrayList<>();
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
        String newValue = "\n" + cityName + ", " + countryCode + ", " + configValuesList.get(2).trim();
        System.out.println(newValue);

        Files.write(Paths.get(fileName), newValue.getBytes(), StandardOpenOption.APPEND);
        return 1;
    }

    public static void main(String[] args) {
        System.out.println("Set the city to query via console. \n");
        Scanner obj = new Scanner(System.in);
        System.out.println("PRESS 1 to change the city. Press sth else to continue with current one. \n");
        int option = obj.nextInt();
        System.out.println(option);
        if (option == 1) {
            System.out.println("Insert city name. \n");
            String cityName = obj.next();
            System.out.println("Insert country code. \n");
            String countryCode = obj.next();
            try {
                changeInputFile(cityName, countryCode);
                System.out.println("Run the tests manually with new set values. \n");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error");
            }
        }
        else {
            System.out.println("Run the tests manually. \n");
        }
        System.exit(0);
    }
}
