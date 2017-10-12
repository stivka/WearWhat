package template;

import org.json.JSONObject;

public class WeatherReport {
    public String getCityName() {
        return cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public JSONObject getGeoCoordinates() {
        return geoCoordinates;
    }

    public Double getLongitude() {
        return this.getGeoCoordinates().getDouble("lon");
    }

    public Double getLatitude() {
        return this.getGeoCoordinates().getDouble("lat");
    }

    public final String cityName;
    public final String countryCode;
    public final JSONObject geoCoordinates;


    public WeatherReport(String cityName, String countryCode, JSONObject geoCoordinates) {
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.geoCoordinates = geoCoordinates;
    }
}
