package com.example.SpotifySongs.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by pcedrowski on 3/24/14.
 */
public class ConnectivityHelper {
    private ConnectivityManager connectivityManager;

    public ConnectivityHelper(Activity activity){
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnected(){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
