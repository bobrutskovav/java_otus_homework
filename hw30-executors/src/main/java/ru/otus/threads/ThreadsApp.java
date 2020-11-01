package ru.otus.threads;

public class ThreadsApp {
    public static void main(String[] args) throws InterruptedException {


        Printer printer = new Printer();
        Thread thread1 = new Thread(printer);
        thread1.setName("Thread 1");
        Thread thread2 = new Thread(printer);
        thread2.setName("Thread 2");


        thread1.start();
        thread2.start();


    }
}
