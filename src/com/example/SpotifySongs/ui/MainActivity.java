package com.example.SpotifySongs.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.SpotifySongs.R;
import com.example.SpotifySongs.managers.SongManager;
import com.example.SpotifySongs.models.Song;

import java.util.ArrayList;

public class MainActivity extends Activity implements SongGridFragment.FragmentCallback{
    private static final String TAG = MainActivity.class.getCanonicalName();
    private Context mContext;
    private SongGridFragment gridFragment;
    private BroadcastReceiver mReceiver;
    private SongManager networkManager;
    private ConnectivityHelper connectivityHelper;
    private Button refreshButton;

    private long songRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        networkManager = SongManager.getInstance();
        setContentView(R.layout.main);

        refreshButton = (Button) findViewById(R.id.retry_load);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSongList();
            }
        });

        if (savedInstanceState == null){
            connectivityHelper = new ConnectivityHelper(this);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (gridFragment == null)
                gridFragment = new SongGridFragment();
            ft.replace(R.id.fragment_container_master, gridFragment, "grid_fragment");
            ft.commit();

            refreshSongList();
        }

        if (gridFragment == null)
            gridFragment = new SongGridFragment();
    }

    @Override
    public void onResume(){
        super.onResume();

        gridFragment = (SongGridFragment) getFragmentManager().findFragmentByTag("grid_fragment");

        /** Refresh the song list if adapter is empty OR refresh the grid view. */
        if (gridFragment.getAdapterData() != null && gridFragment.getAdapterData().size() == 0){
            refreshSongList();
        } else {
            gridFragment.refreshGrid();
        }

        IntentFilter filter = new IntentFilter(SongManager.REQUESTED_RESULT);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long receivedRequestId = intent.getLongExtra(SongManager.REQUEST_ID_EXTRA, 0);

                if (receivedRequestId == songRequest){
                    ArrayList<Song> songsList = intent.getParcelableArrayListExtra("songs");

                    if (gridFragment != null) {
                        gridFragment.setAdapterData(songsList);
                    } else
                        Log.d(TAG, "gridFragment is null");
                }
            }
        };

        mContext.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause(){
        super.onPause();

        if (mReceiver != null) {
            try {
                mContext.unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Problem unregistering Broadcast Receiver");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed(){
        Fragment f = getFragmentManager().findFragmentByTag("song_detail");
        if (f != null && f.isAdded()){
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.remove(f);
            fragmentTransaction.commit();
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_refresh:
                refreshSongList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(Song selectedSong) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        Bundle args = new Bundle();
        args.putParcelable("song", selectedSong);
        Fragment songDetailFragment = new SongDetailFragment();
        songDetailFragment.setArguments(args);
        ft.replace(R.id.fragment_container_master, songDetailFragment, "song_detail");
        ft.addToBackStack("song_detail");
        ft.commit();
    }

    private synchronized void refreshSongList(){
        if (connectivityHelper == null)
            connectivityHelper = new ConnectivityHelper(this);

        if (connectivityHelper.isConnected()){
            Toast.makeText(mContext, "Getting top streamed songs...", Toast.LENGTH_LONG).show();

            findViewById(R.id.fragment_container_master).setVisibility(View.VISIBLE);
            findViewById(R.id.connectivity_layout).setVisibility(View.GONE);
            songRequest = networkManager.getSongList("latest");
        } else {
            findViewById(R.id.fragment_container_master).setVisibility(View.GONE);
            findViewById(R.id.connectivity_layout).setVisibility(View.VISIBLE);
        }
    }
}
