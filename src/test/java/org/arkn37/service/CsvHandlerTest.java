package org.arkn37.service;

import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CsvHandlerTest {

    private static final String CSV_FILE = "file.csv";

    @TempDir
    Path tempDir;

    private List<JsonObject> sampleData() {
        List<JsonObject> list = new ArrayList<>();

        JsonObject a = new JsonObject();
        a.addProperty("id", "1");
        a.addProperty("name", "Alice");
        list.add(a);

        JsonObject b = new JsonObject();
        b.addProperty("name", "Bob");
        b.addProperty("email", "bob@example.com");
        list.add(b);

        JsonObject c = new JsonObject();
        c.addProperty("name", "Alice");
        c.addProperty("age", 30);
        list.add(c);

        JsonObject d = new JsonObject();
        d.addProperty("id", 2);
        d.addProperty("name", "Bob");
        d.addProperty("age", 28);
        list.add(d);


        return list;
    }

    @AfterEach
    void cleanup() {
        File file = new File(CSV_FILE);
        if (file.exists()) file.delete();
    }

    @Test
    void testCreateCsv_withValidJsonObjects() throws IOException {
        List<JsonObject> jsonObjects = sampleData();
        CsvHandler.createCsv(jsonObjects);

        assertTrue(Files.exists(Paths.get(CSV_FILE)));

        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            List<String[]> lines = reader.readAll();
            assertEquals(5, lines.size()); // header + 4 rows

            String[] headers = lines.get(0);
            assertTrue(Arrays.asList(headers).contains("id"));
            assertTrue(Arrays.asList(headers).contains("name"));
            assertTrue(Arrays.asList(headers).contains("email"));
            assertTrue(Arrays.asList(headers).contains("age"));

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

    @Test
    void testCreateCsv_semicolonDelimiter() throws Exception {
        Path out = tempDir.resolve("out.csv");
        List<JsonObject> data = sampleData();

        CsvHandler.createCsv(data, out.toString(), ';');

        List<String> lines = Files.readAllLines(out, StandardCharsets.UTF_8);
        assertEquals(5, lines.size(), "Should write header + 2 rows");

        System.out.println(lines.get(1));
        System.out.println(lines.get(2));

        assertEquals("id;name;email;age", lines.get(0));
        assertEquals("1;Alice;;", lines.get(1));
        assertEquals(";Bob;bob@example.com;", lines.get(2));
    }

    @Test
    void testCreateCsvThrowsWhenDestination_isNotCsvExtension() throws Exception {
        Path out = tempDir.resolve("data.txt");
        List<JsonObject> data = sampleData();

        IOException ex = assertThrows(IOException.class, () ->
                CsvHandler.createCsv(data, out.toString(), ','));

        assertTrue(ex.getMessage().contains("Destination extension is not .csv o .CSV"));
    }

    @Test
    void testCreateCsvAllowsUppercase_csvExtension() throws Exception {
        Path out = tempDir.resolve("DATA.CSV");
        List<JsonObject> data = sampleData();

        CsvHandler.createCsv(data, out.toString(), '\t');

        assertTrue(Files.exists(out));
        List<String> lines = Files.readAllLines(out, StandardCharsets.UTF_8);
        assertFalse(lines.isEmpty());
        assertEquals("id\tname\temail\tage", lines.get(0));
    }
}