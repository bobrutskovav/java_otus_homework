package ru.otus.threads;

public class Printer implements Runnable {

    private volatile boolean firstThreadWasHere = false;
    private volatile boolean secondThreadWasHere = false;


    public synchronized void performPrint(int number) {
        if (Thread.currentThread().getName().endsWith(" 2")) {
            while (!firstThreadWasHere) {
                waitThread();
            }
            printNumber(number);
            secondThreadWasHere = true;
            this.notifyAll();
        }
        if (Thread.currentThread().getName().endsWith(" 1")) {
            printNumber(number);
            firstThreadWasHere = true;
            this.notifyAll();
            while (!secondThreadWasHere) {
                waitThread();
            }
        }

    }


    private void waitThread() {
        try {
            System.out.println(Thread.currentThread().getName() + " wait");
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void printNumber(int number) {
        System.out.println(String.format("%s : %d", Thread.currentThread().getName(), number));
    }


    public synchronized void notifyThreads() {
        firstThreadWasHere = false;
        secondThreadWasHere = false;
        this.notifyAll();
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            performPrint(i);
            firstThreadWasHere = false;
            secondThreadWasHere = false;
        }
    }
}
