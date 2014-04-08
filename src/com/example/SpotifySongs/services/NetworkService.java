package com.example.SpotifySongs.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.example.SpotifySongs.managers.SongManager;
import com.example.SpotifySongs.models.Song;

import java.util.ArrayList;


public class NetworkService extends IntentService {
    private static final String TAG = NetworkService.class.getCanonicalName();

    private Bundle originalBundle;
    private ResultReceiver mCallback;
    private SpotifyProcessor spotifyProcessor;

    public NetworkService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        originalBundle = intent.getExtras();
        mCallback = intent.getParcelableExtra(SongManager.SERVICE_CALLBACK);

        /** Processor to fetch and transcribe song list into an ArrayList. */
        spotifyProcessor = new SpotifyProcessor();
        ArrayList<Song> songList = spotifyProcessor.getSpotifyList("latest");
        originalBundle.putParcelableArrayList("songs", songList);

        mCallback.send(spotifyProcessor.getStatus(), originalBundle);
    }
}
