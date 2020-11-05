package ru.otus.threads;

public class Printer implements Runnable {

    private static final String THREAD_NAME_PATTERN = "Thread %d";
    private final int maxThreads;
    private volatile String nextThreadName;

    public Printer(int maxThreads) {
        nextThreadName = String.format(THREAD_NAME_PATTERN, 1);
        this.maxThreads = maxThreads;
    }


    public synchronized void performPrint(int number) {


        if (!Thread.currentThread().getName().equals(nextThreadName)) {
            while (!nextThreadName.equals(Thread.currentThread().getName())) {
                waitThread();
            }
        }
        printNumber(number);
        resolveNextThreadName();
        this.notifyAll();
    }

    private void resolveNextThreadName() {
        int currentThreadNumber = Integer.parseInt(Thread.currentThread().getName().substring(7));
        nextThreadName = String.format(THREAD_NAME_PATTERN, currentThreadNumber % maxThreads == 0 ? 1 : currentThreadNumber + 1);
    }


    private void waitThread() {
        try {
            // System.out.println(Thread.currentThread().getName() + " wait"); debug sout)
            this.wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(
                    "Thread was interrupted, Failed to complete operation");
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
