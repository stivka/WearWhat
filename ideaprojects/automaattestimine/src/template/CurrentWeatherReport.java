package template;

import org.json.JSONObject;

public class CurrentWeatherReport extends WeatherReport{
    public double getTempCurrent() {
        return tempCurrent;
    }

    public final double tempCurrent;

    public Long getReportDateInUnix() {
        return reportDateInUnix;
    }

    public final Long reportDateInUnix;

    public CurrentWeatherReport(String cityName, String countryCode, JSONObject geoCoordinates, double tempCurrent, Long reportDateInUnix) {
        super(cityName, countryCode, geoCoordinates);
        this.tempCurrent = tempCurrent;
        this.reportDateInUnix = reportDateInUnix;
    }

    @Override
    public String toString() {
        return "cityName: " + cityName + ", " + "geo Coords: " + geoCoordinates + ", DateInUnix: " + getReportDateInUnix().toString();
    }
}
