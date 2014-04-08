package com.example.SpotifySongs.managers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.example.SpotifySongs.services.NetworkService;
import com.example.SpotifySongs.SpotifySongs;

import java.util.UUID;

/**
 * Created by pcedrowski on 3/24/14.
 */
public class SongManager {
    private static final SongManager INSTANCE = new SongManager();
    private Context mContext;

    /** Broadcast variables */
    public static final String REQUESTED_RESULT = "REQUESTED_RESULT";
    public static final String SERVICE_CALLBACK = "SERVICE_CALLBACK";

    /** Extra bundle variables */
    public static final String RANGE_EXTRA = "range";
    public static final String REQUEST_ID_EXTRA = "request_id";

    public static SongManager getInstance(){
        return INSTANCE;
    }

    private SongManager(){
        mContext = SpotifySongs.getContext();
    }

    public long getSongList(String range){
       long requestId = generateRequestID();

        ResultReceiver serviceCallback = new ResultReceiver(null){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleReturnRequest(resultCode, resultData);
            }
        };

        Intent serviceIntent = new Intent(mContext, NetworkService.class);
        serviceIntent.putExtra(RANGE_EXTRA, range);
        serviceIntent.putExtra(REQUEST_ID_EXTRA, requestId);
        serviceIntent.putExtra(SERVICE_CALLBACK, serviceCallback);

        mContext.startService(serviceIntent);

        return requestId;
    }

    /** Broadcast the returning results via callback */
    private void handleReturnRequest(int resultCode, Bundle resultData){
        Intent resultBroadcast = new Intent(REQUESTED_RESULT);

        /** Broadcast the following results:
         * 1) Original request_id
         * 2) Result code of network operation
         * 3) The data in an ArrayList
         */
        resultBroadcast.putExtras(resultData);
        mContext.sendBroadcast(resultBroadcast);
    }


    /** Helper methods for generating request IDs */
    private long generateRequestID() {
        long requestId = UUID.randomUUID().getLeastSignificantBits();
        return requestId;
    }
}
