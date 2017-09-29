package testHelpers;

import org.json.JSONObject;

public class Validator {

    public static void validateGeoCoordinates(JSONObject geoCoordinates) throws Exception {
        double latMax = 90;
        double latMin = -90;
        double lngMax = 180;
        double lngMin = -180;
        if(geoCoordinates == null || geoCoordinates.has("lat") == false || geoCoordinates.has("lon") == false)
            throw new Exception("Geo-coordinates must be specified");
        if(geoCoordinates.getDouble("lat")>latMax || geoCoordinates.getDouble("lat")<latMin)
            throw new Exception("Geo-coordinates latitude is not valid");
        if(geoCoordinates.getDouble("lon")>lngMax ||  geoCoordinates.getDouble("lon")<lngMin)
            throw new Exception("Geo-coordinates longitude is not valid");

    }

    public static void validateTemperature(Double temperature) throws Exception{
        // Units in Kelvin
        double maxTemp = 343.15; // 70 celsius
        double minTemp = -343.15;
        double tempCurrent = temperature;


        if (tempCurrent == 0.00)
            throw new Exception("Temperature is NIL: " + temperature);
        if (tempCurrent<minTemp)
            throw new Exception("Temperature cannot be lower than: " + minTemp + ", report shows: " + tempCurrent);
        if (tempCurrent>maxTemp)
            throw new Exception("Temperature cannot be higher than: " + maxTemp + ", report shows: " + tempCurrent);
    }

}

