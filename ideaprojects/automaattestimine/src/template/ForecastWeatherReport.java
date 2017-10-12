package template;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForecastWeatherReport extends WeatherReport {
    public JSONArray getForeCastList() {
        return foreCast;
    }

    public final JSONArray foreCast;

    public ForecastWeatherReport(String cityName, String countryCode, JSONObject geoCoordinates, JSONArray foreCastList) {
        super(cityName, countryCode, geoCoordinates);
        this.foreCast = foreCastList;
    }

    @Override
    public String toString() {
        return "cityName: " + cityName + ", " + "geoCoords: " + geoCoordinates + ", Forecast: " + getForeCastList().toString();
    }
}
