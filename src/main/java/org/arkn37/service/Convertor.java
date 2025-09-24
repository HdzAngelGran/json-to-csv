package org.arkn37.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Convertor {

    private Convertor() {
        throw new IllegalStateException("Utility class");
    }
    
    public static String jsonToCsv(String filepath, String destinationPath , char delimiter) {
        try {
            Optional<List<JsonObject>> jsonObjects = JsonHandler.getJsonObject(filepath);
            CsvHandler.createCsv(jsonObjects.get(), destinationPath, delimiter);
            return "file.csv";
        } catch (JsonSyntaxException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            return "";
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return "";
        }
    }

}
