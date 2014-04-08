package com.example.SpotifySongs.services;

import android.util.Log;
import com.example.SpotifySongs.models.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pcedrowski on 3/24/14.
 */
public class SpotifyProcessor {
    private static final String TAG = SpotifyProcessor.class.getCanonicalName();
    private static final String SPOTIFY_URL = "http://charts.spotify.com/api/charts/most_streamed/us/%s";
    private int status;
    private String SPOTIFY_REQUEST_URL;
    private String request;
    private ArrayList<Song> songArrayList;

    private int rankCount;

    public SpotifyProcessor(){
        songArrayList = new ArrayList<Song>();
        rankCount = 1;
    }

    public ArrayList<Song> getSpotifyList(String request){
        this.request = request;
        SPOTIFY_REQUEST_URL = String.format(SPOTIFY_URL, this.request);

        parseJson(getSpotifyResponse());

        return songArrayList;
    }

    public int getStatus(){
        return status;
    }

    private String getSpotifyResponse() {
        rankCount = 1;
        try {
            URL url = new URL(SPOTIFY_REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.connect();
            status = connection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            status = 501;
        } catch (IOException ex) {
            status = 502;
        }
        return null;
    }

    private void parseJson(String response){
        if (response == null)
            return;

        try {
            JSONObject tracksObject = new JSONObject(response);
            JSONArray songArray = tracksObject.getJSONArray("tracks");
            for (int i = 0; i < songArray.length(); i++){
                JSONObject songObject = songArray.getJSONObject(i);
                Song song = new Song(songObject, this.request, this.rankCount);
                songArrayList.add(song);
                rankCount++;
            }

        } catch (JSONException e){
            Log.d(TAG, "JSON Exception: " + e.getLocalizedMessage());
            status = 505;
        }
    }

}
