package ru.otus.ms.app.messaging;

import lombok.extern.slf4j.Slf4j;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.ms.app.core.dto.UserDto;

import java.util.Optional;

@Slf4j
public class ResponseHandler implements RequestHandler<UserDto> {

    private final CallbackRegistry callbackRegistry;

    public ResponseHandler(CallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            MessageCallback<? extends ResultDataType> callback = callbackRegistry.getAndRemove(msg.getCallbackId());
            if (callback != null) {
                callback.accept(MessageHelper.getPayload(msg));
            } else {
                log.error("callback for Id:{} not found", msg.getCallbackId());
            }
        } catch (Exception ex) {
            log.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}
