package org.arkn37.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CsvHandler {

    private CsvHandler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create csv.
     *
     * @param jsonObject the json object
     * @throws IOException the io exception
     */
    public static void createCsv(List<JsonObject> jsonObject) throws IOException {

        Set<String> allKeys = new LinkedHashSet<>();
        for (JsonElement element : jsonObject) {
            JsonObject currentObject = element.getAsJsonObject();
            allKeys.addAll(currentObject.keySet());
        }

        String[] headers = allKeys.toArray(new String[0]);

        try (CSVWriter writer = new CSVWriter(new FileWriter("file.csv"))) {
            writer.writeNext(headers);

            for (JsonElement element : jsonObject) {
                List<String> values = mapValuesByHeader(element, headers);
                writer.writeNext(values.toArray(new String[0]));
            }
        }
    }

    public static void createCsv(List<JsonObject> jsonObject, String destinationPath, char delimiter) throws IOException {
        
        if (!destinationPath.endsWith(".csv") && !destinationPath.endsWith(".CSV"))
            throw new IOException("Destination extension is not .csv o .CSV");

        Set<String> allKeys = new LinkedHashSet<>();
        for (JsonElement element : jsonObject) {
            JsonObject currentObject = element.getAsJsonObject();
            allKeys.addAll(currentObject.keySet());
        }

        String[] headers = allKeys.toArray(new String[0]);

        try (CSVWriter writer = new CSVWriter(new FileWriter(destinationPath), delimiter, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {

            writer.writeNext(headers);

            for (JsonElement element : jsonObject) {
                List<String> values = mapValuesByHeader(element, headers);
                writer.writeNext(values.toArray(new String[0]));
            }
        }
    }

    /**
     * Create csv.
     *
     * @param element the json element
     * @param headers the headers
     * @return the list
     */
    private static List<String> mapValuesByHeader(JsonElement element, String[] headers) {
        JsonObject currentObject = element.getAsJsonObject();
        List<String> values = new ArrayList<>();
        for (String header : headers) {
            JsonElement valueElement = currentObject.get(header);

            if (valueElement == null || valueElement.isJsonNull()) {
                values.add("");
            } else if (valueElement.isJsonArray()) {
                JsonArray jsonArrayValue = valueElement.getAsJsonArray();
                List<String> arrayValues = new ArrayList<>();
                for (JsonElement arrayElement : jsonArrayValue) {
                    arrayValues.add(arrayElement.getAsString());
                }
                values.add(String.join(", ", arrayValues));
            } else if (valueElement.isJsonObject())
                values.add(valueElement.getAsJsonObject().toString());
            else values.add(valueElement.getAsString());
        }
        return values;
    }

    /**
     * Generate csv from object string.
     *
     * @param jsonString the json string
     * @return the string
     */
    public static String generateCsvFromObject(String jsonString) {
        if (jsonString.isEmpty()) return "JSON input cannot be empty.";

        if (jsonString.charAt(0) == '{') jsonString = "[" + jsonString + "]";

        try {
            List<JsonObject> objectList = JsonHandler.toListObject(jsonString);

            createCsv(objectList);

            return "CSV file created successfully!";
        } catch (JsonSyntaxException e) {
            return "Invalid JSON format: " + e.getMessage();
        } catch (IOException e) {
            return "Error while writing CSV: " + e.getMessage();
        }
    }

}
