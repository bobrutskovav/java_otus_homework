package ru.otus.atm;

import java.util.List;

public interface Atm {

    List<Bill> getMoney(int sum);

    int getBalance();

    void addBills(Bill... bills);
}
