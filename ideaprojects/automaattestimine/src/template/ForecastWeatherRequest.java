package template;

public class ForecastWeatherRequest extends WeatherRequest {
    public final int foreCastLength;

    public ForecastWeatherRequest(String cityName, String countryCode, int foreCastLength) {
        super(cityName, countryCode);
        this.foreCastLength = foreCastLength;
    }
}
