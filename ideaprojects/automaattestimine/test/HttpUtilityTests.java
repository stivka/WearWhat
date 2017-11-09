import config.Config;
import inputOutput.ReaderWriter;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import utility.HttpUtility;
import template.CurrentWeatherRequest;
import template.ForecastWeatherRequest;

public class HttpUtilityTests {

    private HttpUtility httpRequest = new HttpUtility();
    private static CurrentWeatherRequest currentWeatherRequest;
    private static ForecastWeatherRequest weatherForecastRequest;
    private static String fileName;
    private static Config config;

    @BeforeClass
    public static void setUpClass() throws IOException {
        config = new Config();
        currentWeatherRequest = new CurrentWeatherRequest(config.getCityName(), config.getCountyCode());
        weatherForecastRequest = new ForecastWeatherRequest("Tallinn", "EE", 3);
        fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/output.txt";
        File file = new File(fileName);
        file.createNewFile();
        Date today = new Date();
        String testTitle = "\n  -----------------------------------------------TEST STARTED, HTTP UTILITY TESTS " + today.toString() + " --------------------------------------------------\n";
        ReaderWriter.appendToFile(testTitle, fileName);
    }


   

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

    @Test(timeout = 10000)
    public void makesHttpRequestToCurrentWeatherApi() {
        try {
            String response = httpRequest.makeCurrentWeatherApiHttpRequest(currentWeatherRequest, config.getApiKey());
            assertEquals(response.isEmpty(), false);
            String result = testName.getMethodName() + ", " + response + ", PASSED \n";
            ReaderWriter.appendToFile(result, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(timeout = 10000)
    public void makesHttpRequestToForecastWeatherApi() throws IOException {
        httpRequest.makeForecastWeatherApiHttpRequest(weatherForecastRequest, config.getApiKey());
        try {
            String response = httpRequest.makeForecastWeatherApiHttpRequest(weatherForecastRequest, config.getApiKey());
            assertEquals(response.isEmpty(), false);
            String result = testName.getMethodName() + ", " + response + ", PASSED \n";
            ReaderWriter.appendToFile(result, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
