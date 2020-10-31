package ru.otus.threads;

public class Printer implements Runnable {

    private final Integer countLimit;
    private volatile Integer currentCount = 0;


    public Printer(Integer countLimit) {
        this.countLimit = countLimit;
    }

    public synchronized void printNumber(int number) {
        System.out.println(String.format("%s : %d", Thread.currentThread().getName(), number));
        currentCount++;
        if (currentCount.equals(countLimit)) {
            currentCount = 0;
            this.notifyAll();
        } else {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            printNumber(i);
        }
    }
}
