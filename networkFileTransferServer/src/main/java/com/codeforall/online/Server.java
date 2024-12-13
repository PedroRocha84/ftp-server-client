package com.codeforall.online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            this.port = port;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            System.out.println("Server listening on port " + port);
            clientSocket = serverSocket.accept(); //metodo bloqueante. estabeleceu/aceitou a ligação com o cliente
            System.out.println("Connection accepted for client: " + clientSocket.getInetAddress() + " on port " +  clientSocket.getPort());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
