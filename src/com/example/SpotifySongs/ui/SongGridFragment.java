package com.example.SpotifySongs.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.SpotifySongs.R;
import com.example.SpotifySongs.models.Song;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

/**
 * Created by pcedrowski on 3/24/14.
 */
public class SongGridFragment extends Fragment {
    private static final String TAG = SongGridFragment.class.getCanonicalName();
    private Context mContext;
    private FragmentCallback mCallbacks;

    private GridView mGridView;
    private SongAdapter mAdapter;
    private ArrayList<Song> adapterData;

    @Override
    public void onCreate(Bundle savedBundleState){
        super.onCreate(savedBundleState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mContext = getActivity();
        mAdapter = new SongAdapter(mContext, getAdapterData());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args){
        super.onCreateView(inflater, container, args);

        View rootView = inflater.inflate(R.layout.grid_fragment, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.grid_view);
        mGridView.setOnItemClickListener(gridViewClickListener);
        mGridView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().getActionBar().setTitle("Spotify Top Songs");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        mCallbacks = (FragmentCallback) activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    private AdapterView.OnItemClickListener gridViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Song selectedSong = (Song) view.getTag();
            mCallbacks.onItemSelected(selectedSong);
        }
    };

    public void setAdapterData(ArrayList<Song> adapterData) {
        this.adapterData = adapterData;

        refreshGrid();
    }

    public ArrayList<Song> getAdapterData() {
        if (adapterData == null)
            adapterData = new ArrayList<Song>();

        return adapterData;
    }

    public void refreshGrid(){
        if (mAdapter != null){
            mAdapter.setSongArrayList(adapterData);
            mAdapter.notifyDataSetChanged();
        } else
            Log.d(TAG, "mAdapter is null");
    }


    /** Custom Adapter extending a BaseAdapter responsible for loading data elements
     * into GridView. This uses a library called ImageLoader.
     */
    private class SongAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;
        private ArrayList<Song> songArrayList;
        private ImageLoader imageLoader;
        private DisplayImageOptions displayOptions;


        public SongAdapter(Context context, ArrayList<Song> songList){
            mContext = context;
            inflater = LayoutInflater.from(mContext);
            imageLoader = ImageLoader.getInstance();
            displayOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            if (songList == null)
                songArrayList = new ArrayList<Song>();
            else
                songArrayList = songList;
        }

        public void setSongArrayList(ArrayList<Song> newSongList){
            songArrayList = newSongList;
        }

        @Override
        public int getCount() {
            return songArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return songArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View gridItemView;
            if (convertView == null) {
                gridItemView = inflater.inflate(R.layout.song_grid_item, null);
            } else {
                gridItemView = convertView;
            }

            TextView songTitle = (TextView) gridItemView.findViewById(R.id.song_title);
            TextView songArtist = (TextView) gridItemView.findViewById(R.id.song_artist);
            ImageView albumArt = (ImageView) gridItemView.findViewById(R.id.album_image);

            Song song = songArrayList.get(position);

            songTitle.setText(song.getSongName());
            songArtist.setText(song.getSongArtist());
            imageLoader.displayImage(song.getArtworkUrl(), albumArt, displayOptions);

            gridItemView.setTag(song);
            return gridItemView;
        }
    }

    /** Interface for callbacks to Activity */
    public interface FragmentCallback {
        public void onItemSelected(Song song);
    }
}
