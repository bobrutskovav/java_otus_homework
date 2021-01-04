package ru.otus.multiprocess.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.multiprocess.messagesystem.message.Message;
import ru.otus.multiprocess.messagesystem.message.MessageBuilder;
import ru.otus.multiprocess.messagesystem.message.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public final class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private static final int MESSAGE_QUEUE_SIZE = 100_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;
    private static final int MSG_PROCESSOR_THREAD_LIMIT = 2;
    private final int messageSystemPort;

    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private final Map<String, ClientInfoStore> clientMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);
    private final ExecutorService msgProcessor = Executors.newFixedThreadPool(MSG_PROCESSOR_THREAD_LIMIT, new ThreadFactory() {
        private final AtomicInteger threadNameSeq = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("msg-processor-thread" + threadNameSeq.incrementAndGet());
            return thread;
        }
    });
    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT,
            new ThreadFactory() {
                private final AtomicInteger threadNameSeq = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
                    return thread;
                }
            });
    private Runnable disposeCallback;

    public MessageSystemImpl(int messageSystemPort) {
        this.messageSystemPort = messageSystemPort;
        start();
    }

    public MessageSystemImpl(boolean startProcessing, int messageSystemPort) {
        this.messageSystemPort = messageSystemPort;
        if (startProcessing) {
            start();
        }
    }

    @Override
    public void start() {
        msgProcessor.submit(this::processMessages);
        msgProcessor.submit(this::awaitNewMessages);

    }

    @Override
    public int currentQueueSize() {
        return messageQueue.size();
    }

    @Override
    public void addClient(String clientName, String clientHost, int clientPort) {
        logger.info("new client:{}", clientName);
        ClientInfoStore clientInfoStore = new ClientInfoStore(clientHost, clientPort);
        clientMap.put(clientName, clientInfoStore);
    }

    @Override
    public void removeClient(String clientName) {
        ClientInfoStore removedClient = clientMap.remove(clientName);
        if (removedClient == null) {
            logger.warn("client not found: {}", clientName);
        } else {
            logger.info("removed client:{}", removedClient);
        }
    }

    @Override
    public boolean newMessage(Message msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            logger.warn("MS is being shutting down... rejected:{}", msg);
            return false;
        }
    }

    @Override
    public void dispose() throws InterruptedException {
        logger.info("now in the messageQueue {} messages", currentQueueSize());
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        disposeCallback = callback;
        dispose();
    }

    private void processMessages() {
        logger.info("msgProcessor started, {}", currentQueueSize());
        while (runFlag.get() || !messageQueue.isEmpty()) {
            try {
                Message msg = messageQueue.take();
                if (msg == MessageBuilder.getVoidMessage()) {
                    logger.info("received the stop message");
                } else if (msg.getType().equals(MessageType.REGISTER_ME.getName())) {
                    logger.info("REGISTERED CLIENT {}", msg.getFrom());
                } else {
                    ClientInfoStore clientInfoStore = clientMap.get(msg.getTo());
                    if (clientInfoStore == null) {
                        logger.warn("client not found");
                    } else {
                        msgHandler.submit(() -> handleMessage(clientInfoStore, msg));
                    }
                }
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        if (disposeCallback != null) {
            msgHandler.submit(disposeCallback);
        }
        msgHandler.submit(this::messageHandlerShutdown);
        logger.info("msgProcessor finished");
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        logger.info("msgHandler has been shut down");
    }


    private void handleMessage(ClientInfoStore clientInfoStore, Message msg) {

        String sendToHost = clientInfoStore.getClientHost();
        int port = clientInfoStore.getClientPort();
        try (Socket clientSocket = new Socket(sendToHost, port);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {
            objectOutputStream.writeObject(msg);
            logger.info("Message with ID [{}] is send to {} on host {} and port {}", msg.getId(), msg.getTo(), sendToHost, port);

        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(MessageBuilder.getVoidMessage());
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(MessageBuilder.getVoidMessage());
        }
    }


    private void awaitNewMessages() {
        try (ServerSocket serverSocket = new ServerSocket(messageSystemPort)) {
            logger.info("Starting Message System on port: {}", messageSystemPort);
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("Waiting for client connection...");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }


    private void clientHandler(Socket clientSocket) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            Message receivedMessage = (Message) objectInputStream.readObject();

            String clientHostAddress = clientSocket.getInetAddress().getHostAddress();
            int clientPort = receivedMessage.getPortFrom();
            logger.info("Received from {}: message ID[{}] ", receivedMessage.getFrom(), receivedMessage.getId());
            if (receivedMessage.getType().equals(MessageType.REGISTER_ME.getName())) {
                addClient(receivedMessage.getFrom(), clientHostAddress, clientPort);
            } else {
                newMessage(receivedMessage);
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

}
