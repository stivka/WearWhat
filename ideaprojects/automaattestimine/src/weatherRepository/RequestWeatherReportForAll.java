package weatherRepository;

import inputOutput.ReaderWriter;
import org.json.JSONArray;
import org.json.JSONException;
import template.CurrentWeatherReport;
import template.CurrentWeatherRequest;
import template.ForecastWeatherReport;
import template.ForecastWeatherRequest;

import java.io.File;
import java.io.IOException;

public class RequestWeatherReportForAll {
    public static void main(String[] args) throws JSONException{
        System.out.println("Reading from input.txt . \n");

        Weather weatherRepo = new WeatherRepository();
        JSONArray configData = ReaderWriter.readAllLinesFromFile("/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/input.txt");
        for (int i = 0; i < configData.length(); i++) {
            String[] split = configData.get(i).toString().split(",");
            String cityName = split[0].trim();
            String countyCode = split[1].trim();
            String key = split[2].trim();
            ForecastWeatherRequest foreCastRequest = new ForecastWeatherRequest(cityName, countyCode, 3);
            CurrentWeatherRequest currentWeatherRequest = new CurrentWeatherRequest(cityName, countyCode);
            try {
                CurrentWeatherReport currentReport = weatherRepo.getCurrentWeather(currentWeatherRequest);
                ForecastWeatherReport foreCastReport = weatherRepo.getWeatherForecast(foreCastRequest);
                try {
                    String fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/cities/" + cityName + ".txt";
                    while (new File(fileName).exists()) {
                        fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/cities/" + cityName + String.valueOf(i) + ".txt";
                        i++;
                    }
                    File file = new File(fileName);
                    file.createNewFile();
                    ReaderWriter.appendToFile(currentReport.toString() + " \n", fileName);
                    ReaderWriter.appendToFile(foreCastReport.toString() + " \n", fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

}
