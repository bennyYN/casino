package de.ben;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GameClient {

    private Socket clientSocket; // Socket für die Verbindung zum Server
    private PrintWriter out; // PrintWriter zum Senden von Nachrichten an den Server
    private BufferedReader in; // BufferedReader zum Empfangen von Nachrichten vom Server
    private final String name; // Name des Spielers
    private int port; // Portnummer des Servers
    private String host; // Hostname oder IP-Adresse des Servers
    private GameServer gameServer; // Referenz auf den GameServer
    private Lobby lobby; // Referenz auf die Lobby
    private PokerGUI pokerVAR; // Referenz auf die PokerGUI
    private MainGUI mainGui; // Referenz auf die MainGUI

    // Konstruktor für GameClient
    public GameClient(String host, int port, String name, MainGUI mainGUI) throws IOException {
        this.host = host;
        this.name = name;
        this.port = port;
        this.mainGui = mainGUI;
    }

    // Methode um eine Verbindung zum Server aufzubauen
    public void connect() throws IOException {
        clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println(name); // Sendet den Namen des Spielers an den Server

        // Starte neuen Thread um Nachrichten vom Server zu empfangen und zu verarbeiten
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("START:")) {
                        handleStartMessage(message);
                    } else if (message.startsWith("AKTION")) {
                        handleAktionMessage(message);
                    } else if (message.startsWith("DECK:")) {
                        String deckString = message.substring(5);
                        Deck deck = Deck.deserialize(deckString);
                        pokerVAR.getGameInstance().getDeck().setDeck(deck);
                    } else if (message.startsWith("PLAYER:")) {
                        String playerName = message.substring(7);
                        if (lobby != null) {
                            lobby.addPlayer(playerName);
                        }
                    } else if (message.startsWith("PLAYERS:")) {
                        String[] playerNames = message.substring(8).split(",");
                        if (lobby != null) {
                            lobby.setPlayerNames(List.of(playerNames));
                            if (lobby.mainGUI.playerIndex == -1) {
                                lobby.mainGUI.playerIndex = playerNames.length - 1;
                            }
                        }
                    } else if (message.startsWith("STARTCHIPS:") && !Objects.requireNonNull(lobby).getisleader()) {
                        int startChips = Integer.parseInt(message.substring(11));
                        lobby.setStartChips(startChips);
                    } else if (message.startsWith("BIGBLIND:") && !lobby.getisleader()) {
                        int bigBlind = Integer.parseInt(message.substring(9));
                        lobby.setBigBlind(bigBlind);
                    }
                }
            } catch (IOException e) {
                System.err.println("Verbindung wurde abgebrochen!");
                System.exit(1);
            }
        }).start();
    }

    // Methode um Aktionen zu verarbeiten
    private void handleAktionMessage(String message) {
        String[] parts = message.split(":", 3);
        parts[1] = parts[1].trim();
        switch (parts[1]) {
            case "CALL":
                pokerVAR.doCall();
                break;
            case "RAISE":
                if (parts[2] != null) {
                    parts[2] = parts[2].trim();
                }
                pokerVAR.doRaise2(Integer.parseInt(parts[2]));
                break;
            case "FOLD":
                pokerVAR.doFold();
                break;
            case "CHECK":
                pokerVAR.doCheck();
                break;
            case "ALLIN":
                pokerVAR.doAllIn();
                break;
            case "CONTINUE":
                pokerVAR.doContinue();
                break;
        }
    }

    // Methode um Startnachrichten zu verarbeiten
    private void handleStartMessage(String message) {
        String[] parts = message.split(":", 6);
        if (parts.length < 6) {
            System.err.println("Ungültige START Nachricht: " + message);
            return;
        }
        int numPlayers = Integer.parseInt(parts[1]);
        int startChips = Integer.parseInt(parts[2]);
        int bigBlind = Integer.parseInt(parts[3]);
        String[] playerNames = parts[4].split(",");
        Deck deck = Deck.deserialize(parts[5]);

        ArrayList<String> playerNamesArrayList = new ArrayList<>(Arrays.asList(playerNames));

        while (playerNamesArrayList.size() < 8) {
            playerNamesArrayList.add("");
        }

        pokerVAR = new PokerGUI(numPlayers, playerNamesArrayList, startChips, bigBlind, mainGui);
        pokerVAR.getGameInstance().getDeck().setDeck(deck);
        pokerVAR.setVisible(true);
        try {
            lobby.dispose();
        } catch (Exception e) {
            System.out.println("Lobby schon geschlossen");
        }
    }

    // Methode um Nachrichten zu senden
    public void sendMessage(String message) {
        out.println(message);
    }

    // Methode um die Verbindung zu schließen
    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    // Methode um die Lobby zu setzen
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    // Methode um den GameServer zu bekommen
    public GameServer getGameServer() {
        return gameServer;
    }

    // Methode um zu überprüfen, ob der Spieler der Anführer ist
    public boolean isLeader() {
        return lobby.getisleader();
    }
}