package org.arkn37.service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;

public class Menu {
    static {
        System.out.println("---------------------------------------");
    }
    private final Scanner scanner = new Scanner(System.in);

    private void jsonFileReaderMenu() {
        System.out.println("--------------------------------");
        System.out.print("Provide file path: ");
        String filePath = scanner.next();

        Optional<List<JsonObject>> jsonObject = JsonHandler.getJsonObject(filePath);
        jsonObject.ifPresent(jsonObjects ->
            System.out.printf("""
                    JsonParse:
                        %s
                    """, jsonObjects)
        );

    }

    private void createCsvMenu() {
        System.out.println("--------------------------------");
        System.out.print("Enter JSON object (single line): ");
        String jsonInput = scanner.next();
        System.out.println(CsvHandler.generateCsvFromObject(jsonInput));
    }

    private void convertJsonToCsvMenu() {
        System.out.println("--------------------------------");
        System.out.print("Provide file path: ");
        String filePath = scanner.next();
        System.out.print("Provide destination path and file name (with .csv extension): ");
        String destinationPath = scanner.next();
        System.out.print("""
                Select delimiter (any other key for default ','):
                1) Comma (,)
                2) Semicolon (;)
                3) Tab (\\t)
                4) Space ( )
                Option:\t""");
        while(!scanner.hasNextInt()) {
            System.out.print("Not a valid input. Try again: ");
            scanner.next();
        }
        char delimiter = switch (scanner.nextInt()) {
            case 2 -> ';';
            case 3 -> '\t';
            case 4 -> ' ';
            default -> CSVWriter.DEFAULT_SEPARATOR;
        };

        String resultPath = Convertor.jsonToCsv(filePath, destinationPath, delimiter);
        if (!resultPath.isEmpty())
            System.out.printf("""
                    ┌-------------------------┐
                      CSV file created at: %s
                    └-------------------------┘
                    """, resultPath);
        else
            System.err.println("Failed to create CSV file.");
    }

    public void initMenu() {
        System.out.println("-- Welcome to JSON to CSV convertor --");
        int action;
        do {
            System.out.println("""
                    ------------ Menu ------------
                    1) Read Json File content.
                    2) Create CSV file from Json Object.
                    3) Convert from JSON file to CSV file.
                    9) Exit.""");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Not a valid input. Try again: ");
                scanner.next();
            }

            action = scanner.nextInt();
            switch (action) {
                case 1 -> jsonFileReaderMenu();
                case 2 -> createCsvMenu();
                case 3 -> convertJsonToCsvMenu();
                case 9 -> System.out.println("""
                        --------------------------------
                          Thanks for your visit
                          See you soon ;)
                        """);
                default -> System.out.println("Number " + action + " is not a valid option. Try again.");
            }

        } while (action != 9);

        scanner.close();
    }
}
