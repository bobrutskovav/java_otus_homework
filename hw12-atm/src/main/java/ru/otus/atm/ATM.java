package ru.otus.atm;

import java.util.ArrayList;
import java.util.List;

public class ATM {


    private CashController cashController;

    public ATM() {
        cashController = new CashController();
    }

    public ATM(Bill... bills) {
        this();
        cashController.addBills(bills);
    }


    public List<Bill> getMoney(int sum) {
        if (!checkBalance(sum)) {
            throw new IllegalArgumentException(String.format("Not enough cash in ATM for sum", sum));
        }

        if (!checkRequiredSum(sum, Bill.getMinimalBill())) {
            throw new IllegalArgumentException(String.format("Can't give a money for sum %d", sum));
        }
        int remainder = sum;
        List<Bill> monies = new ArrayList<>();
        for (Bill bill : Bill.getSortedBillList()) {
            while (cashController.checkAvailableBill(bill)) {
                remainder = remainder - bill.getDenomination();
                monies.add(bill);
                cashController.removeBill(bill);
            }

            if (remainder == 0) {
                break;
            }
        }
        return monies;
    }


    public int getBalance() {
        return cashController.getBalance();
    }


    private boolean checkBalance(int sum) {
        int currentBalance = cashController.getBalance();
        return sum <= currentBalance;
    }

    private boolean checkRequiredSum(int sum, Bill minimalBill) {
        return sum % minimalBill.getDenomination() == 0;
    }


}
