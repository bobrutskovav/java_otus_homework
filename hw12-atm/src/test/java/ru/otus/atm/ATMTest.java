package ru.otus.atm;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ATMTest {

    private ATM atm;


    @Test
    public void loadMoneyTest() {
        atm = new ATM(Bill.M_500, Bill.M_500, Bill.M_10);
        assertEquals(atm.getBalance(), 1010);
    }

    @Test
    public void emptyAtmTest() {
        atm = new ATM();
        assertEquals(atm.getBalance(), 0);
    }


    @Test
    public void getMoneyTest() {
        atm = new ATM(Bill.M_500, Bill.M_500);
        List<Bill> bills = atm.getMoney(1000);
        Assert.assertEquals(Bill.M_500, bills.get(0));
        Assert.assertEquals(Bill.M_500, bills.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMoneyNegativeTest() {
        atm = new ATM(Bill.M_500, Bill.M_500);
        atm.getMoney(1001);
    }

    @Test
    public void getMoneyRecoverStateTest() {
        atm = new ATM(Bill.M_500, Bill.M_500);
        try {
            atm.getMoney(1001);
        } catch (IllegalArgumentException ex) {
            //ignored
        }
        Assert.assertEquals(atm.getBalance(), 1000);
        List<Bill> bills = atm.getMoney(1000);
        Assert.assertEquals(Bill.M_500, bills.get(0));
        Assert.assertEquals(Bill.M_500, bills.get(1));
        Assert.assertEquals(atm.getBalance(), 0);

    }

    @Test
    public void getZeroSumTest() {
        atm = new ATM();
        Assert.assertTrue(atm.getMoney(0).isEmpty());
    }


}