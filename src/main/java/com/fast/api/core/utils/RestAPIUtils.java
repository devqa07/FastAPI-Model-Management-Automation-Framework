package com.fast.api.core.utils;

import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.*;

public class RestAPIUtils {
    static String env;
    private static final Random RANDOM = new Random();

    //Get the environment details to run the tests
    public static String getEnvToRun() {
        String url = null;
        if (System.getProperty("environment") != null) {
            env = System.getProperty("environment");
        } else {
            env = ConfigHelper.returnPropVal("config", "environment");
        }
        switch (env.toLowerCase()) {
            case "qa":
                url = ConfigHelper.GetBaseUrl("qaUrl");
                break;
            case "dev":
                url = ConfigHelper.GetBaseUrl("devUrl");
                break;
            default:
                System.out.println("Invalid Environment Value");
        }
        return url;
    }

    public static String generateModelName() {
        String prefix = "My Model";
        Integer randNumber = generateRandomNumber(7);
        String modelName = prefix + randNumber;
        return modelName;
    }

    public static String generateVersionName() {
        String prefix = "My Version";
        Integer randNumber = generateRandomNumber(7);
        String modelName = prefix + randNumber;
        return modelName;
    }


    public static Integer generateRandomNumber(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of digits must be positive.");
        }
        int lowerBound = (int) Math.pow(10, n - 1);
        int upperBound = (int) Math.pow(10, n) - 1;

        return lowerBound + RANDOM.nextInt(upperBound - lowerBound + 1);
    }

    public static String getSpecificJsonAttribute(Response response, String key) {
        JSONObject json = new JSONObject(response.asString());
        return json.getString(key);
    }
}