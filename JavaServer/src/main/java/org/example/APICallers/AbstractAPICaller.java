package org.example.APICallers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AbstractAPICaller implements APICaller {
    protected String base_url;

    public AbstractAPICaller(String base_url) {
        this.base_url = base_url;
    }

    @Override
    public HttpURLConnection getConnection(String link) throws IOException {
        URL url = new URL(base_url + link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.connect();
        return connection;
    }
}
