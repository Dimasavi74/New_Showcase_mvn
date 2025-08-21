package org.Modules;

import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerEmptyCommand;

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
            throw new RuntimeException(e);
        }
    }

    public ServerCommand executeCommand(ServerCommand command) throws IOException {
        if (!channel.isConnected()) {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(host, port));
        }

        // Отправка на сервер
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            channel.configureBlocking(true);

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
            throw new RuntimeException(e); // !!! Передать команде на вывод
        }



        // Получение данных с сервера
        ByteBuffer lengthBuffer;
        ByteBuffer dataBuffer;
        try {
            channel.configureBlocking(true);

            // Чтение размера данных (4 байта)
            lengthBuffer = ByteBuffer.allocate(READING_DATA_SIZE_BUFFER_CAPACITY);
            try {
                channel.read(lengthBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lengthBuffer.flip();
            int dataLength = lengthBuffer.getInt();
            dataBuffer = ByteBuffer.allocate(dataLength);
            while (dataBuffer.hasRemaining()) {
                channel.read(dataBuffer);
            }
            dataBuffer.flip();

        } catch (IOException e) {
            throw new RuntimeException(e); // !!! Передать команде на вывод
        }

        // Десериализация
        ServerCommand newCommand;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBuffer.array()))) {
            newCommand = (ServerCommand) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            newCommand = new ServerEmptyCommand();
            newCommand.setError(e);
            throw new RuntimeException("Ошибка при десериализвции объекта на клиенте", e); // !!! Передать команде на вывод
        }

        try {
            channel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e); // !!! Игнорировать
        }
    channel.close();
    return newCommand;
    }
}
