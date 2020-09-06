package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.HistoryListener;
import ru.otus.listener.ListenerPrinter;
import ru.otus.processor.*;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 DONE
       2. Сделать процессор, который поменяет местами значения field11 и field13 DONE
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду DONE
       4. Сделать Listener для ведения истории: старое сообщение - новое DONE
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение DONE
         */


        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()),
                new LoggerProcessor(new ProcessorReplaceField11andField13()),
                new LoggerProcessor(new ProcessorException()));

        var complexProcessor = new ComplexProcessor(processors, ex -> System.out.printf("EXCEPTION!!! : %s\n", ex.getMessage()));
        var listenerPrinter = new ListenerPrinter();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("KEK!")
                .field13("LOL!")
                .build();

        System.out.println("Field 11 was : " + message.getField11());
        System.out.println("Field 13 was : " + message.getField13());
        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        System.out.println("Field 11 now is : " + result.getField11());
        System.out.println("Field 13 now is : " + result.getField13());

        complexProcessor.removeListener(listenerPrinter);
    }
}
