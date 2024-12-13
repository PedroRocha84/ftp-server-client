package com.codeforall.online;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;


    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to " + host + ":" + port);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeResources();
        }
    }
    private void closeResources(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
