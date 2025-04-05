package org.example.APICallers;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface APICaller {
    HttpURLConnection getConnection(String link) throws IOException;
}
