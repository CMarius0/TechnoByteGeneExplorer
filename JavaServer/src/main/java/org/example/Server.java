package org.example;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.*;

public class Server extends NanoHTTPD {

    public Server() throws IOException {
        super(1080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Server started");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String type = session.getParms().get("type");
        Response res;
        switch (type) {
            case "GetGene":
                var x = APICalls.getGeneInfoFromID(APICalls.getGeneIdFromSymbol(session.getParms().get("gene")));
                if (x==null){
                    JSONObject obj = new JSONObject();
                    obj.put("Error", "No matching Gene Name");
                    res = newFixedLengthResponse(obj.toString());
                    break;
                }
                res = newFixedLengthResponse(x.toString());
                break;
            default:
                JSONObject obj = new JSONObject();
                obj.put("Error", "Error");
                res = newFixedLengthResponse(obj.toString());
        }
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type");

        return res;
    }

    public static void main(String[] args) {
        try {
            new Server();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }
}
