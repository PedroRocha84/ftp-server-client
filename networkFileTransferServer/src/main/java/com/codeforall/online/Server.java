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

    private FileInputStream fileInput;
    private FileOutputStream fileOutput;

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
            while(true) {
                handleRequest();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            closeResources();
        }*/
    }

    private void handleRequest() throws IOException {
        openStreams();
        String message;
        String text = null;

        while((message = bufferedReader.readLine()) != null) {
            switch(message.toUpperCase()) {
                case "BYE":
                    text = "Terminate the connection";
                    System.out.println("Terminate the connection");
                    writeMessageToClient(text);
                    exit();

                case "DISCONNECT":
                    text = "Terminate the connection";
                    System.out.println("Terminate the connection");
                    writeMessageToClient(text);
                    exit();
                    break;
                case "QUIT":
                    text = "Terminate the connection";
                    System.out.println("Terminate the connection");
                    writeMessageToClient(text);
                    exit();
                    break;
                case "HELP":
                    System.out.println("Command: Help - See available commands");
                    help();
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

        }
    }

    private void help() {

        try {
            FileReader reader = new FileReader("src/main/resources/listCommands.txt");
            BufferedReader fileReader = new BufferedReader(reader);

            String line;
            while ((line = fileReader.readLine()) != null) {
                writeMessageToClient(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }


/*        try {
            fileInput = new FileInputStream("src/main/resources/listCommands.txt");

            byte[] buffer = new byte[1024];
            int bytesRead = fileInput.read(buffer);

            while (bytesRead != -1) {
                writeMessageToClient(fileInput.toString());
                bytesRead = fileInput.read(buffer);
            }

        } catch (IOException e) {
            System.out.println("Error loading file ... ");
        }*/

    }

    private void exit() {
        closeResources();
        System.exit(0);
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
            serverSocket.close();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
