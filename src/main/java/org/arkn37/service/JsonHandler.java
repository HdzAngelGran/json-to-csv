package org.arkn37.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class JsonHandler {

    private JsonHandler() {
        throw new IllegalStateException("Utility class");
    }

    private static String readFile(String filePath) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader jsonFile = new BufferedReader(new FileReader(filePath))) {
            if (!filePath.endsWith(".json") && !filePath.endsWith(".JSON"))
                throw new IOException("File selected is not a JSON file.");

            String line;
            while ((line = jsonFile.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        String stringFile = stringBuilder.toString();
        if (stringFile.charAt(0) == '{') stringFile = "[" + stringFile + "]";

        return stringFile;
    }

    private static List<JsonObject> toListObject(String jsonString) throws JsonSyntaxException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<JsonObject>>() {
        }.getType();
        return gson.fromJson(jsonString, listType);
    }

    public static Optional<List<JsonObject>> getJsonObject(String filePath) {
        if (filePath.isEmpty()) {
            System.out.println("File path cannot be empty. Try again.");
            return Optional.empty();
        }

        try {
            String jsonString = readFile(filePath);
            return Optional.of(toListObject(jsonString));
        } catch (JsonSyntaxException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            return Optional.empty();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return Optional.empty();
        }
    }

}
