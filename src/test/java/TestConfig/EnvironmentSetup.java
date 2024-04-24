package TestConfig;

import io.restassured.RestAssured;

public class EnvironmentSetup {

    public static String env = "BETA";
    // env :{ "DEMO", "BETA","LIVE"}

    public static void setEnvironment(String environment) {
        switch (environment) {
            case "DEMO":
                RestAssured.baseURI = "https://demo-api.nagwa.io";
                RestAssured.basePath = "/v1";
                break;
            case "BETA":
                RestAssured.baseURI = "https://beta-api.nagwa.io";
                RestAssured.basePath = "/v1";
                break;
            case "LIVE":
                RestAssured.baseURI = "https://api.nagwa.io";
                RestAssured.basePath = "/v1";
                break;
            default:
                throw new IllegalArgumentException("Invalid environment: " + environment);
        }
    }


}