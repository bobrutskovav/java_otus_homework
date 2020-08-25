package ru.otus.atm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum Bill {
    M_5000,
    M_1000,
    M_500,
    M_100,
    M_50,
    M_10;


    public static final List<Bill> BILLS;


    static {
        BILLS = Arrays.asList(Bill.values());
        BILLS.sort(Comparator.comparingInt(Bill::parseDenominationBill).reversed());
    }

    private int denomination;

    Bill() {
        denomination = parseDenominationBill();
    }

    public static Bill getMinimalBill() {
        List<Bill> allBills = getSortedBillList();
        return getSortedBillList().get(allBills.size() - 1);
    }

    public static List<Bill> getSortedBillList() {
        return BILLS;
    }

    public int getDenomination() {
        return denomination;
    }

    private int parseDenominationBill() {
        return Integer.parseInt(this.toString().substring(2));
    }
}
