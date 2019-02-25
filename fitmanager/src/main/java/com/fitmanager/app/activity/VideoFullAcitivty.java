package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fitmanager.app.R;
import com.fitmanager.app.model.VideoVO;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoFullAcitivty extends AppCompatActivity {
    final static String TAG = "VideoFullAcitivty";

    private VideoVO mVideoData = null;
    private Context mContext;
    private String mUrl;
    private long mPosition;

    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;

    protected void onResume() {
        super.onResume();
    }

    private void setExoPlayer(Uri videoUrl) {
        // 1. Create a default TrackSelector
//        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);

        playerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(this, "FitManager"));
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(videoUrl,
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        player.prepare(videoSource);

        player.seekTo(mPosition);
        player.setPlayWhenReady(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_video_full);

        ButterKnife.bind(this);

        getIntentInit();
        setExoPlayer(Uri.parse(mUrl));
    }


    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mUrl = bundle.getString("url");
        mPosition = bundle.getLong("position");

        Log.d(TAG, "mUrl: " + mUrl);
        Log.d(TAG, "mPosition: " + mPosition);
    }

    @Override
    protected void onDestroy() {
        if (playerView != null && playerView.getPlayer() != null) {
            playerView.getPlayer().stop();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (playerView != null && playerView.getPlayer() != null) {
            playerView.getPlayer().stop();
        }
        super.onStop();
    }

}
