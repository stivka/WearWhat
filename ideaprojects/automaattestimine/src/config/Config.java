package config;

import java.util.List;

import inputOutput.ReaderWriter;

public class Config {

    private String apiKey;
    private String cityName;
    private String countyCode;

    public Config() {
        try {
            List configData = ReaderWriter.readLineFromFileAndSplitByComma("/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/input.txt");
            this.cityName = configData.get(0).toString().trim();
            this.countyCode = configData.get(1).toString().trim();
            this.apiKey = configData.get(2).toString().trim();
            this.mock = true;
            System.out.println(configData);
        } catch (Exception e) {
            System.out.println("Failed to read from input.txt, settings default values.");
            this.countyCode = "Tallinn";
            this.countyCode = "EE";
            this.apiKey = "9940b51e4a672605dc060c11655257b3";
            this.mock = true;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public boolean isMock() {
        return mock;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }

    private boolean mock;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
