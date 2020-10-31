package ru.otus.threads;

public class ThreadsApp {
    public static void main(String[] args) throws InterruptedException {

        int countLimit = 2;
        Printer printer = new Printer();
        Thread thread1 = new Thread(printer);
        thread1.setName("Thread 1");

        Thread thread2 = new Thread(printer);
        thread2.setName("Thread 2");

        Thread thread3 = new Thread(() -> {
            while (true) {
                if (printer.getCurrentCount().equals(countLimit)) {
                    printer.resetCount();
                }
                if (!thread1.isAlive() && !thread2.isAlive()) {
                    return;
                }
            }
        });
        thread3.setName("Watcher");

        thread1.start();
        thread2.start();
        thread3.start();


    }
}
