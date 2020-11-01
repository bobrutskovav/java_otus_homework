package ru.otus.threads;

public class Printer implements Runnable {

    private volatile boolean isFirstThreadRun = false;


    public synchronized void performPrint(int number) {
        if (!Thread.currentThread().getName().endsWith(" 2")) {
            while (isFirstThreadRun) {
                waitThread();
            }
        }
        if (!Thread.currentThread().getName().endsWith(" 1")) {
            while (!isFirstThreadRun) {
                waitThread();
            }
        }
        printNumber(number);
        isFirstThreadRun = !isFirstThreadRun;
        this.notifyAll();

    }


    private void waitThread() {
        try {
            // System.out.println(Thread.currentThread().getName() + " wait"); debug sout)
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void printNumber(int number) {
        System.out.println(String.format("%s : %d", Thread.currentThread().getName(), number));
    }


    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            performPrint(i);
        }
        for (int i = 10; i >= 0; i--) {
            performPrint(i);
        }
    }
}
