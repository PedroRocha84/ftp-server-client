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
            scannerInput = new Scanner(System.in);
            sendMessage();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } /*finally {  // >Codigo retirado para evitar fecho da stream apos cada mensagem
            closeResources();
        }*/
    }

    private void sendMessage() throws IOException {
        openStreams();

        String message;

        while (true) {
            message = readMessage(); // Retorno do Scanner = Terminal
            System.out.println(message);
            sendMessageToServer(message);
            //readMessageFromServer();

            if(message.equalsIgnoreCase("BYE")
                    || message.equalsIgnoreCase("DISCONNECT")
                    || message.equalsIgnoreCase("QUIT")) {
                readMessageFromServer();
                exit();
            }

            if (message.equalsIgnoreCase("HELP")) {
                String serverMessage;
                while ((serverMessage = bufferedReader.readLine()) != null) {
                    System.out.println(serverMessage);
                    if(serverMessage.equals("#")){
                        break;
                    }
                }

            }

            if(message.equalsIgnoreCase("LS")){
                String serverMessage;
                System.out.println("List of files on server:");
                while ((serverMessage = bufferedReader.readLine()) != null
                        && !serverMessage.equals("no files"))
                    System.out.println( "  -> " + serverMessage);
                }

            if(message.equalsIgnoreCase("PUT")){
                System.out.println("Please specify the filename:");
                String userInput;
                while ((userInput = readMessage()) != null ) {
                    System.out.println("FIlename is: " + userInput);
                }

            }
        }
    }

    private void exit() {
        closeResources();
        System.exit(0);
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
