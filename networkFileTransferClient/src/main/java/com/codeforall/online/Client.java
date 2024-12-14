package com.codeforall.online;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Scanner scannerInput;

    private FileInputStream in;
    private BufferedOutputStream out;

    private BufferedInputStream inStream;
    private FileOutputStream outStream;

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

            if(message.equalsIgnoreCase("LS")) {
                String serverMessage;
                System.out.println("List of files on server:");
                while ((serverMessage = bufferedReader.readLine()) != null) {
                    if(!serverMessage.equals("no files")){
                        System.out.println("  -> " + serverMessage);
                    }
                    if(serverMessage.equals("no files")){
                        break;
                    }
                }
            }


            if(message.equalsIgnoreCase("PUT")){
                System.out.println("Please specify the filename:");
                String userInput;
                while ((userInput = readMessage()) != null ) {
                    System.out.println("Filename is: " + userInput);
                    sendFileName( userInput);
                    readFile( userInput);
                    break;
                }
            }

            if(message.equalsIgnoreCase("GET")) {
                System.out.println("Please write the filename and press enter:");
                String fileName = null; // nome do ficheiro que vamos que vamos pedir ao server

                while (fileName == null){
                    fileName = scannerInput.nextLine();
                }

                System.out.println(fileName);
                requestFile(fileName);
                receiveFile(fileName);
                System.out.println("File created");
            }

            if(message.equalsIgnoreCase("MKDIR")){
                System.out.println("Please write the directory name and press enter:");
                String fileName = null;

                while (fileName == null){
                    fileName = scannerInput.nextLine();
                }
                sendMessageToServer(fileName);
            }

        }
    }

    private void requestFile(String fileName) throws IOException {
        sendMessageToServer(fileName);

    }

    private void sendFileName(String userInput) throws IOException {
        sendMessageToServer(userInput); // Estou a mandar o nome do ficheiro
    }

    private void readFile(String source) throws IOException {

        in = new FileInputStream("clientRoot/" + source);
        out = new BufferedOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[1024];

        int bytesRead = in.read(buffer);

        while(bytesRead != -1) {
            out.write(buffer, 0, bytesRead);
            bytesRead = in.read(buffer);
        }

        out.flush();
        in.close();
    }

    private void receiveFile(String fileName) throws IOException {

        if(fileName == null){
            return;
        }

        String destination = "clientRoot/" + fileName;

        inStream = new BufferedInputStream(socket.getInputStream());
        outStream = new FileOutputStream(destination);

        byte[] buffer = new byte[1024];

        int bytesRead = inStream.read(buffer);

        while (bytesRead != -1) {
            if (bytesRead < buffer.length) {
                break;
            }
            outStream.write(buffer, 0, bytesRead);
            bytesRead = inStream.read(buffer);
        //  System.out.println(bytesRead);
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
