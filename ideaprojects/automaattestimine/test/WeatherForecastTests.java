import config.Config;
import exception.WeatherReportNotFoundException;
import inputOutput.ReaderWriter;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.json.*;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import template.CurrentWeatherRequest;
import template.ForecastWeatherReport;
import weatherRepository.Weather;
import weatherRepository.WeatherRepository;
import template.ForecastWeatherRequest;
import testHelpers.Validator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class WeatherForecastTests {


    private static ForecastWeatherRequest forecastWeatherRequest;
    private static CurrentWeatherRequest currentWeatherRequest;
    private static String fileName;
    private static Weather weatherRepo;
    private static Config config;

    public static void write(String msg) throws IOException {
        Files.write(Paths.get(fileName), msg.getBytes(), StandardOpenOption.APPEND);
    }

    public WeatherForecastTests(String cityName, String countryCode) {
        if (cityName.isEmpty() || countryCode.isEmpty()) {
            forecastWeatherRequest = new ForecastWeatherRequest(config.getCityName(), config.getCountyCode(), 5);
        }
        else {
            forecastWeatherRequest = new ForecastWeatherRequest(cityName, countryCode, 5);
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        Object[][] data = new Object[][] {{"",""}};
        return Arrays.asList(data);
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        config = new Config();
        //config.setMock(false); //comment out if you don't want to use mock values.
        if (config.isMock()) {
            weatherRepo = Mockito.mock(Weather.class);
        }
        else {
            weatherRepo = new WeatherRepository();
        }
        forecastWeatherRequest = new ForecastWeatherRequest(config.getCityName(), config.getCountyCode(), 5);
        currentWeatherRequest = new CurrentWeatherRequest(config.getCityName(), config.getCountyCode());

        fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/output.txt";
        File file = new File(fileName);
        file.createNewFile();
        Date today = new Date();
        String testTitle = "\n  -----------------------------------------------TEST STARTED, WEATHER FORECAST TESTS " + today.toString() + " --------------------------------------------------\n";
        ReaderWriter.appendToFile(testTitle, fileName);
    }

    @Rule
    public TestName testName = new TestName();

    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println(e + testName.getMethodName());
            String failResult = testName.getMethodName() + ", ERROR: " + e.getMessage() + ", FAILED \n";
            try {
                ReaderWriter.appendToFile(failResult, fileName);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    };

    @Test(timeout = 10000)
    public void isForecastWeatherApiResponseStatusCode200() throws IOException {
        try {
            if (config.isMock()) {
                when(weatherRepo.getWeatherApiResponseStatus(currentWeatherRequest)).thenReturn(200); // Mock method.
            }
            int statusCode = weatherRepo.getWeatherApiResponseStatus(currentWeatherRequest);
            assertEquals(200, statusCode);
            if (config.isMock()) verify(weatherRepo).getWeatherApiResponseStatus(currentWeatherRequest);
            String result = testName.getMethodName() + ", " + statusCode + ", PASSED \n";
            ReaderWriter.appendToFile(result, fileName);

        } catch (WeatherReportNotFoundException e) {
            fail("Failure cause: " + e.getMessage());
        }
    }

    @Test(timeout = 10000)
    public void doesCurrentWeatherApiReturnCoords() throws IOException {
        try {
            ForecastWeatherReport response = null;
            if (config.isMock()) {
                JSONObject mockResp = new JSONObject();
                JSONObject cityValues = new JSONObject();
                cityValues.put("lat", 2.00);
                cityValues.put("lon", 3.00);
                mockResp.put("city", cityValues);
                response = new ForecastWeatherReport("Tallinn", "EE", mockResp.getJSONObject("city"), new JSONArray());
                when(weatherRepo.getWeatherForecast(forecastWeatherRequest)).thenReturn(response);
                when(weatherRepo.getGeoCoordsOfRequestedCity(mockResp.getJSONObject("city"))).thenReturn(cityValues);
            }
            response = weatherRepo.getWeatherForecast(forecastWeatherRequest);
            if (response == null) throw new NullPointerException("Response is null");
            JSONObject coordsObject = weatherRepo.getGeoCoordsOfRequestedCity(response.getGeoCoordinates());
            boolean hasLatitude;
            if (response.getLatitude() != null) hasLatitude = true;
            else hasLatitude = false;
            boolean hasLongitude;
            if (response.getLongitude() != null) hasLongitude = true;
            else hasLongitude = false;
            Boolean hasLatAndLon = (hasLatitude && hasLongitude);
            Validator.validateGeoCoordinates(coordsObject);
            assertTrue(hasLatAndLon);
            if (config.isMock()) verify(weatherRepo).getWeatherForecast(forecastWeatherRequest);
            String result = testName.getMethodName() + ", " + response + ", PASSED \n";
            ReaderWriter.appendToFile(result, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(timeout = 10000)
    public void doesWeatherApiReturnHighestAndLowestTempOfSetDays() throws IOException {
        ForecastWeatherReport response;
        JSONArray jsonMockArray = new JSONArray();
        try {
            if (config.isMock()) {
                JSONObject tempObject = new JSONObject();
                tempObject.put("maxTemp", 203.3);
                tempObject.put("minTemp", 207.2);
                jsonMockArray.put(tempObject);
                response = new ForecastWeatherReport("Tallinn", "EE", new JSONObject(), jsonMockArray);
                when(weatherRepo.getWeatherForecast(forecastWeatherRequest)).thenReturn(response);
                when(weatherRepo.getHighestandLowestTempOfEachDayInForecast(jsonMockArray)).thenReturn(jsonMockArray);
            }

            response = weatherRepo.getWeatherForecast(forecastWeatherRequest);
            if (!config.isMock()) {
                if (response == null) {
                    throw new NullPointerException("Response is null");
                }
            }
            JSONArray foreCastArray = weatherRepo.getHighestandLowestTempOfEachDayInForecast(response.getForeCastList());
            int temperatureCount = 0;
            for (int i = 0 ; i < foreCastArray.length(); i++) {
                JSONObject temeperatures = foreCastArray.getJSONObject(i);
                Validator.validateTemperature(temeperatures.getDouble("maxTemp"));
                Validator.validateTemperature(temeperatures.getDouble("minTemp"));
                if (temeperatures != null && temeperatures.has("maxTemp") && temeperatures.has("minTemp")) {
                    temperatureCount++;
                }
            }
            if (config.isMock()) {
                verify(weatherRepo).getWeatherForecast(forecastWeatherRequest);
                verify(weatherRepo).getHighestandLowestTempOfEachDayInForecast(jsonMockArray);
                assertEquals(1, temperatureCount);
            }
            else assertEquals(response.getForeCastList().length(), temperatureCount);
            String result = testName.getMethodName() + ", " + response + ", PASSED \n";
            ReaderWriter.appendToFile(result, fileName);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
}
