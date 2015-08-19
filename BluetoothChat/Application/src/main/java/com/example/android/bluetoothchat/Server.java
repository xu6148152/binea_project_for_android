package com.example.android.bluetoothchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by xubinggui on 8/19/15.
 */
public class Server {
    public static void main(String args[]){
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(23120);
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            while(true){
                socket.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                System.out.print("receive data " + s);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket != null){
                socket.close();
            }
        }
    }
}
