package org.arkn37.service;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {

    private static final String VALID_JSON_FILE = "test.json";
    private static final String INVALID_JSON_FILE = "invalid.txt";
    private static final String EMPTY_JSON_FILE = "empty.json";

    @BeforeAll
    static void setup() throws IOException {
        // Create a valid JSON file
        try (FileWriter writer = new FileWriter(VALID_JSON_FILE)) {
            writer.write("{\"name\":\"Test\"}");
        }

        // Create an invalid file
        try (FileWriter writer = new FileWriter(INVALID_JSON_FILE)) {
            writer.write("Not a JSON");
        }

        // Create an empty file
        try (FileWriter writer = new FileWriter(EMPTY_JSON_FILE)) {
            writer.write("");
        }
    }

    @AfterAll
    static void cleanup() {
        new File(VALID_JSON_FILE).delete();
        new File(INVALID_JSON_FILE).delete();
        new File(EMPTY_JSON_FILE).delete();
    }

    @Test
    void testReadFile_validJson() throws Exception {
        Method method = JsonHandler.class.getDeclaredMethod("readFile", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, VALID_JSON_FILE);
        assertTrue(result.startsWith("[{"));
    }

    @Test
    void testReadFile_invalidExtension() {
        Exception exception = assertThrows(Exception.class, () -> {
            Method method = JsonHandler.class.getDeclaredMethod("readFile", String.class);
            method.setAccessible(true);
            method.invoke(null, INVALID_JSON_FILE);
        });
        assertTrue(exception.getCause().getMessage().contains("not a JSON file"));
    }

    @Test
    void testGetJsonObject_emptyJsonFile() throws Exception {
        Method method = JsonHandler.class.getDeclaredMethod("readFile", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, EMPTY_JSON_FILE);
        assertEquals("[{\"key\":\"value\"}]", result, "Addition result is incorrect");
    }

    @Test
    void testToListObject_validJson() throws Exception {
        String json = "[{\"key\":\"value\"}]";
        Method method = JsonHandler.class.getDeclaredMethod("toListObject", String.class);
        method.setAccessible(true);
        List<JsonObject> result = (List<JsonObject>) method.invoke(null, json);
        assertEquals(1, result.size());
        assertTrue(result.get(0).has("key"));
    }

    @Test
    void testGetJsonObject_validInput() {
        String input = VALID_JSON_FILE + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Optional<List<JsonObject>> result = JsonHandler.getJsonObject(VALID_JSON_FILE);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void testGetJsonObject_emptyInput() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Optional<List<JsonObject>> result = JsonHandler.getJsonObject(input);
        assertTrue(result.isEmpty());
    }
}