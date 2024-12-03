package de.ben;

import java.io.*;
import java.net.Socket;

public class GameClient {

    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final String name;

    public GameClient(String host, int port, String name) throws IOException {
        this.clientSocket = new Socket(host, port);
        this.name = name;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("Server: " + host + ":" + port);
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