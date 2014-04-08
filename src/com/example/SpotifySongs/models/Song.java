package com.example.SpotifySongs.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pcedrowski on 3/24/14.
 */
public class Song implements Parcelable{
    private String albumUrl;
    private String albumName;
    private String artistName;
    private String artistUrl;
    private String trackName;
    private String trackUrl;
    private String artworkUrl;

    private long streamCount;
    private int songRank;
    public String dateRange;

    /** The Song model that the whole application surrounds.
     *
     * @param response is a JSONObject obtained from the array of JSONOBjects received from the server.
     * @param dateRange is the range of dates that this song is associated with. (ex: latest, 03-23-2014, etc)
     * @param rank is the rank number in the given dateRange.
     */
    public Song(JSONObject response, String dateRange, int rank){
        /** Response from server is in the following format: */
        //    date: "2014-03-16",
        //    country: "US",
        //    track_url: "https://play.spotify.com/track/4SGPZVMQoFWSh2yvbThK2o",
        //    track_name: "Team",
        //    artist_name: "Lorde",
        //    artist_url: "https://play.spotify.com/artist/163tK9Wjr9P9DmM0AVK7lm",
        //    album_name: "Team",
        //    album_url: "https://play.spotify.com/album/4d6FkNbDWPNN08PFrk52rt",
        //    artwork_url: "http://o.scdn.co/300/955e4819aa36d45e2c2e3fb7b41dc6e93deb7ead",
        //    num_streams: 1312072
        try {
            trackName = response.getString("track_name");
            trackUrl = response.getString("track_url");
            streamCount = response.getInt("num_streams");

            artistName = response.getString("artist_name");
            artistUrl = response.getString("artist_url");

            albumName = response.getString("album_name");
            albumUrl = response.getString("album_url");

            artworkUrl = response.getString("artwork_url");

            this.dateRange = dateRange;
            this.songRank = rank;

        } catch (JSONException e){
            Log.d("SONG", "Json exception: " + e.getLocalizedMessage());
        }
    }

    public String getSongName(){
        return trackName;
    }
    public String getArtworkUrl(){
        return artworkUrl;
    }
    public String getSongArtist(){
        return artistName;
    }
    public String getAlbumName(){
        return albumName;
    }
    public long getStreamCount(){
        return streamCount;
    }
    public int getSongRank(){
        return songRank;
    }
    public String getDateRange(){
        return dateRange;
    }

    /** Parcel data */
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public Song (Parcel in){
        this.trackName = in.readString();
        this.trackUrl = in.readString();
        this.albumUrl = in.readString();
        this.albumName = in.readString();
        this.artistName = in.readString();
        this.artistUrl = in.readString();
        this.artworkUrl = in.readString();
        this.streamCount = in.readLong();
        this.songRank = in.readInt();
        this.dateRange = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.trackName);
        out.writeString(this.trackUrl);
        out.writeString(this.albumUrl);
        out.writeString(this.albumName);
        out.writeString(this.artistName);
        out.writeString(this.artistUrl);
        out.writeString(this.artworkUrl);
        out.writeLong(this.streamCount);
        out.writeInt(this.songRank);
        out.writeString(this.dateRange);
    }
}
