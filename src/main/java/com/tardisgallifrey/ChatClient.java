package com.tardisgallifrey;

public class ChatClient {
    public static void main(String[] args) {

        Runnable client = new ChatSocket("localhost", 6100);

        Thread c = new Thread(client);
        c.setName("Client 1");
        System.out.println(c.getName()+" starting.");
        c.start();

    }
}