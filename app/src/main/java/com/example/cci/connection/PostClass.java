package com.example.cci.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class PostClass {
    static InputStream veri;
    static String veri_string;
    int responseCode = 0;
    String line = null;

    public PostClass() {
        // TODO Auto-generated constructor stub
    }

    public  String httpPost(String apiurl, String method) {
        try {

            URL url = new URL(apiurl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            //if (method == "POST") {
            //} else {
            if (method.equals("GET")) {
                String urlParameters = "fizz=buzz";
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "tr-TR,tr;q=0.8,en-US;q=0.6,en;q=0.4");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                //connection.setConnectTimeout(15000);
                //connection.setReadTimeout(15000);
                connection.connect();
                try {
                    responseCode = connection.getResponseCode();
                    final StringBuilder output = new StringBuilder("Request URL " + url);
                    if(responseCode==400){
                        BufferedReader br_ = new BufferedReader(new InputStreamReader(connection
                                .getErrorStream()));
                        StringBuilder sb_ = new StringBuilder();
                        line = null;
                        while((line = br_.readLine()) != null ) {
                            sb_.append(line);
                        }
                        br_.close();
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    line = null;
                    while((line = br.readLine()) != null ) {
                        sb.append(line);
                    }
                    br.close();

                    veri_string = sb.toString();

                } catch (Exception e){
                    veri_string = null;
                    e.printStackTrace();
                }
            }
            //}
            connection.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return veri_string;
    }
}