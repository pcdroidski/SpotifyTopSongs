package com.example.SpotifySongs;

import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by pcedrowski on 3/24/14.
 */
public class SpotifySongs extends Application {
    private static SpotifySongs INSTANCE;
    private static Context appContext;

    public static SpotifySongs getInstance(){
        return INSTANCE;
    }
    public static Context getContext(){
        return appContext;
    }


    @Override
    public void onCreate(){
        super.onCreate();

        INSTANCE = this;
        appContext = this.getApplicationContext();

        /** Image loader init */
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();

        ImageLoader.getInstance().init(config);
    }
}
