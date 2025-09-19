package org.arkn37;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to JSON to CSV convertor");
        int action;
        do {
            System.out.println("""
                    ------------ Menu ------------
                    1) Read Json File content
                    9) Exit""");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Not a valid input. Try again: ");
                scanner.next();
            }

            action = scanner.nextInt();
            switch (action) {
                case 1 -> System.out.println("---> Read Json File content\n");
                case 9 -> System.out.println("""
                    --------------------------------
                      Thanks for your visit
                      See you soon ;)
                    """);
                default -> {
                    System.out.println("Number " + action + " is not a valid option");
                    System.out.println("Try again");
                }
            }

        } while (action != 9);

        scanner.close();
    }
}