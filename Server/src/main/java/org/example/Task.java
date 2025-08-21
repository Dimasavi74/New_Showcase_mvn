package org.example;

import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerEmptyCommand;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class Task implements Runnable {
    private final int id;
    private final SelectionKey key;
    private BdManager bdManager;
    private final int READING_DATA_SIZE_BUFFER_CAPACITY = 4;
    private Set<SelectionKey> blockedKeys;

    public Task(int id, SelectionKey k, BdManager bd, Set<SelectionKey> blockedKeys) {
        this.id = id;
        this.key = k;
        this.bdManager = bd;
        this.blockedKeys = blockedKeys;
    }

    public int getId(){
        return id;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                doAccept();
                blockedKeys.remove(key);
            }
            else if (key.isReadable()) {
                doRead();
                blockedKeys.remove(key);
            }
            else if (key.isWritable()) {
                doWrite();
                blockedKeys.remove(key);
            }
        }
    }

    public void doAccept() {
        System.out.println("Выполняется accept задачи №" + id);
        System.out.println("Ключ: " + key);
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        System.out.println("Канал: " + ssc);
        SocketChannel sc;

        try {
            sc = ssc.accept();
        } catch (IOException e) {
            System.out.println("Подключение не удалось!");
            key.cancel();
            blockedKeys.remove(key);
            throw new RuntimeException(e); // !!!! Убрать на игнорирование
        }
        System.out.println("Подключение удалось!");

        try {
            sc.configureBlocking(false);
        } catch (IOException e) {
            try {
                sc.close();
            } catch (IOException ignored) {}
            key.cancel();
            blockedKeys.remove(key);
            throw new RuntimeException(e); // !!! Игнор
        }

        SelectionKey clientKey;
        try {
            System.out.println("Регистрируем ключ на чтение");
            clientKey = sc.register(key.selector(), SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            System.out.println("Канал закрыт, регистрация ключа на чтение невозможна! Канал: " + sc);
            try {
                sc.close();
            } catch (IOException ignored) {}
            key.cancel();
            blockedKeys.remove(key);
            throw new RuntimeException(e); // !!! Убрать на игнорирование
        } catch (CancelledKeyException e) {
            System.out.println("Ключ отменен, регистрация на чтение невозможна!");
            blockedKeys.remove(key);
            throw new RuntimeException(e); // !!! Убрать на игнорирование
        }
        System.out.println("Ключ на чтение зарегистрирован!: " + clientKey);
        ClientData data = new ClientData();
        clientKey.attach(data);
        key.selector().wakeup();
    }

    public void doRead() {
        System.out.println("Выполняется чтение задачи №" + id);
        System.out.println("Ключ: " + key);
        SocketChannel sc = (SocketChannel) key.channel();
        System.out.println("Канал: " + sc);
        ClientData data = (ClientData) key.attachment();
        System.out.println("Привязаный объект: " + data);
        ServerCommand command;

        ByteBuffer lengthBuffer;
        ByteBuffer dataBuffer;
        try {
            // Чтение размера данных (4 байта)
            System.out.println("Начато чтение данных клиента");
            lengthBuffer = ByteBuffer.allocate(READING_DATA_SIZE_BUFFER_CAPACITY);
            try {
                sc.read(lengthBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lengthBuffer.flip();
            int dataLength = lengthBuffer.getInt();
            System.out.println("Размер входящих данных: " + dataLength);
            dataBuffer = ByteBuffer.allocate(dataLength);
            while (dataBuffer.hasRemaining()) {
                sc.read(dataBuffer);
            }
            dataBuffer.flip();
        } catch (IOException e) {
            command = new ServerEmptyCommand();
            command.setError(e);
            throw new RuntimeException("Ошибка при получении данных клиента", e); // !!! Убрать на игнорирование!
        }

        // Десериализация

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBuffer.array()))) {
            command = (ServerCommand) ois.readObject();
            System.out.println("Получен объект: " + command);
        } catch (ClassNotFoundException | IOException e) {
            command = new ServerEmptyCommand();
            command.setError(e);
            throw new RuntimeException("Ошибка при десериализвции объекта на сервере", e); // !!! Убрать на игнорирование!
        }
        System.out.println("Назначаем бд-менеджер");
        command.setBdManager(bdManager);
        System.out.println("Исполняем команду");
        command.execute();
        System.out.println("Ошибка при исполнении: " + command.getError());
        data.setCommand(command);

        SelectionKey clientKey;
        try {
            System.out.println("Регистрируем ключ на запись");
            clientKey = sc.register(key.selector(), SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            System.out.println("Канал закрыт, регистрация ключа на запись невозможна! Канал: " + sc);
            key.cancel();
            blockedKeys.remove(key);
            throw new RuntimeException(e); // !!! Убрать на игнорирование!
        } catch (CancelledKeyException e) {
            System.out.println("Ключ отменен, регистрация на запись невозможна!");
            try {
                sc.close();
            } catch (IOException ignored) {}
            blockedKeys.remove(key);
            throw new RuntimeException(e); // !!! Убрать на игнорирование
        }
        System.out.println("Ключ на запись зарегистрирован!: " + clientKey);
        clientKey.attach(data);
    }

    public void doWrite() {
        System.out.println("Выполняется запись задачи №" + id);
        System.out.println("Ключ: " + key);
        SocketChannel sc = (SocketChannel) key.channel();
        System.out.println("Канал: " + sc);
        ClientData data = (ClientData) key.attachment();
        System.out.println("Привязаный объект: " + data);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(data.getCommand());
            oos.flush();
            byte[] sendingData = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(READING_DATA_SIZE_BUFFER_CAPACITY + sendingData.length);
            buffer.putInt(sendingData.length);
            buffer.put(sendingData);
            buffer.flip();

            while (buffer.hasRemaining()) {
                sc.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // !!! Убрать на игнорирование!
        }
        try {
            sc.close();
        } catch (IOException ignored) {}
        key.cancel();
        blockedKeys.remove(key);
    }
}