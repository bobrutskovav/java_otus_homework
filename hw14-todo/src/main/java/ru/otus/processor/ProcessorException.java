package ru.otus.processor;

import ru.otus.Message;

import java.time.LocalDateTime;

public class ProcessorException implements Processor {
    @Override
    public Message process(Message message) {
        if (LocalDateTime.now().getSecond() % 2 == 0) {
            throw new RuntimeException("Even Second!");
        }
        return message;
    }
}
