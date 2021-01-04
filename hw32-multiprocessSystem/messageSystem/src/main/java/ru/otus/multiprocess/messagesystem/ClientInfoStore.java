package ru.otus.multiprocess.messagesystem;

import java.util.Objects;

public class ClientInfoStore {
    private String clientHost;
    private int clientPort;


    public ClientInfoStore(String clientHost, int clientPort) {
        this.clientHost = clientHost;
        this.clientPort = clientPort;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClientInfoStore that = (ClientInfoStore) object;
        return Objects.equals(clientHost, that.clientHost) &&
                Objects.equals(clientPort, that.clientPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientHost, clientPort);
    }
}
