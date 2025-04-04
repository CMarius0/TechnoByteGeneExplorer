package org.example;

import java.net.MalformedURLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<String> lista = APICalls.request("get/hsa:102466751");
            if (lista != null)
                for (var x: lista)
                    System.out.println(x);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}