package com.codeforall.online;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

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
            handleRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void handleRequest() throws IOException {
        openStreams();
        String message;
        String text = null;

        while((message = bufferedReader.readLine()) != null) {
            switch(message) {
                case "BYE":
                    text = "Terminate the connection";
                    System.out.println("Terminate the connection");
                    break;
                case "DISCONNECT":
                    text = "Terminate the connection";
                    System.out.println("Terminate the connection");
                    break;
                case "QUIT":
                    text = "Terminate the connection";
                    System.out.println("Terminate the connection");
                    break;
                case "HELP":
                    text = "See available commands";
                    System.out.println("See available commands");
                    break;
                case "LS":
                    text = "List files available on the server";
                    System.out.println("List files available on the server");
                    break;
                case "PUT":
                    text = "Upload a file to the server";
                    System.out.println("Upload a file to the server");
                    break;
                case "GET":
                    text = "Get a file from the server";
                    System.out.println("Get a file from the server");
                    break;
                case "MKDIR":
                    text = "Create a directory on the server";
                    System.out.println("Create a directory on the server");
                    break;
                default:
            }
            writeMessageToClient(text);
        }
    }


    public void writeMessageToClient(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();//****PORQUE É QUE TEMOS DE ESCREVER ESTE new.line PARA ENVIAR A MENSAGEM? O FKUSH NÃO OBRIGA A QUE ESTA MENSAGEM SEJA ENVIADA?
        bufferedWriter.flush();
    }

    private void openStreams() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Could not open streams.");
            throw new RuntimeException(e);
        }
    }

    private void closeResources() {
        try {
            clientSocket.close();
            //serverSocket.close();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
