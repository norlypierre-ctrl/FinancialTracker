package com.pluralsight;

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


    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

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
                } catch (DateTimeException |NumberFormatException e) {
                    System.out.println("Invalid Transaction: " + line + " ["+ e.getMessage() + "]");
                }
            }
        } catch (IOException e) {
            System.out.println("Error Reading File: " + e.getMessage());
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
            System.out.println("Error Saving File: " + e.getMessage());
        }
    }

    private static void addDeposit(Scanner scanner) {

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
                System.out.println("Deposit Must be Positive.");
                return;
            }

            Transaction t = new Transaction(date, time, description, vendor, amount);
            transactions.add(t);
            System.out.println("Deposited Successfully:");
            System.out.println(t);

        } catch (DateTimeException e) {
            System.out.println("Invalid Date or Time Format. Please Try Again.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Amount. Please Enter a Number.");
        } catch (Exception e) {
            System.out.println("Error Depositing: " + e.getMessage());
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
                    System.out.println("Deposit Must be Positive.");
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

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            System.out.printf("%s | %s | %-22s | %-12s | %8.2f%n",
                    t.getDate().format(DATE_FMT),
                    t.getTime().format(TIME_FMT),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
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
            System.out.print("Enter vendor name to search: ");
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
        if (!found) System.out.println("No Transactions Found for These Dates .");
    }


    private static void filterTransactionsByVendor(String vendor) {


    }

    private static void customSearch(Scanner scanner) {

    }


    private static LocalDate parseDate(String s) {

        return null;
    }

    private static Double parseDouble(String s) {

        return null;
    }
}
