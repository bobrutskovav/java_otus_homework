package ru.otus.processor;

import ru.otus.Message;

public class ProcessorReplaceField11andField13 implements Processor {
    @Override
    public Message process(Message message) {
        String field11 = message.getField11();
        String field13 = message.getField13();
        return message.toBuilder().field11(field13).field13(field11).build();
    }
}
