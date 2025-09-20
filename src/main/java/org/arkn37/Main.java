package org.arkn37;

import com.google.gson.JsonObject;
import org.arkn37.service.JsonHandler;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-- Welcome to JSON to CSV convertor --");
        int action;
        do {
            System.out.println("""
                    ------------ Menu ------------
                    1) Read Json File content.
                    9) Exit.""");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Not a valid input. Try again: ");
                scanner.next();
            }

            action = scanner.nextInt();
            switch (action) {
                case 1 -> {
                    System.out.println("--------------------------------");
                    Optional<List<JsonObject>> jsonObject = JsonHandler.getJsonObject();
                    jsonObject.ifPresent(jsonObjects ->
                            System.out.printf("""
                                    JsonParse:
                                        %s
                                    """, jsonObjects)
                    );
                }
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