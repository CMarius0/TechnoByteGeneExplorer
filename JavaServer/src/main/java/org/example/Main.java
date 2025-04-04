package org.example;

import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) {
        try {
            APICalls.request("list/hsa");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}