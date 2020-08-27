package ru.otus.atm;

public interface CashController {

    int getTotalSum();

    void addBill(Bill bill);

    boolean removeBill(Bill bill);

    boolean checkAvailableBill(Bill bill);
}
