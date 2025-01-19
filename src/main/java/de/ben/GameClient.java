package de.ben;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GameClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String name;
    private int port;
    private String host;
    private GameServer gameServer;
    private Lobby lobby;
    private PokerGUI pokerVAR;
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
                    }else if(message.startsWith("AKTION")) {
                        handleAktionMessage(message);
                    }else if(message.startsWith("DECK:")) {
                        String deckString = message.substring(5);
                        Deck deck = Deck.deserialize(deckString);
                        pokerVAR.getGameInstance().getDeck().setDeck(deck);
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
                    }else if((message.startsWith("STARTCHIPS:") && !Objects.requireNonNull(lobby).getisleader())) {
                        int startChips = Integer.parseInt(message.substring(11));
                        lobby.setStartChips(startChips);
                    }else if(message.startsWith("BIGBLIND:") && !lobby.getisleader()) {
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

    private void handleAktionMessage(String message) {
        System.out.println("Received AKTION message: " + message);
        String[] parts = message.split(":", 3);
        parts[1] = parts[1].trim();
        switch(parts[1]) {
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

    private void handleStartMessage(String message) {
        System.out.println("Received START message: " + message);
        String[] parts = message.split(":", 6); // Split into 6 parts to handle the serialized deck correctly
        if (parts.length < 6) {
            System.err.println("Invalid START message format");
            return;
        }
        int numPlayers = Integer.parseInt(parts[1]);
        int startChips = Integer.parseInt(parts[2]);
        int bigBlind = Integer.parseInt(parts[3]);
        String[] playerNames = parts[4].split(",");
        Deck deck = Deck.deserialize(parts[5]);

        // Log the parsed values
        /*System.out.println("numPlayers: " + numPlayers);
        System.out.println("startChips: " + startChips);
        System.out.println("bigBlind: " + bigBlind);
        System.out.println("playerNames: " + Arrays.toString(playerNames));
        System.out.println("Deck: " + parts[5]);*/

        ArrayList<String> playerNamesArrayList = new ArrayList<>(Arrays.asList(playerNames));

        while (playerNamesArrayList.size() < 8) {
            playerNamesArrayList.add("");
        }

        System.out.println("Final playerNamesArrayList: " + playerNamesArrayList);
        pokerVAR = new PokerGUI(numPlayers, playerNamesArrayList, startChips, bigBlind, mainGui);
        pokerVAR.getGameInstance().getDeck().setDeck(deck);
        pokerVAR.setVisible(true);
        try {
            lobby.dispose();
        } catch (Exception e) {
            System.out.println("Lobby already closed");
        }
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

    public boolean isLeader() {
        return lobby.getisleader();
    }
}