package de.ben;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {

    private final ServerSocket serverSocket; // ServerSocket für eingehende Verbindungen
    private final List<ClientHandler> clients = new ArrayList<>(); // Liste der verbundenen Clients
    private final ArrayList<String> playerNames = new ArrayList<>(); // Liste der Spielernamen
    private Lobby lobby; // Referenz auf die Lobby

    // Konstruktor für GameServer
    public GameServer(int port, MainGUI maingui) throws IOException {
        this.serverSocket = new ServerSocket(port);
        new Thread(this::start).start(); // Startet den Server in einem neuen Thread
    }

    // Methode um die Lobby zu setzen
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    // Methode um die Spielernamen zu bekommen
    public List<String> getPlayerNames() {
        return playerNames;
    }

    // Methode um einen Spielernamen hinzuzufügen
    public void addPlayerNames(String playerName) {
        playerNames.add(playerName);
        if (lobby != null) {
            lobby.addPlayer(playerName);
        }
        broadcastPlayerNames(); // Sendet die aktualisierte Liste der Spielernamen an alle Clients
    }

    // Methode um einen Spielernamen zu entfernen
    public void removePlayerName(String playerName) {
        playerNames.remove(playerName);
        if (lobby != null) {
            lobby.removePlayer(playerName);
        }
        broadcastPlayerNames(); // Sendet die aktualisierte Liste der Spielernamen an alle Clients
    }

    // Methode um die Liste der Spielernamen an alle Clients zu senden
    private void broadcastPlayerNames() {
        String playerNamesMessage = "PLAYERS:" + String.join(",", playerNames);
        for (ClientHandler client : clients) {
            client.sendMessage(playerNamesMessage);
        }
    }

    // Methode um den Server zu starten und auf eingehende Verbindungen zu warten
    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start(); // Startet einen neuen Thread für jeden Client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Innere Klasse für die Behandlung von Client-Verbindungen
    class ClientHandler implements Runnable {

        private final Socket socket;
        public final GameServer server;
        private PrintWriter out;
        private BufferedReader in;
        private String playerName;

        // Konstruktor für ClientHandler
        public ClientHandler(Socket socket, GameServer server) {
            this.socket = socket;
            this.server = server;
        }

        // Methode um Nachrichten vom Client zu empfangen und zu verarbeiten
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
                }

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("START:")) {
                        handleStartMessage(message);
                    } else if (message.startsWith("DECK:") || message.startsWith("AKTION:") || message.startsWith("STARTCHIPS:") || message.startsWith("BIGBLIND:")) {
                        broadcastMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    synchronized (server.getPlayerNames()) {
                        server.removePlayerName(playerName);
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Methode um eine Nachricht an den Client zu senden
        public void sendMessage(String message) {
            out.println(message);
        }

        // Methode um eine Nachricht an alle Clients zu senden
        public void broadcastMessage(String message) {
            for (ClientHandler client : server.clients) {
                client.sendMessage(message);
            }
        }

        // Methode um Startnachrichten zu verarbeiten
        private void handleStartMessage(String message) {
            String[] parts = message.split(":");
            Integer.parseInt(parts[1]); // Startchips
            Integer.parseInt(parts[2]); // BigBlind
            String[] playerNames = parts[3].split(",");
            Deck leaderDeck = new Deck();
            String serializedDeck = leaderDeck.serialize();
            String startMessage = String.join(":", parts[0], parts[1], parts[2], parts[3], parts[4], serializedDeck);
            broadcastMessage(startMessage);
        }
    }
}