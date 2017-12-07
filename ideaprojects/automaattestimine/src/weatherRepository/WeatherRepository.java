package weatherRepository;

import config.Config;
import exception.WeatherReportNotFoundException;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import template.*;
import org.json.JSONObject;
import utility.HttpUtility;

public class WeatherRepository implements Weather {

    private HttpUtility httpRequest = new HttpUtility();
    private Config config = new Config();

    @Override
    public CurrentWeatherReport getCurrentWeather(CurrentWeatherRequest request) throws WeatherReportNotFoundException, JSONException {
        try {
            final String response = httpRequest.makeCurrentWeatherApiHttpRequest(request, config.getApiKey());
            JSONObject jsonResp = new JSONObject(response);
            System.out.println(jsonResp);
            return new CurrentWeatherReport(
                    jsonResp.getString("name"),
                    jsonResp.getJSONObject("sys").getString("country"),
                    jsonResp.getJSONObject("coord"),
                    jsonResp.getJSONObject("main").getDouble("temp"),
                    jsonResp.getLong("dt")
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        throw new WeatherReportNotFoundException("Missing interface implementation");
    }

    @Override
    public Integer getWeatherApiResponseStatus(CurrentWeatherRequest request) throws WeatherReportNotFoundException {
        try {
            final String response = httpRequest.makeCurrentWeatherApiHttpRequest(request, config.getApiKey());
            JSONObject jsonResp = new JSONObject(response);
            return response.length() > 0 ? 200 : 500;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new WeatherReportNotFoundException("Missing interface implementation");
    }

    @Override
    public ForecastWeatherReport getWeatherForecast(ForecastWeatherRequest request) throws WeatherReportNotFoundException {
        final String response;
        try {
            response = httpRequest.makeForecastWeatherApiHttpRequest(request, config.getApiKey());
            JSONObject jsonResp = new JSONObject(response);
            return new ForecastWeatherReport(
                    jsonResp.getJSONObject("city").getString("name"),
                    jsonResp.getJSONObject("city").getString("country"),
                    jsonResp.getJSONObject("city").getJSONObject("coord"),
                    jsonResp.getJSONArray("list")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new WeatherReportNotFoundException("Missing interface implementation");
    }

    @Override
    public JSONObject getGeoCoordsOfRequestedCity(JSONObject coords) throws WeatherReportNotFoundException {
        try {
            return coords;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        throw new WeatherReportNotFoundException("Missing interface implementation");
    }

    @Override
    public JSONArray getHighestandLowestTempOfEachDayInForecast(JSONArray weatherInfo) throws WeatherReportNotFoundException {
        try {
            JSONArray lowestAndHighestTemp = new JSONArray();
            for (int i = 0 ; i < weatherInfo.length(); i++) {
                JSONObject weather = weatherInfo.getJSONObject(i);
                JSONObject dayInfo = new JSONObject();
                Long date = weather.getLong("dt");
                Double highestTemp = weather.getJSONObject("main").getDouble("temp_max");
                Double lowestTemp = weather.getJSONObject("main").getDouble("temp_min");
                dayInfo.put("maxTemp", highestTemp);
                dayInfo.put("minTemp", lowestTemp);
                dayInfo.put("date", date);
                lowestAndHighestTemp.put(dayInfo);
            }
            return lowestAndHighestTemp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        throw new WeatherReportNotFoundException("Missing interface implementation");
    }
}


