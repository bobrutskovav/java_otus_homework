package ru.otus.threads;

public class Printer implements Runnable {

    private volatile Integer currentCount = 0;


    public synchronized void printNumber(int number) {
        System.out.println(String.format("%s : %d", Thread.currentThread().getName(), number));
        currentCount++;
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            printNumber(i);
        }
    }

    public synchronized Integer getCurrentCount() {
        return currentCount;
    }

    public synchronized void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    public synchronized void resetCount() {
        currentCount = 0;
        this.notifyAll();
    }
}
