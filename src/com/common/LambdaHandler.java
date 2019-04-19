package com.common;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, String>, String> {

    private static Logger logger;

    public String handleRequest(Map<String,String> inputMap, Context context) {
        logger = LoggerFactory.getLogger(Main.class);
        logger.info("Logger started!");

        // Get Environment Variables
        String url = System.getenv("URL");
        boolean disableSSL = System.getenv("disableSSL").equals("true");

        // Get input variables
//        JsonObject jsonObj = (new JsonParser()).parse(inputJson).getAsJsonObject();
        logger.info("Handler received input: " + inputMap.toString());
        String username = inputMap.get("username");
        String password = inputMap.get("password");

//        URLConnection connection = new URLConnection(url, disableSSL, username, password);
        return "DONE!";
    }
}
