package org.example;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.ServerCommands.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Set;

public class Task implements Runnable {
    private final int id;
    private final SelectionKey key;
    private BdManager bdManager;
    private final int READING_DATA_SIZE_BUFFER_CAPACITY = 4;
    private Set<SelectionKey> blockedKeys;
    private HashMap<String, ServerCommand> serverCommandObjects = new HashMap<>();


    public Task(int id, SelectionKey k, BdManager bd, Set<SelectionKey> blockedKeys) {
        this.id = id;
        this.key = k;
        this.bdManager = bd;
        this.blockedKeys = blockedKeys;

        serverCommandObjects.put("addFavourite", new ServerAddFavouriteCommand());
        serverCommandObjects.put("createAdvertisement", new ServerCreateAdvertisementCommand());
        serverCommandObjects.put("deleteAdvertisement", new ServerDeleteAdvertisementCommand());
        serverCommandObjects.put("deleteUser", new ServerDeleteUserCommand());
        serverCommandObjects.put("echo", new ServerEchoCommand());
        serverCommandObjects.put("empty", new ServerEmptyCommand());
        serverCommandObjects.put("login", new ServerLoginCommand());
        serverCommandObjects.put("myAdvertisements", new ServerMyAdvertisementsCommand());
        serverCommandObjects.put("myFavourites", new ServerMyFavouritesCommand());
        serverCommandObjects.put("register", new ServerRegisterCommand());
        serverCommandObjects.put("removeFavourite", new ServerRemoveFavouriteCommand());
        serverCommandObjects.put("search", new ServerSearchCommand());
    }

    public int getId(){
        return id;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                doAccept();
            }
            else if (key.isReadable()) {
                doRead();
            }
            else if (key.isWritable()) {
                doWrite();
            }
        }
    }

    public void doAccept() {
        System.out.println("Выполняется accept задачи №" + id);
        System.out.println("Ключ: " + key);
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        System.out.println("Канал: " + ssc);
        SocketChannel sc;
        SelectionKey clientKey;

        try {
            sc = ssc.accept();
            sc.configureBlocking(false);
            System.out.println("Подключение удалось!");
            System.out.println("Регистрируем ключ на чтение");
            clientKey = sc.register(key.selector(), SelectionKey.OP_READ);
            System.out.println("Ключ на чтение зарегистрирован!: " + clientKey);
            ClientData data = new ClientData();
            clientKey.attach(data);
        }  catch (ClosedChannelException e) {
            System.out.println("Канал закрыт, регистрация ключа на чтение невозможна!: " + e.getMessage());
            key.cancel();
//            throw new RuntimeException(e); // !!! Убрать на игнорирование
        } catch (CancelledKeyException e) {
            System.out.println("Ключ отменен, регистрация на чтение невозможна!: " + e.getMessage());
            key.cancel();
//            throw new RuntimeException(e); // !!! Убрать на игнорирование
        } catch (IOException e) {
            System.out.println("Подключение не удалось!: " + e.getMessage());
            key.cancel();
//            throw new RuntimeException(e); // !!!! Убрать на игнорирование
        } finally {
            blockedKeys.remove(key);
            key.selector().wakeup();
        }

    }

    public void doRead() {
        System.out.println("Выполняется чтение задачи №" + id);
        System.out.println("Ключ: " + key);
        SocketChannel sc = (SocketChannel) key.channel();
        System.out.println("Канал: " + sc);
        ClientData data = (ClientData) key.attachment();
        System.out.println("Привязанный объект: " + data);
        ServerCommand command;

        ByteBuffer lengthBuffer;
        ByteBuffer dataBuffer = ByteBuffer.allocate(0);
        try {
            System.out.println("Начато чтение данных клиента");
            lengthBuffer = ByteBuffer.allocate(READING_DATA_SIZE_BUFFER_CAPACITY);
            while (lengthBuffer.hasRemaining()) {
                sc.read(lengthBuffer);
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
            System.out.println("Ошибка во время чтения данных клиента!: " + e.getMessage());
            command = new ServerEmptyCommand();
            command.setErrorMessage(e.getMessage());
//            throw new RuntimeException("Ошибка при получении данных клиента", e); // !!! Убрать на игнорирование!
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBuffer.array()))) {
            ServerCommandData commandData = (ServerCommandData) ois.readObject();
            command = serverCommandObjects.get(commandData.getCommandName()).setDataByFields(commandData);
            System.out.println("Назначаем бд-менеджер");
            command.setBdManager(bdManager);
            System.out.println("Исполняем команду");
            command.execute();
            System.out.println("Ошибка при исполнении: " + command.getErrorMessage());
            data.setCommand(command);
            System.out.println("Получен объект: " + command);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Ошибка при десериализации объекта!: " + e.getMessage());
            command = new ServerEmptyCommand();
            command.setErrorMessage(e.getMessage());
//            throw new RuntimeException("Ошибка при десериализации объекта на сервере", e); // !!! Убрать на игнорирование!
        }


        SelectionKey clientKey;
        try {
            System.out.println("Регистрируем ключ на запись");
            clientKey = sc.register(key.selector(), SelectionKey.OP_WRITE);
            clientKey.attach(data);
            System.out.println("Ключ на запись зарегистрирован!: " + clientKey);
        } catch (ClosedChannelException e) {
            System.out.println("Канал закрыт, регистрация ключа на запись невозможна! Канал: " + sc);
            key.cancel();
//            throw new RuntimeException(e); // !!! Убрать на игнорирование!
        } catch (CancelledKeyException e) {
            System.out.println("Ключ отменен, регистрация на запись невозможна! " + e.getMessage());
            key.cancel();
//            throw new RuntimeException(e); // !!! Убрать на игнорирование
        } finally {
            blockedKeys.remove(key);
            key.selector().wakeup();
        }
    }

    public void doWrite() {
        System.out.println("Выполняется запись задачи №" + id);
        System.out.println("Ключ: " + key);
        SocketChannel sc = (SocketChannel) key.channel();
        System.out.println("Канал: " + sc);
        ClientData data = (ClientData) key.attachment();
        System.out.println("Привязанный объект: " + data);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(data.getCommand().generateServerCommandData());
            oos.flush();
            byte[] sendingData = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(READING_DATA_SIZE_BUFFER_CAPACITY + sendingData.length);
            buffer.putInt(sendingData.length);
            buffer.put(sendingData);
            buffer.flip();

            while (buffer.hasRemaining()) {
                sc.write(buffer);
            }
            System.out.println("Запись выполнена успешно!");

            SelectionKey clientKey = sc.register(key.selector(), SelectionKey.OP_READ);
            clientKey.attach(new ClientData());
            System.out.println("Ключ на чтение зарегистрирован!: " + clientKey);
        } catch (IOException e) {
            System.out.println("Ошибка передачи при отправке!: " + e.getMessage());
            key.cancel();
//            throw new RuntimeException(e); // !!! Убрать на игнорирование!
        } catch (Exception e) {
            System.out.println("Ошибка при отправке!: " + e.getMessage());
            key.cancel();
        }
        finally {
            blockedKeys.remove(key);
            key.selector().wakeup();
        }
    }
}