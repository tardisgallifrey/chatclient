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
    Scanner input;
    private final String hostName;
    private final int hostPort;
    private String message = "";    //cannot be null when read by while()
    private boolean firstTime = false;

    ChatSocket(String host, int port){
        this.hostName = host;
        this.hostPort = port;
    }

    @Override
    public void run() {
        input = new Scanner(System.in);
        System.out.println("Enter your name or handle for ID.");
        String clientName = input.nextLine();

        while(!message.equals("exit")) {
            try {

                client = new Socket(this.hostName, this.hostPort);
                client.setKeepAlive(true);
                if (client.isConnected() && !firstTime) {
                    System.out.println("Chat connection made...");
                    firstTime = true;
                }

                //create objects for in and out messages
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                System.out.println("Enter your message below. Use 'exit' to quit.");

                //get message from user or exit
                message = input.nextLine();

                //don't send exit as message to server
                if (!message.equals("exit")){
                    out.println(clientName);
                    out.println(": sends-> "+message);
                    out.flush();

                    if ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }else{
                        System.out.println("no message received");
                    }
                }


            } catch (IOException e) {
                if (e.getMessage().contains("refuse")) {
                    System.out.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }

            }
            finally{

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
                if(Thread.currentThread().isInterrupted()){
                    System.exit(1);
                }
            }

        }
        System.out.println("Client closing");

    }
}
