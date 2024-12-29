package de.ben;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class GameClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String name;
    private int port;
    private String host;
    private GameServer gameServer;
    private Lobby lobby;

    public GameClient(String host, int port, String name) throws IOException {
        this.host = host;
        this.name = name;
        this.port = port;
        connect();
    }

    public void connect() throws IOException {
        clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("Connected to server: " + host + ":" + port);
        out.println(name);

        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    if (message.startsWith("PLAYER:")) {
                        String playerName = message.substring(7);
                        if (lobby != null) {
                            lobby.addPlayer(playerName);
                        }
                    } else if (message.startsWith("PLAYERS:")) {
                        String[] playerNames = message.substring(8).split(",");
                        if (lobby != null) {
                            lobby.setPlayerNames(List.of(playerNames));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }
}