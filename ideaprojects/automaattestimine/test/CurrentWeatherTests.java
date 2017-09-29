import config.Config;
import exception.WeatherReportNotFoundException;
import org.junit.*;
import org.json.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import template.CurrentWeatherReport;
import weatherRepository.Weather;
import weatherRepository.WeatherRepository;
import template.CurrentWeatherRequest;
import testHelpers.Validator;
import inputOutput.ReaderWriter;


@RunWith(Parameterized.class)
public class CurrentWeatherTests {

    private static CurrentWeatherRequest request;
    private static String fileName;
    private static Weather weatherRepo;
    private static Config config;

    public CurrentWeatherTests(String cityName, String countryCode) {
        System.out.println(String.valueOf(cityName.isEmpty()));
        if (cityName.isEmpty() || countryCode.isEmpty()) {
            request = new CurrentWeatherRequest(config.getCityName(), config.getCountyCode());
        } else {
            request = new CurrentWeatherRequest(cityName, countryCode);
        }
    }

    // REQUESTED IN THE ASSIGNMENT
    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        Object[][] data = new Object[][]{{"", ""}};
        return Arrays.asList(data);
    }

    @Mock
    private Reader reader;

    @Mock
    private Writer writer;

    @BeforeClass
    public static void setUpClass() throws IOException {
        config = new Config();
        //config.setMock(false); //comment out if you don't want to use mock values.
        if (config.isMock()) {
            weatherRepo = Mockito.mock(Weather.class);
        } else {
            weatherRepo = new WeatherRepository();
        }
        request = new CurrentWeatherRequest(config.getCityName(), config.getCountyCode());
        fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/output.txt";
        File file = new File(fileName);
        file.createNewFile();
        Date today = new Date();
        String testTitle = "\n  -----------------------------------------------TEST STARTED, CURRENT WEATHER TESTS " + today.toString() + " --------------------------------------------------\n";
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
       /*
        @Override
        protected void succeeded(Description d) {
            System.out.println(testName.getMethodName() + "SUCESSS");
        }
        */
    };

    @After
    public void afterTest() {
    }

    @Test(timeout = 10000)
    public void isCurrentWeatherApiResponseStatusCode200() throws IOException {
        try {
            if (config.isMock()) {
                when(weatherRepo.getWeatherApiResponseStatus(request)).thenReturn(200); // Mock method.
            }
            int statusCode = weatherRepo.getWeatherApiResponseStatus(request);
            if (config.isMock()) verify(weatherRepo).getWeatherApiResponseStatus(request);
            assertEquals(200, statusCode);
            String result = testName.getMethodName() + ", " + statusCode + ", PASSED \n";
            ReaderWriter.appendToFile(result, fileName);
        } catch (WeatherReportNotFoundException e) {
            fail("Failure cause: " + e.getMessage());
        }
    }

    @Test(timeout = 10000)
    public void doesGetCurrentWeatherReturnDate() throws IOException {
        try {
            CurrentWeatherReport report;
            if (config.isMock()) {
                JSONObject mockResp = new JSONObject();
                JSONObject cityValues = new JSONObject();
                cityValues.put("lat", 2.00);
                cityValues.put("lon", 3.00);
                mockResp.put("city", cityValues);
                report = new CurrentWeatherReport("Tallinn", "EE", mockResp.getJSONObject("city"), 19, new Long(1511190821));
                when(weatherRepo.getCurrentWeather(request)).thenReturn(report);
                when(weatherRepo.getGeoCoordsOfRequestedCity(mockResp.getJSONObject("city"))).thenReturn(cityValues);
            }
            report = weatherRepo.getCurrentWeather(request);
            boolean responseHasDatekey;
            if (report.getReportDateInUnix() != null) responseHasDatekey = true;
            else responseHasDatekey = false;
            if (config.isMock()) verify(weatherRepo).getCurrentWeather(request);
            String result = testName.getMethodName() + ", " + report.getReportDateInUnix() + ", PASSED \n";
            assertEquals(true, responseHasDatekey);
            ReaderWriter.appendToFile(result, fileName);

        } catch (WeatherReportNotFoundException e) {
            fail("Failure cause: " + e.getMessage());

        }
    }

    @Test(timeout = 10000)
    public void doesGetCurrentWeatherReturnInfoAboutToday() throws IOException {
        try {
            CurrentWeatherReport report;
            if (config.isMock()) {
                JSONObject mockResp = new JSONObject();
                JSONObject cityValues = new JSONObject();
                cityValues.put("lat", 2.00);
                cityValues.put("lon", 3.00);
                mockResp.put("city", cityValues);
                report = new CurrentWeatherReport("Tallinn", "EE", mockResp.getJSONObject("city"), 19, new Long(1511190821));
                when(weatherRepo.getCurrentWeather(request)).thenReturn(report);
                when(weatherRepo.getGeoCoordsOfRequestedCity(mockResp.getJSONObject("city"))).thenReturn(cityValues);
            }
            report = weatherRepo.getCurrentWeather(request);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Long dateInMilliseconds = report.getReportDateInUnix();
            Date today = new Date();
            Date dateInResponse = new Date(dateInMilliseconds * 1000);
            String result = testName.getMethodName() + ", " + report + ", PASSED \n";
            if (config.isMock()) {
                verify(weatherRepo).getCurrentWeather(request);
                assertEquals(Date.class, dateInResponse.getClass());
            } else {
                assertEquals(dateFormat.format(today), dateFormat.format(dateInResponse));
            }
            ReaderWriter.appendToFile(result, fileName);
        } catch (WeatherReportNotFoundException e) {
            fail("Failure cause: " + e.getMessage());
        }
    }

    @Test(timeout = 10000)
    public void doesCurrentWeatherApiReturnCoords() throws Exception {
        try {
            CurrentWeatherReport report;
            if (config.isMock()) {
                JSONObject mockResp = new JSONObject();
                JSONObject cityValues = new JSONObject();
                cityValues.put("lat", 2.00);
                cityValues.put("lon", 3.00);
                mockResp.put("city", cityValues);
                report = new CurrentWeatherReport("Tallinn", "EE", mockResp.getJSONObject("city"), 19, new Long(1511190821));
                when(weatherRepo.getCurrentWeather(request)).thenReturn(report);
                when(weatherRepo.getGeoCoordsOfRequestedCity(mockResp.getJSONObject("city"))).thenReturn(cityValues);
            }

            report = weatherRepo.getCurrentWeather(request);
            JSONObject coordsObject = weatherRepo.getGeoCoordsOfRequestedCity(report.getGeoCoordinates());
            boolean hasLatitude;
            if (coordsObject != null && coordsObject.has("lat")) hasLatitude = true;
            else hasLatitude = false;
            boolean hasLongitude;
            if (coordsObject != null && coordsObject.has("lon")) hasLongitude = true;
            else hasLongitude = false;
            Validator.validateGeoCoordinates(coordsObject);
            boolean hasLatAndLon = hasLatitude && hasLongitude;
            if (config.isMock()) verify(weatherRepo).getCurrentWeather(request);
            assertEquals(true, hasLatAndLon);
            String result = testName.getMethodName() + ", " + report + ", PASSED \n";
            ReaderWriter.appendToFile(result, fileName);
        } catch (WeatherReportNotFoundException e) {
            fail("Failure cause: " + e.getMessage());

        }
    }
}
