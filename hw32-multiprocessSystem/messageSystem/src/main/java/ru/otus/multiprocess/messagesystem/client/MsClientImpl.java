package ru.otus.multiprocess.messagesystem.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.multiprocess.messagesystem.HandlersStore;
import ru.otus.multiprocess.messagesystem.RequestHandler;
import ru.otus.multiprocess.messagesystem.message.Message;
import ru.otus.multiprocess.messagesystem.message.MessageBuilder;
import ru.otus.multiprocess.messagesystem.message.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MsClientImpl implements MsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    private final String name;
    private final String messageServerHost;
    private final int messageServerPort;
    private final int clientPort;
    private final HandlersStore handlersStore;
    private final CallbackRegistry callbackRegistry;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MsClientImpl(String name, String messageServerHost, int messageServerPort, int clientPort, HandlersStore handlersStore,
                        CallbackRegistry callbackRegistry) {
        this.name = name;
        this.messageServerHost = messageServerHost;
        this.messageServerPort = messageServerPort;
        this.clientPort = clientPort;
        this.handlersStore = handlersStore;
        this.callbackRegistry = callbackRegistry;
        Message registerMeMessage = produceMessage("MessageSystem", new EmptyResultDataType(), MessageType.REGISTER_ME, System.out::println);
        sendMessage(registerMeMessage);
        executorService.submit(this::awaitIncomingMsg);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = sendMessageViaSocket(msg);
        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @SuppressWarnings("all")
    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlersStore.getHandlerByType(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(message -> sendMessage((Message) message));
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
    }

    @Override
    public <T extends ResultDataType> Message produceMessage(String to, T data, MessageType msgType,
                                                             MessageCallback<T> callback) {
        Message message = MessageBuilder.buildMessage(name, clientPort, to, null, data, msgType);
        callbackRegistry.put(message.getCallbackId(), callback);
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    private boolean sendMessageViaSocket(Message message) {

        try (Socket clientSocket = new Socket(messageServerHost, messageServerPort);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {
            objectOutputStream.writeObject(message);
            logger.info("Message with ID [{}] is send to {} on host {} and port {}", message.getId(), message.getTo(), messageServerHost, messageServerPort);
            return true;
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return false;
        }


    }


    private void awaitIncomingMsg() {
        try (ServerSocket serverSocket = new ServerSocket(clientPort)) {
            logger.info("Starting Listening new messages on port: {}", clientPort);
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("Waiting for client connection...");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket); //ToDo сделать обработку входящего сообщения -> вызвать метод hdndle
                }
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    private void clientHandler(Socket clientSocket) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            Message receivedMessage = (Message) objectInputStream.readObject();
            logger.info("Received from {}: message ID[{}] ", receivedMessage.getFrom(), receivedMessage.getId());
            handle(receivedMessage);

        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }
}
