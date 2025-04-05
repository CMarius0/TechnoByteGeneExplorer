package org.example;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class APICalls {
    public static ArrayList<String> request(String parameter) {
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
            while ((inputLine = reader.readLine()) != null && n != 0) {
                response.add(inputLine);
                n--;
            }
            reader.close();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getGeneIdFromSymbol(String symbol) {
        try {
            URL url = new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term=" + symbol + "[gene]+AND+Homo+sapiens[orgn]&retmode=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            JSONObject jsonObject = new JSONObject(reader.readLine());
            return Integer.parseInt(jsonObject.getJSONObject("esearchresult").getJSONArray("idlist").get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getGeneInfoFromID(Integer id) {
        try {
            URL url = new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=gene&id=" + id + "&retmode=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            JSONObject jsonObject = new JSONObject(reader.readLine());
            JSONObject test = jsonObject.getJSONObject("result").getJSONObject(id.toString());

            Set<String> keysToKeep = Set.of("name", "summary", "chromosome", "otherdesignations", "otheraliases");
            Set<String> keysToRemove = new HashSet<>();
            for (String key : test.keySet()) {
                if (!keysToKeep.contains(key)) {
                    keysToRemove.add(key);
                }
            }
            // Remove unwanted keys
            for (String key : keysToRemove) {
                test.remove(key);
            }
            return test;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

