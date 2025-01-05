package de.ben;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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
    private PokerGUI pokerGUI;
    private MainGUI mainGui;

    public GameClient(String host, int port, String name, MainGUI mainGUI) throws IOException {
        this.host = host;
        this.name = name;
        this.port = port;
        this.mainGui = mainGUI;
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
                    if (message.startsWith("START:")) {
                        handleStartMessage(message);
                    }
                    else if (message.startsWith("PLAYER:")) {
                        String playerName = message.substring(7);
                        if (lobby != null) {
                            lobby.addPlayer(playerName);
                        }
                    } else if (message.startsWith("PLAYERS:")) {
                        String[] playerNames = message.substring(8).split(",");
                        if (lobby != null) {
                            lobby.setPlayerNames(List.of(playerNames));
                            if(lobby.mainGUI.playerIndex == -1){
                                lobby.mainGUI.playerIndex = playerNames.length-1;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Verbindung wurde abgebrochen!");
                System.exit(1);
            }
        }).start();
    }

    private void handleStartMessage(String message) {
        String[] parts = message.split(":");
        int playerCount = Integer.parseInt(parts[1]);
        int startChips = Integer.parseInt(parts[2]);
        int bigBlind = Integer.parseInt(parts[3]);
        String[] playerNames = parts[4].split(",");

        // Log the parsed values
        System.out.println("playerCount: " + playerCount);
        System.out.println("startChips: " + startChips);
        System.out.println("bigBlind: " + bigBlind);
        System.out.println("playerNames: " + Arrays.toString(playerNames));

        ArrayList<String> playerNamesArrayList = new ArrayList<>(Arrays.asList(playerNames));

        while (playerNamesArrayList.size() < 8) {
            playerNamesArrayList.add("");
        }

        System.out.println("Final playerNamesArrayList: " + playerNamesArrayList);
        new PokerGUI(playerCount, playerNamesArrayList, startChips, bigBlind, mainGui).setVisible(true);
        lobby.dispose();
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