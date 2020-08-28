package ru.otus.atm;

import java.util.ArrayList;
import java.util.List;

public class AtmImpl implements Atm {

    private CashController cashController;

    public AtmImpl(CashController cashController) {
        this.cashController = cashController;
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
        return cashController.getTotalSum();
    }

    @Override
    public void addBills(Bill... bills) {
        for (Bill bill : bills) {
            cashController.addBill(bill);
        }
    }

    private boolean checkBalance(int sum) {
        int currentBalance = cashController.getTotalSum();
        return sum <= currentBalance;
    }

    private boolean checkRequiredSum(int sum, Bill minimalBill) {
        return sum % minimalBill.getDenomination() == 0;
    }


}
