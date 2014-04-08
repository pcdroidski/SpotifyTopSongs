package com.example.SpotifySongs.ui;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.SpotifySongs.R;
import com.example.SpotifySongs.models.Song;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.DecimalFormat;

/**
 * Created by pcedrowski on 3/24/14.
 */
public class SongDetailFragment extends Fragment {
    private Song songDetail;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        imageLoader = ImageLoader.getInstance();

        if (savedInstanceState != null)
            songDetail = savedInstanceState.getParcelable("song");

        if (songDetail == null)
            songDetail = getArguments().getParcelable("song");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args){
        super.onCreateView(inflater, container, args);

        View rootView = inflater.inflate(R.layout.song_detail_fragment, container, false);
        TextView songTitle = (TextView) rootView.findViewById(R.id.detail_song_name);
        TextView artistName = (TextView) rootView.findViewById(R.id.detail_artist_name);
        TextView albumTitle = (TextView) rootView.findViewById(R.id.detail_album_name);
        TextView dateRange = (TextView) rootView.findViewById(R.id.date_range);
        TextView songRank = (TextView) rootView.findViewById(R.id.detail_song_rank);
        TextView steamCount = (TextView) rootView.findViewById(R.id.detail_stream_count);
        ImageView albumArtworkImage = (ImageView) rootView.findViewById(R.id.detail_album_image);

        /** Set views to song information */
        songTitle.setText(songDetail.getSongName());
        artistName.setText(songDetail.getSongArtist());
        albumTitle.setText(songDetail.getAlbumName());
        dateRange.setText(songDetail.getDateRange());

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedStreamCount = formatter.format(songDetail.getStreamCount());
        steamCount.setText(formattedStreamCount);

        songRank.setText("# " + songDetail.getSongRank());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageLoader.displayImage(songDetail.getArtworkUrl(), albumArtworkImage, options);

        return rootView;
    }


    @Override
    public void onResume(){
        super.onResume();

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setTitle(songDetail.getSongName());
    }
}
