package exception;

public class WeatherReportNotFoundException extends Exception {

    public WeatherReportNotFoundException(String errMsg) {
        super("Error: " + errMsg);
    }
}
