package ru.otus.listener;

import ru.otus.Message;

import java.util.HashMap;
import java.util.Map;

public class HistoryListener implements Listener {

    private final Map<Message, Message> historyMap = new HashMap<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        historyMap.put(oldMsg, newMsg);
    }
}
