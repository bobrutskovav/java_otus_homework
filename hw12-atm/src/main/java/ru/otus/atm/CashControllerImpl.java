package ru.otus.atm;

import java.util.EnumMap;
import java.util.Map;

public class CashControllerImpl implements CashController {

    private Map<Bill, Integer> cash = new EnumMap<>(Bill.class);

    public CashControllerImpl() {
        for (Bill bill : Bill.values()) {
            cash.put(bill, 0);
        }
    }

    public int getTotalSum() {
        return cash.entrySet().stream().mapToInt((entry) -> {
            Bill bill = entry.getKey();
            int billCount = entry.getValue();
            int denominationBill = bill.getDenomination();
            int total = 0;
            for (int i = 0; i < billCount; i++) {
                total = total + denominationBill;
            }
            return total;
        }).sum();
    }

    public void addBills(Bill... bills) {
        for (Bill bill : bills) {
            int count = cash.get(bill);
            count++;
            cash.put(bill, count);
        }
    }

    public void addBill(Bill bill) {
        int count = cash.get(bill);
        count++;
        cash.put(bill, count);
    }

    public boolean removeBill(Bill bill) {
        int count = cash.get(bill);
        if (count == 0) {
            return false;
        } else {
            count--;
            cash.put(bill, count);
            return true;
        }

    }

    public boolean checkAvailableBill(Bill bill) {
        return cash.get(bill) > 0;
    }
}
