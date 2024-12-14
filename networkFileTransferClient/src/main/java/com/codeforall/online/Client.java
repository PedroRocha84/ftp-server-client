package com.codeforall.online;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Scanner scannerInput;


    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to " + host + ":" + port);
            sendMessage();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    private void sendMessage() throws IOException {
        openStreams();
        scannerInput = new Scanner(System.in);
        String message;
        while (true) {
            message = readMessage();
            sendMessageToServer(message);
            readMessageFromServer();
        }
    }

    private void readMessageFromServer() throws IOException {
        String clientMessage;
        clientMessage = bufferedReader.readLine();
        System.out.println(clientMessage);
    }

    private void sendMessageToServer(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private String readMessage() {
        return scannerInput.nextLine();
    }

    private void closeResources() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openStreams() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Could not open streams.");
            throw new RuntimeException(e);
        }
    }
}
