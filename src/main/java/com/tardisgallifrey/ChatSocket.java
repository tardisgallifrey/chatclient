package com.tardisgallifrey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatSocket implements Runnable {
    private Socket client;
    PrintWriter out;
    BufferedReader in;
    Scanner input = new Scanner(System.in);
    private final String hostName;
    private final int hostPort;
    private String message = "";    //cannot be null when read by while()

    ChatSocket(String host, int port){
        this.hostName = host;
        this.hostPort = port;
    }

    @Override
    public void run() {

        while(!message.equals("exit")) {
            try {
                client = new Socket(this.hostName, this.hostPort);
                client.setKeepAlive(true);
                if (client.isConnected()) {
                    System.out.println("Chat connection made...");
                }

                //create objects for in and out messages
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                System.out.println("Enter your message below. Use 'exit' to quit.");

                //get message from user or exit
                message = input.nextLine();

                //don't send exit as message to server
                if (!message.equals("exit")){
                    out.println(message);
                    out.flush();
                }


            } catch (IOException e) {
                if (e.getMessage().contains("refuse")) {
                    System.out.println(e.getMessage());
                }

            }
            finally{
                input.close();
                if(out != null){
                    out.close();
                }
                if(in != null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                }
                if(client != null){
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                }
            }

        }
        System.out.println("Client closing");

    }
}
