package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class APICalls {
    public static ArrayList<String> request(String parameter) throws MalformedURLException {
        try {
            URL url = new URL("https://rest.kegg.jp/" + parameter);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> response = new ArrayList<>();
            String inputLine;
            int n = 1000;
            while ((inputLine = reader.readLine()) != null && n!=0) {
                response.add(inputLine);
                n--;
            }
            reader.close();
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
