package ru.otus.multiprocess.backend.messaging;

import lombok.extern.slf4j.Slf4j;
import ru.otus.multiprocess.backend.core.dto.UserDto;
import ru.otus.multiprocess.messagesystem.RequestHandler;
import ru.otus.multiprocess.messagesystem.client.CallbackRegistry;
import ru.otus.multiprocess.messagesystem.client.MessageCallback;
import ru.otus.multiprocess.messagesystem.client.ResultDataType;
import ru.otus.multiprocess.messagesystem.message.Message;
import ru.otus.multiprocess.messagesystem.message.MessageHelper;

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
