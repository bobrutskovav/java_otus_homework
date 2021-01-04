package ru.otus.multiprocess.messagesystem.message;


import ru.otus.multiprocess.messagesystem.client.CallbackId;
import ru.otus.multiprocess.messagesystem.client.ResultDataType;

import java.util.UUID;

public class MessageBuilder {
    private static final Message VOID_MESSAGE =
            new Message(new MessageId(UUID.randomUUID().toString()), null, 0, null,
                    null, "voidTechnicalMessage", new byte[1], null);

    private MessageBuilder() {
    }

    public static Message getVoidMessage() {
        return VOID_MESSAGE;
    }

    public static <T extends ResultDataType> Message buildMessage(String from, int portFrom, String to, MessageId sourceMessageId,
                                                                  T data, MessageType msgType) {
        return buildMessage(from, portFrom, to, sourceMessageId, data, msgType, null);
    }

    public static <T extends ResultDataType> Message buildReplyMessage(MessageType messageType, Message message, T data) {
        return buildMessage(message.getTo(), message.getPortFrom(), message.getFrom(), message.getId(), data,
                messageType, message.getCallbackId());
    }

    private static <T extends ResultDataType> Message buildMessage(String from, int portFrom, String to, MessageId sourceMessageId,
                                                                   T data, MessageType msgType, CallbackId callbackId) {
        String id = UUID.randomUUID().toString();
        return new Message(new MessageId(id), from, portFrom, to, sourceMessageId, msgType.getName(),
                Serializers.serialize(data), callbackId == null ? new CallbackId(id) : callbackId);
    }
}
