package org.arkn37.service;

import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.*;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CsvHandlerTest {

    private static final String CSV_FILE = "file.csv";

    @AfterEach
    void cleanup() {
        File file = new File(CSV_FILE);
        if (file.exists()) file.delete();
    }

    @Test
    void testCreateCsv_withValidJsonObjects() throws IOException {
        List<JsonObject> jsonObjects = new ArrayList<>();

        JsonObject obj1 = new JsonObject();
        obj1.addProperty("name", "Alice");
        obj1.addProperty("age", 30);

        JsonObject obj2 = new JsonObject();
        obj2.addProperty("name", "Bob");
        obj2.addProperty("age", 25);
        obj2.addProperty("city", "Monterrey");

        jsonObjects.add(obj1);
        jsonObjects.add(obj2);

        CsvHandler.createCsv(jsonObjects);

        assertTrue(Files.exists(Paths.get(CSV_FILE)));

        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            List<String[]> lines = reader.readAll();
            assertEquals(3, lines.size()); // header + 2 rows

            String[] headers = lines.get(0);
            assertTrue(Arrays.asList(headers).contains("name"));
            assertTrue(Arrays.asList(headers).contains("age"));
            assertTrue(Arrays.asList(headers).contains("city"));

            String[] row1 = lines.get(1);
            String[] row2 = lines.get(2);

            assertEquals("Alice", row1[Arrays.asList(headers).indexOf("name")]);
            assertEquals("Bob", row2[Arrays.asList(headers).indexOf("name")]);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testMapValuesByHeader_handlesNullAndArray() throws Exception {
        String[] headers = {"name", "tags", "details"};

        JsonObject obj = new JsonObject();
        obj.addProperty("name", "Test");

        JsonArray tags = new JsonArray();
        tags.add("tag1");
        tags.add("tag2");
        obj.add("tags", tags);

        JsonObject details = new JsonObject();
        details.addProperty("info", "value");
        obj.add("details", details);

        Method method = CsvHandler.class.getDeclaredMethod("mapValuesByHeader", JsonElement.class, String[].class);
        method.setAccessible(true);

        List<String> values = (List<String>) method.invoke(null, obj, headers);

        assertEquals("Test", values.get(0));
        assertEquals("tag1, tag2", values.get(1));
        assertTrue(values.get(2).contains("info"));
    }
}