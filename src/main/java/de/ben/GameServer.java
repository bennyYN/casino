package de.ben;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {

    private final ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final ArrayList<String> playerNames = new ArrayList<>();
    private Lobby lobby;

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void addPlayerNames(String playerName) {
        playerNames.add(playerName);
        if (lobby != null) {
            lobby.addPlayer(playerName);
        }
        broadcastPlayerNames();
    }

    public GameServer(int port, MainGUI maingui) throws IOException {
        this.serverSocket = new ServerSocket(port);
        new Thread(this::start).start(); // Run start() in a separate thread
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePlayerName(String playerName) {
        playerNames.remove(playerName);
        if (lobby != null) {
            lobby.removePlayer(playerName);
        }
        broadcastPlayerNames();
    }

    private void broadcastPlayerNames() {
        String playerNamesMessage = "PLAYERS:" + String.join(",", playerNames);
        for (ClientHandler client : clients) {
            client.sendMessage(playerNamesMessage);
        }
    }

    class ClientHandler implements Runnable {

        private final Socket socket;
        private final GameServer server;
        private PrintWriter out;
        private BufferedReader in;
        private String playerName;

        public ClientHandler(Socket socket, GameServer server) {
            this.socket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                playerName = in.readLine();
                if (playerName != null && !playerName.isEmpty()) {
                    synchronized (server.getPlayerNames()) {
                        server.addPlayerNames(playerName);
                    }
                    System.out.println("New player connected: " + playerName);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(playerName + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    synchronized (server.getPlayerNames()) {
                        server.removePlayerName(playerName);
                    }
                    System.out.println("Player disconnected: " + playerName);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}