package com.example.trabalho2_150926.Conexao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Conexao {
    public static final String BASE_URL = "http://192.168.100.7:8080/estudantes/";

    public static String requisitar(String urlEndpoint, String metodo, String corpoJson) throws Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlEndpoint);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(metodo);
            con.setConnectTimeout(6000);
            con.setReadTimeout(6000);
            con.setRequestProperty("Accept", "application/json");

            if (corpoJson != null && (metodo.equals("POST") || metodo.equals("PUT"))) {
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = corpoJson.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            int code = con.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? con.getInputStream() : con.getErrorStream();
            if (is == null) return "";

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                sb.append(linha);
            }
            return sb.toString();
        } finally {
            if (con != null) con.disconnect();
        }
    }
}