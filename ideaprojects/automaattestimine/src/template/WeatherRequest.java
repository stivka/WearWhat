package template;

public class WeatherRequest {

    public final String cityName;
    public final String countryCode;

    public WeatherRequest(String cityName, String countryCode) {
        this.cityName = cityName;
        this.countryCode = countryCode;
    }
}
