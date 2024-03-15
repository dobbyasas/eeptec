package com.tofi;

import java.util.Scanner;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class App {
    static HashMap<String, String[]> people = new HashMap<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while(true) {
            System.out.println("1. Add person");
            System.out.println("2. Remove person");
            System.out.println("3. Find person");
            System.out.println("4. End");
            System.out.print("Choose action: ");
            String choice = input.nextLine();

            if ("4".equals(choice)) {
                System.out.println("Ending...");
                break;
            }

            switch (choice) {
                case "1":
                    addPerson();
                    break;
                case "2":
                    removePerson();
                    break;
                case "3":
                    findPerson();
                    break;   
                default:
                    System.out.println("Invalid command. Try again: ");
                    continue;
            }

            if (!promptContinue()) {
                System.out.println("Ending...");
                break;
            }
        }
    }

    // Ask if we wish to continue
    public static boolean promptContinue() {
        System.out.println("Continue? (y/n)");
        String response = input.nextLine();
        return response.equalsIgnoreCase("y");
    }

    // Adding new person
    public static void addPerson() {
        System.out.println("Name:");
        String name = input.nextLine();
        System.out.println("Surname:");
        String surname = input.nextLine();
        System.out.println("Birth number (YYMMDDXXXX or YYMMDD/XXXX):");
        String id = getValidBirthNumber();

        if(name.equals("") || surname.equals("")) {
            System.out.println("Name and surname cant be empty!");
            return;
        }

        if(people.containsKey(id)) {
            System.out.println("This birth number is already used");
        } else {
            people.put(id, new String[]{name, surname, id});
            System.out.println("Added.");
        }
    }

    // Removing person
    public static void removePerson() {
        System.out.println("Birth number of person you wish to delete:");
        String id = getValidBirthNumber();

        if(people.remove(id) != null) {
            System.out.println("Removed.");
        } else {
            System.out.println("This person does not exists.");
        }
    }

    // Finding person
    public static void findPerson() {
        System.out.println("Birth number:");
        String id = getValidBirthNumber();

        if(people.containsKey(id)) {
            String[] person = people.get(id);
            System.out.println("Name: " + person[0] + ", Surname: " + person[1] + ", Birth numberr: " + person[2]);
            System.out.println("Age: " + calculateAge(person[2]) + " let");
        } else {
            System.out.println("This person does not exists");
        }
    }

    // Validating input for birth number
    public static String getValidBirthNumber() {
        while (true) {
            String birthNumber = input.nextLine();
            if (birthNumber.matches("\\d{10}")) {
                // Addung "/" into birth number, if not there
                birthNumber = birthNumber.substring(0, 6) + "/" + birthNumber.substring(6);
            }

            if (!birthNumber.matches("\\d{6}/\\d{4}")) {
                System.out.println("Invalid format (use YYMMDDXXXX or YYMMDD/XXXX): ");
                continue;
            }

            String birthDatePart = birthNumber.substring(0, 6);
            try {
                LocalDate.parse(birthDatePart, DateTimeFormatter.ofPattern("yyMMdd"));
                // Validating date
                return birthNumber;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date: ");
            }
        }
    }

    public static int calculateAge(String birthNumber) {
        try {
            String birthDate = birthNumber.substring(0, 6);
            LocalDate birth = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyMMdd"));
            return LocalDate.now().getYear() - birth.getYear();
        } catch (DateTimeParseException e) {
            System.out.println("Unable to calculate age.");
            return -1;
        }
    }
}
