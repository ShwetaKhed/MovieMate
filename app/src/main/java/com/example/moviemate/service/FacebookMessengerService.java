package com.example.moviemate.service;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.AccessTokenSource;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class FacebookMessengerService {

    OkHttpClient httpClient = new OkHttpClient();
    private String accessToken = "EACF5VqagZBIoBACZASlZBOhwC4KoiVZC4OdgiC9w6kpZBi8yJZC4lMO0FGSiuoJVu4s1LIoK4dTZA4Pk6LnCOUM1wdFTIYIOE24kTqMCKtxK7IQYV4YL7V5dx86LioY1ZCnDYZBwOQoWmhZBQTdj6wegFm6xlKzFDg3YfCLyQG5nKz77MZAlMCpP9MN";


    public void sendMessage(String recipientId, String message){

        MediaType mediaType = MediaType.parse("application/json");

        String jsonBody = "{\"recipient\":{\"id\":\""+ recipientId + "\"},\"message\":{\"text\":\""+ message +"\"},\"messaging_type\":\"RESPONSE\"}";

        RequestBody requestBody = RequestBody.create(mediaType, jsonBody);

        String apiUrl = "https://graph.facebook.com/v16.0/106830202415972/messages?access_token=" + accessToken;

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            String responseJson = response.body().string();
            System.out.println(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
