package utility;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import template.CurrentWeatherRequest;
import template.ForecastWeatherRequest;

import java.io.IOException;
import java.net.URL;

public class HttpUtility {

    private OkHttpClient client = new OkHttpClient();

    public String makeCurrentWeatherApiHttpRequest(CurrentWeatherRequest currentWeatherRequest, String config) throws IOException {
        URL urlRequest = new HttpUrl.Builder()
                .scheme("http")
                .host("api.openweathermap.org")
                .addPathSegments("/data/2.5/weather")
                .addQueryParameter("q", currentWeatherRequest.cityName + "," + currentWeatherRequest.countryCode)
                .addQueryParameter("APPID", config)
                .build().url();
        Request request = new Request.Builder()
                .url(urlRequest)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String makeForecastWeatherApiHttpRequest(ForecastWeatherRequest weatherForecastRequest, String config) throws IOException {
        URL urlRequest = new HttpUrl.Builder()
                .scheme("http")
                .host("api.openweathermap.org")
                .addPathSegments("/data/2.5/forecast")
                .addQueryParameter("q", weatherForecastRequest.cityName + "," + weatherForecastRequest.countryCode)
                .addQueryParameter("APPID", config)
                .addQueryParameter("cnt", String.valueOf(weatherForecastRequest.foreCastLength))
                .build().url();
        Request request = new Request.Builder()
                .url(urlRequest)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
