package com.pluralsight;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class FinancialTracker {


    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
        text colors
       ------------------------------------------------------------------ */
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[93m";
    private static final String BLUE = "\u001B[34m";
    private static final String BLUE2 = "\u001B[94m";
/*
    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();

        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(BLUE2 + "\nWelcome to TransactionApp\n" + RESET);
            System.out.println(BLUE2 + "==== Home Screen ====" + RESET);
            System.out.println(BLUE + "Choose an option:" + RESET);
            System.out.println(GREEN + "D) Add Deposit" + RESET);
            System.out.println(GREEN + "P) Make Payment (Debit)" + RESET);
            System.out.println(GREEN + "L) Ledger" + RESET);
            System.out.println(RED + "X) Exit" + RESET);

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }

        saveTransactions(FILE_NAME);
        scanner.close();
    }


    public static void loadTransactions(String fileName) {

        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 5) continue;

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim(), DATE_FMT);
                    LocalTime time = LocalTime.parse(parts[1].trim(), TIME_FMT);
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4].trim());

                    transactions.add(new Transaction(date, time, description, vendor, amount));
                } catch (DateTimeException | NumberFormatException e) {
                    System.out.println(BLUE2 + "Invalid Transaction: " + line + " [" + e.getMessage() + "]" + RESET);
                }
            }
        } catch (IOException e) {
            System.out.println(RED + "Error Reading File: " + e.getMessage() + RESET);
        }
    }

    private static void saveTransactions(String fileName) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Transaction t : transactions) {
                writer.write(String.format("%s|%s|%s|%s|%.2f",
                        t.getDate().format(DATE_FMT),
                        t.getTime().format(TIME_FMT),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(RED + "Error Saving File: " + e.getMessage() + RESET);
        }
    }

    private static void addDeposit(Scanner scanner) {

        try {

            System.out.print(BLUE2 + "Enter Date (" + DATE_PATTERN + "): " + RESET);
            LocalDate date = LocalDate.parse(scanner.nextLine().trim(), DATE_FMT);


            System.out.print(BLUE2 + "Enter Time (" + TIME_PATTERN + "): " + RESET);
            LocalTime time = LocalTime.parse(scanner.nextLine().trim(), TIME_FMT);


            System.out.print(BLUE + "Enter Description: " + RESET);
            String description = scanner.nextLine().trim();


            System.out.print(GREEN + "Enter Vendor: " + RESET);
            String vendor = scanner.nextLine().trim();


            System.out.print(GREEN + "Enter Amount: " + RESET);
            double amount = Double.parseDouble(scanner.nextLine().trim());

            if (amount <= 0) {
                System.out.println(BLUE2 + "Deposit Must be Positive." + RESET);
                return;
            }

            Transaction t = new Transaction(date, time, description, vendor, amount);
            transactions.add(t);
            System.out.println(YELLOW + "Deposited Successfully:" + RESET);
            System.out.println(t);

        } catch (DateTimeException e) {
            System.out.println(RED + "Invalid Date or Time Format. Please Try Again." + RESET);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Invalid Amount. Please Enter a Number." + RESET);
        } catch (Exception e) {
            System.out.println(RED + "Error Depositing: " + e.getMessage() + RESET);
        }
    }

    private static void addPayment(Scanner scanner) {

        try {

            System.out.print("Enter Date (" + DATE_PATTERN + "): ");
            LocalDate date = LocalDate.parse(scanner.nextLine().trim(), DATE_FMT);


            System.out.print("Enter Time (" + TIME_PATTERN + "): ");
            LocalTime time = LocalTime.parse(scanner.nextLine().trim(), TIME_FMT);


            System.out.print("Enter Description: ");
            String description = scanner.nextLine().trim();


            System.out.print("Enter Vendor: ");
            String vendor = scanner.nextLine().trim();


            System.out.print("Enter Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            if (amount <= 0) {
                System.out.println("payment Must be Positive.");
                return;
            }

            amount = -amount;

            Transaction t = new Transaction(date, time, description, vendor, amount);
            transactions.add(t);
            System.out.println("Payment Successful:");
            System.out.println(t);

        } catch (DateTimeException e) {
            System.out.println("Invalid Date or Time Format. Please Try Again.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Amount. Please Enter a Number.");
        } catch (Exception e) {
            System.out.println("Error Adding Payment: " + e.getMessage());
        }
    }


    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }


    private static void displayLedger() {

        if (transactions.isEmpty()) {
            System.out.println("Transactions Unavailable.");
            return;
        }

        System.out.println("All Transactions (Newest First):");
        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static void displayDeposits() {

        boolean made = false;
        System.out.println("Deposits (Newest First):");
        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------------------------");

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) {
                System.out.printf("%s | %s | %-22s | %-12s | %8.2f%n",
                        t.getDate().format(DATE_FMT),
                        t.getTime().format(TIME_FMT),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                made = true;
            }
        }

        if (!made) {
            System.out.println("No Deposits Established.");
        }
    }

    private static void displayPayments() {

        boolean made = false;
        System.out.println("Payments (Newest First):");
        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------------------------");

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) {
                System.out.printf("%s | %s | %-22s | %-12s | %8.2f%n",
                        t.getDate().format(DATE_FMT),
                        t.getTime().format(TIME_FMT),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                made = true;
            }
        }

        if (!made) {
            System.out.println("No Payments Made.");
        }
    }


    private static void reportsMenu(Scanner scanner) {

        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {

                case "1" -> monthToDateReport();
                case "2" -> previousMonthReport();
                case "3" -> yearToDateReport();
                case "4" -> previousYearReport();
                case "5" -> searchByVendor(scanner);
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid Option");
            }
        }
    }

    private static void monthToDateReport() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        filterTransactionsByDate(start, now);
    }

    private static void previousMonthReport() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusMonths(1).withDayOfMonth(1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        filterTransactionsByDate(start, end);
    }

    private static void yearToDateReport() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfYear(1);
        filterTransactionsByDate(start, now);
    }

    private static void previousYearReport() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusYears(1).withDayOfYear(1);
        LocalDate end = start.withDayOfYear(start.lengthOfYear());
        filterTransactionsByDate(start, end);
    }

    private static void searchByVendor(Scanner scanner) {
        System.out.print("Enter Vendor Name to Search: ");
        String vendor = scanner.nextLine().trim();
        filterTransactionsByVendor(vendor);

    }


    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {

        System.out.printf("%nTransactions from %s to %s:%n", start, end);
        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------------------------");

        boolean found = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if ((date.isEqual(start) || date.isAfter(start)) && (date.isEqual(end) || date.isBefore(end))) {
                System.out.printf("%s | %s | %-22s | %-12s | %8.2f%n",
                        date.format(DATE_FMT),
                        t.getTime().format(TIME_FMT),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                found = true;
            }
        }
        if (!found) System.out.println("No Transactions Found for These Dates.");
    }


    private static void filterTransactionsByVendor(String vendor) {
        System.out.printf("%nTransactions for vendor: %s%n", vendor);

        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------------------------");

        boolean found = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                System.out.printf("%s | %s | %-22s | %-12s | %8.2f%n",
                        t.getDate().format(DATE_FMT),
                        t.getTime().format(TIME_FMT),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                found = true;

            }
        }
        if (!found) System.out.println("No Transactions Found From Vendor: " + vendor);
    }


    private static void customSearch(Scanner scanner) {


        System.out.print("Start Date [yyyy-MM-dd]: ");
        String startInput = scanner.nextLine().trim();
        LocalDate startDate = null;
        if (!startInput.isEmpty()) {
            try {
                startDate = LocalDate.parse(startInput, DATE_FMT);
            } catch (Exception e) {
                System.out.println("Invalid Format for Start Date.");
                startDate = null;
            }
        }

        System.out.print("End Date [yyyy-MM-dd]: ");
        String endInput = scanner.nextLine().trim();
        LocalDate endDate = null;
        if (!endInput.isEmpty()) {
            try {
                endDate = LocalDate.parse(endInput, DATE_FMT);
            } catch (Exception e) {
                System.out.println("Invalid Format for End Date.");
                endDate = null;
            }
        }

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) description = null;

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine().trim();
        if (vendor.isEmpty()) vendor = null;

        System.out.print("Amount: ");
        String amountInput = scanner.nextLine().trim();
        Double amount = null;
        if (!amountInput.isEmpty()) {
            try {
                amount = Double.parseDouble(amountInput);
            } catch (Exception e) {
                System.out.println("Invalid Amount.");
                amount = null;
            }
        }


        System.out.println("\nMatching Transactions:");
        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------------------------");

        boolean match = false;


        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            if ((startDate != null && t.getDate().isBefore(startDate)) ||
                    (endDate != null && t.getDate().isAfter(endDate)) ||
                    (description != null && !t.getDescription().toLowerCase().contains(description.toLowerCase())) ||
                    (vendor != null && !t.getVendor().equalsIgnoreCase(vendor)) ||
                    (amount != null && t.getAmount() != amount)) {
                continue;
            }

            System.out.printf("%s | %s | %-22s | %-12s | %8.2f%n",
                    t.getDate().format(DATE_FMT),
                    t.getTime().format(TIME_FMT),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
            match = true;
        }

        if (!match) System.out.println("No Transaction Matches Your Criteria.");
    }


    private static LocalDate parseDate(String input) {

        if (input.isEmpty()) return null;
        try {
            return LocalDate.parse(input, DATE_FMT);
        } catch (DateTimeException e) {
            System.out.println("Invalid Date Format: " + input);

            return null;
        }
    }

    private static Double parseDouble(String input) {

        if (input.isEmpty()) return null;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + input);
            return null;
        }
    }
            private static void createAndShowGUI() {
                // Create the main application window
                JFrame frame = new JFrame("Personal Financial Tracker");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
                frame.setSize(800, 600); // Set window size
                frame.setLocationRelativeTo(null); // Center the window on the screen

                TitleScreen titleScreen = new TitleScreen(frame);
                frame.add(titleScreen);

                frame.setVisible(true);
            }
        }
    }
}

