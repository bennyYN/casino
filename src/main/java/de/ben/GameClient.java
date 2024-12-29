package de.ben;

import java.io.*;
import java.net.Socket;

public class GameClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String name;
    private int port;
    private String host;

    public GameClient(String host, int port, String name) {
        this.host = host;
        this.name = name;
        this.port = port;
        try {
            connect();
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        }
    }
    public void connect() throws IOException {
        clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("Connected to server: " + host + ":" + port);
        out.println(name);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public String getName() {
        return name;
    }
}