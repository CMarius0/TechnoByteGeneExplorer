package org.example;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        System.out.println("Listening");
        try(ServerSocket server = new ServerSocket(1080)) {
            Socket s = server.accept();

            BufferedReader bis = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());

            System.out.println("Connected"+ bis.readLine());
            JSONObject obj = new JSONObject(bis.readLine());
            if ( obj.has("key") && obj.get("key") == "test") {
                JSONObject send = new JSONObject();
                send.append("test","hielau");
                bos.write(obj.toString().getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
