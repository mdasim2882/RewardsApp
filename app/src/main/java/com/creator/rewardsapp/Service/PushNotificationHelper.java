package com.creator.rewardsapp.Service;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PushNotificationHelper {
    private final static String AUTH_KEY_FCM = "AAAAi_PFvko:APA91bGQiAmqQheIrHe3B4tMtfgTpPHWtVqw4EUjL8hBPZXtphAO0nfN8ZbZyB9oSPR0AL_OUfwPG_ujwImjFAV52TYtRKUizKf94FHs3zp4_-vk787YVupnkMMdrdZf6N83yLozcYNA";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private final String TAG = getClass().getSimpleName();

    public String sendPushNotification(String deviceToken)
            throws IOException {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();


        try {

            Log.d(TAG, "sendPushNotification: "+deviceToken.trim());
            json.put("to",deviceToken.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject info = new JSONObject();
        try {
            info.put("title", "Hey! folks"); // Notification title
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            info.put("body", "Congrats! You won in of the selected offers!"); // Notification
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        try {
//            info.put("time_to_live", 60*1000*8); // Notification
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            info.put("time_to_live", 60*1000*24); // Notification
//        } catch (JSONException e) {
//        e.printStackTrace();
//    }
        // body
        try {
            json.put("notification", info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            Log.d(TAG, "sendPushNotification: Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                Log.d(TAG, "sendPushNotification: "+output);
            }
            result = "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE: "+e.getMessage() ;
        }
        Log.d(TAG, "sendPushNotification: FCM Notification is sent successfully");

        return result;

    }
}