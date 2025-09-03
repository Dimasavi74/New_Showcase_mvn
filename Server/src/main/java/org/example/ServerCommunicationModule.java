package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServerCommunicationModule {
    Selector selector;
    BdManager bdManager;
    Integer counter = 0;
    Set<SelectionKey> blockedKeys = Collections.synchronizedSet(new HashSet<>());

    public ServerCommunicationModule(BdManager bdManager, int port) {
        this.bdManager = bdManager;
        ServerSocketChannel server;
        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.configureBlocking(false);

            server.bind(new InetSocketAddress(port));
            System.out.println("Сервер открыт?: " + server.isOpen());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            System.out.println("Канал для регистрации accept закрыт!");
            throw new RuntimeException(e);
        }
    }

    public Set<Task> checkTasks(){
        try {
            selector.select();
        } catch (IOException e) {
            System.out.println("Ошибка при вызове select()!");
//            throw new RuntimeException(e);
        }
        Set<SelectionKey> keys = selector.selectedKeys();
        Set<Task> tasks = new HashSet<>();
        for (var iter = keys.iterator(); iter.hasNext(); ) {
            SelectionKey key = iter.next();
            if (blockedKeys.contains(key)) {
                iter.remove();
                continue;
            }
            iter.remove();
            Task task = new Task(counter++, key, bdManager, blockedKeys);
            System.out.println("Задача №" + (counter - 1) + " создана");
            tasks.add(task);
            blockedKeys.add(key);
        }
        return tasks;
    }
}

