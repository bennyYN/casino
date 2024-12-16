package de.ben;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {

    private final ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final ArrayList<String> playerNames = new ArrayList<>();

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void addPlayerNames(String playerName) {
        playerNames.add(playerName);
    }

    public GameServer(int port, MainGUI maingui) throws IOException {
        this.serverSocket = new ServerSocket(port);
        new Thread(this::start).start(); // Run start() in a separate thread
    }

    public void start() {
            try {
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    class ClientHandler implements Runnable {

        private final Socket socket;
        private final GameServer server;
        private PrintWriter out;
        private BufferedReader in;
        private String playerName; // Speichert den Namen des Spielers

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
                    System.out.println("Neuer Spieler verbunden: " + playerName);

                }

                // Nachrichten empfangen (falls nötig)
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(playerName + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    synchronized (server.getPlayerNames()) {
                        server.getPlayerNames().remove(playerName);
                    }
                    System.out.println("Spieler getrennt: " + playerName);
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