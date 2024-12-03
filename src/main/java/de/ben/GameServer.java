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

    public GameServer(int port, int startChips, int bigBlind, MainGUI maingui) throws IOException {
        this.serverSocket = new ServerSocket(port);
        new PokerGUI(8, playerNames, startChips, bigBlind,0, maingui);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

class ClientHandler implements Runnable {

    private final Socket socket;
    private final GameServer server;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                // Broadcast message to all clients
                for (ClientHandler client : server.clients) {
                    client.out.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }
}
