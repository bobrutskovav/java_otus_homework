package ru.otus.atm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum Bill {
    M_5000(5000),
    M_1000(1000),
    M_500(500),
    M_100(100),
    M_50(50),
    M_10(10);


    public static final List<Bill> BILLS;


    static {
        BILLS = Arrays.asList(Bill.values());
        BILLS.sort(Comparator.comparingInt(Bill::getDenomination).reversed());
    }

    private int denomination;

    Bill(int denomination) {
        this.denomination = denomination;
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

}
