package org.example.Modules;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerEmptyCommandData;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientCommunicationModule {
    private SocketChannel channel;
    private String host;
    private int port;
    private final int READING_DATA_SIZE_BUFFER_CAPACITY = 4;

    public ClientCommunicationModule(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            channel = SocketChannel.open();
        } catch (IOException e) {
            System.out.println("Ошибка при открытии канала коммуникации!");
            throw new RuntimeException(e);
        }
    }

    public ServerCommandData executeCommand(ServerCommandData command) {
        ServerCommandData newCommandData;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            if (!channel.isOpen()) {
                channel = SocketChannel.open();
            }
            if (!channel.isConnected()) {
                channel.connect(new InetSocketAddress(host, port));
            }
            channel.configureBlocking(true);

//            channel = SocketChannel.open();
//            channel.connect(new InetSocketAddress(host, port));
//            channel.configureBlocking(true);

            oos.writeObject(command);
            oos.flush();
            byte[] sendingData = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(READING_DATA_SIZE_BUFFER_CAPACITY + sendingData.length);
            buffer.putInt(sendingData.length);
            buffer.put(sendingData);
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            try {channel.close();} catch (IOException ignored) {}
            newCommandData = new ServerEmptyCommandData();
            newCommandData.setErrorMessage(e.getMessage());
            return newCommandData;
//            throw new RuntimeException(e); // !!! Передать команде на вывод
        }

        ByteBuffer lengthBuffer;
        ByteBuffer dataBuffer = ByteBuffer.allocate(0);
        try {
            lengthBuffer = ByteBuffer.allocate(READING_DATA_SIZE_BUFFER_CAPACITY);
            while (lengthBuffer.hasRemaining()) {
                channel.read(lengthBuffer);
            }
            lengthBuffer.flip();
            int dataLength = lengthBuffer.getInt();
            dataBuffer = ByteBuffer.allocate(dataLength);
            while (dataBuffer.hasRemaining()) {
                channel.read(dataBuffer);
            }
            dataBuffer.flip();
        } catch (IOException e) {
            newCommandData = new ServerEmptyCommandData();
            newCommandData.setErrorMessage(e.getMessage());
            return newCommandData;
//            throw new RuntimeException(e); // !!! Передать команде на вывод
        }

        // Десериализация
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBuffer.array()))) {
            newCommandData = (ServerCommandData) ois.readObject();;
        } catch (ClassNotFoundException | IOException e) {
            try {channel.close();} catch (IOException ignored) {}

            newCommandData = new ServerEmptyCommandData();
            newCommandData.setErrorMessage(e.getMessage());
//            throw new RuntimeException("Ошибка при десериализации объекта на клиенте", e); // !!! Передать команде на вывод
        }
        return newCommandData;
    }
}
