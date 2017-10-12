package weatherRepository;

import exception.WeatherReportNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import template.*;

public interface Weather {

    CurrentWeatherReport getCurrentWeather(CurrentWeatherRequest request) throws WeatherReportNotFoundException;

    Integer getWeatherApiResponseStatus(CurrentWeatherRequest request) throws WeatherReportNotFoundException;

    ForecastWeatherReport getWeatherForecast(ForecastWeatherRequest request) throws WeatherReportNotFoundException;

    JSONObject getGeoCoordsOfRequestedCity(JSONObject apiResponse) throws WeatherReportNotFoundException;

    JSONArray getHighestandLowestTempOfEachDayInForecast(JSONArray weatherInfo) throws WeatherReportNotFoundException;

}
