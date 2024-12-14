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

    private BufferedInputStream in;
    private FileOutputStream out;

    private FileInputStream inStream;
    private BufferedOutputStream outStream;
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
            System.out.println("Connection accepted for client: " + clientSocket.getInetAddress() + " on port " + clientSocket.getPort());
            while (true) {
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

        while ((message = bufferedReader.readLine()) != null) {
            switch (message.toUpperCase()) {
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
                    System.out.println("List files available on the server");
                    listFiles();
                    break;
                case "PUT":
                    receiveFile();
                    System.out.println("Upload a file to the server");
                    break;
                case "GET":
                    requestFileName();
                    break;
                case "MKDIR":
                    text = "Create a directory on the server";
                    System.out.println("Create a directory on the server");
                    break;
                default:
            }

        }
    }

    private void requestFileName() throws IOException {

        String filename = null;
        while (filename == null) {
            filename = bufferedReader.readLine();
        }

        System.out.println("Get Method - Filename is: " + filename);

        readFile(filename);
    }

    private void readFile(String source) throws IOException {

        inStream = new FileInputStream("serverRoot/" +source);
        outStream = new BufferedOutputStream(clientSocket.getOutputStream());

        byte[] buffer = new byte[1024];

        int bytesRead = inStream.read(buffer);

        while(bytesRead != -1) {
            outStream.write(buffer, 0, bytesRead);
            bytesRead = inStream.read(buffer);
        }

        outStream.flush();
        inStream.close();
    }
    private void receiveFile() throws IOException {
        String fileName = null;
        while (fileName == null) {
            fileName = bufferedReader.readLine();
        }

        String destination = "serverRoot/" + fileName;
        System.out.println(destination);

        in = new BufferedInputStream(clientSocket.getInputStream());
        out = new FileOutputStream(destination);

        byte[] buffer = new byte[1024];

        int bytesRead = in.read(buffer);

        while (bytesRead != -1) {
            if (bytesRead < buffer.length) {
                break;
            }
            out.write(buffer, 0, bytesRead);
            bytesRead = in.read(buffer);
            System.out.println(bytesRead);
        }
    }

    private void listFiles() throws IOException {
        String folderPath = "serverRoot";

        File directory = new File(folderPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File fileInFolder : files) {
                if (fileInFolder.isFile()) {
                    System.out.println(fileInFolder.getName());
                    writeMessageToClient(fileInFolder.getName());
                }
            }
        }
        writeMessageToClient("no files");
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
