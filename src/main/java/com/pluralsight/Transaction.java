package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    LocalDate date;
    LocalTime time;
    String description;
    String vendor;
    double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }
    public LocalTime getTime() {
        return time;
    }
    public String getDescription() {
        return description;
    }
    public String getVendor() {
        return vendor;
    }
    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        ///truncate the string to fit the width without messing up the column alignment.
        String descriptionTruncate = description.length() > 22 ? description.substring(0, 19) + "..." : description;
        String vendorTruncate = vendor.length() > 12 ? vendor.substring(0, 9) + "..." : vendor;

        String DATE_PATTERN = "yyyy-MM-dd";
        String TIME_PATTERN = "HH:mm:ss";

        DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
        DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);

        return String.format("%s|%s|%-22s|%-12s| $%8.2f%n", date.format(DATE_FMT), time.format(TIME_FMT),
                descriptionTruncate, vendorTruncate, amount);
    }
}
