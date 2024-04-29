package com.tardisgallifrey;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatSocket implements Runnable {
    private Socket client;
    Scanner input = new Scanner(System.in);
    private final String hostName;
    private final int hostPort;
    private int choice = 1;

    ChatSocket(String host, int port){
        this.hostName = host;
        this.hostPort = port;
    }
    @Override
    public void run() {

        while(choice != -1) {
            try {
                client = new Socket(this.hostName, this.hostPort);
                client.setKeepAlive(true);
                if (client.isConnected()) {
                    System.out.println("Chat connection made...");
                }
            } catch (IOException e) {
                if (e.getMessage().contains("refuse")) {
                    System.out.println(e.getMessage());
                }

            }
            System.out.println("Enter -1 to quit");
            choice = input.nextInt();
        }

    }
}
