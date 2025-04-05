package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends NanoHTTPD {

    public Server() throws IOException {
        super(1080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Server started");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String key = session.getParms().get("key");
        Response res = newFixedLengthResponse(APICalls.getGeneInfoFromID(APICalls.getGeneIdFromSymbol("TP53")).toString());
        res.addHeader("Access-Control-Allow-Origin", "*"); // 💥 This is what fixes it!
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type");

        return res;
    }

    public static void main(String[] args) throws IOException {
        try {
            new Server();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }
}
