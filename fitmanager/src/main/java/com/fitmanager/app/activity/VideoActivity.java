package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.VideoInfoAdaper;
import com.fitmanager.app.model.BookmarkVO;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.AlertDialogUtil;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.OnSingleClickListener;
import com.fitmanager.app.util.Utils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VideoActivity extends AppCompatActivity implements VideoInfoAdaper.BookmarkClickListener,
        ExoPlayer.EventListener {
    private static FitProgressBar mProgressBar = new FitProgressBar();
    final static String TAG = "VideoActivity";

    static final boolean GRID_LAYOUT = false;
    private LinearLayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private VideoInfoAdaper mVideoInfoAdaper;
    private VideoVO mVideoData = null;
    private Context mContext;
    private int mVideoId;
    private int mCoachId;
    private Uri mVideoUrl;
    private String mUrl;
    private long mPlayPosition;
    private boolean isAgreedWifi = false;

    @BindView(R.id.btn_fullscreen)
    AppCompatButton btnFullScreen;
    private List<VideoVO> mRecommendVideoList = new ArrayList<>();

    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbarCollapse)
    CollapsingToolbarLayout collapsingToolbar;

    protected void onResume() {
        super.onResume();


        if (mVideoUrl != null) {
            setExoPlayer(mVideoUrl);
            playerView.getPlayer().seekTo(mPlayPosition);

            if (isAgreedWifi) {
                playerView.getPlayer().setPlayWhenReady(true);
            } else {
                checkMobileUseAgree();
            }
        }
        getRestVideoInfoService(true);
    }

    private void checkMobileUseAgree() {
        if (Utils.isWifiConnection(this) && isAgreedWifi == false) {
            AlertDialogUtil.showDoubleDialog(mContext, getString(R.string.msg_check_mobile_use_agree), new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    AlertDialogUtil.dismissDialog();
                    finish();
                }
            }, new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    AlertDialogUtil.dismissDialog();
                    finish();
                }
            });
        } else {
            isAgreedWifi = true;
        }
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

        // add EventListener
        player.addListener(this);
        player.setPlayWhenReady(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        setToolbar();

        if (GRID_LAYOUT) {
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mLayoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        getIntentInit();
        collapsingToolbar.setTitle(" ");
        ActionBar actionBar = getSupportActionBar();
        mVideoInfoAdaper = new VideoInfoAdaper(mContext, mVideoData, mVideoId);
        mRecyclerView.setAdapter(mVideoInfoAdaper);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");
        getRestVideoInfoService(false);
        getVideoRestIsBookmarkService();

        btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPlayPosition = playerView.getPlayer().getCurrentPosition();
                Intent intent = new Intent(mContext, VideoFullAcitivty.class);
                Bundle extras = new Bundle();
                extras.putLong("position", mPlayPosition);
                extras.putString("url", mUrl);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            Log.d(TAG, "onPlayerStateChanged: " + playbackState);
            playerView.getPlayer().seekTo(0);
            playerView.getPlayer().setPlayWhenReady(false);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mVideoId = bundle.getInt("videoId");
        Log.d(TAG, "videoId: " + mVideoId);
    }


    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "Back button clicked", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
    }


    public void getVideoRestIsBookmarkService() {

        if (LoginUtils.isLoggedIn() == true) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.SERVER_ADDR)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Map<String, Object> param = new HashMap<>();
            param.put("targetId", mVideoId);
            param.put("targetType", Constant.TargetType.VIDEO.value);
            param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());

            RestService service = retrofit.create(RestService.class);
            final Call<Integer> getIsBookmarkCall = service.getIsBookmark(param);

            getIsBookmarkCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    int bookmarkCount = response.body();
                    if (bookmarkCount > 0) {
                        mVideoInfoAdaper.setVideoBookmarkResultData(true);
                    } else {
                        mVideoInfoAdaper.setVideoBookmarkResultData(false);
                    }

                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "onFailure: getVideoRestIsBookmarkService rest 통신실패");

                }
            });
        }
    }

    public void getIsCoachBookmarkService() {


        if (LoginUtils.isLoggedIn() == true) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.SERVER_ADDR)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Map<String, Object> param = new HashMap<>();
            param.put("targetId", mCoachId);
            param.put("targetType", Constant.TargetType.COACH.value);
            param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());

            RestService service = retrofit.create(RestService.class);
            final Call<Integer> getIsBookmarkCall = service.getIsCoachBookmark(param);

            getIsBookmarkCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    int bookmarkCount = response.body();
                    if (bookmarkCount > 0) {
                        mVideoInfoAdaper.setCoachBookmarkResultData(true);
                    } else {
                        mVideoInfoAdaper.setCoachBookmarkResultData(false);
                    }
                    hideProgress();

                }


                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "onFailure: getVideoRestIsBookmarkService rest 통신실패");
                    hideProgress();
                }
            });
        }
        hideProgress();

    }

    private void getRestVideoInfoService(final boolean isBookmarkUpdate) {
        showProgress();

        Map<String, Object> param = new HashMap<>();
        param.put("targetType", Constant.TargetType.VIDEO.value);
        param.put("targetId", mVideoId);
        param.put("videoId", mVideoId);
        if (LoginUtils.isLoggedIn()) {
            MemberVO memberVO = LoginUtils.getLoginUserVO();
            param.put("memberId", memberVO.getMemberId());
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<VideoVO> videoVOCall = service.getVideoInfo(param);

        videoVOCall.enqueue(new Callback<VideoVO>() {
            @Override
            public void onResponse(Call<VideoVO> call, Response<VideoVO> response) {

                final VideoVO videoVO = response.body();
                if (videoVO != null) {
                    Log.d(TAG, "videoVO : " + videoVO);
                    if (isBookmarkUpdate) {
                        // 북마크 업데이트일 경우: bookmark count 만 업데이트 해준다.
                        mVideoInfoAdaper.setHeaderData(videoVO);
                    } else {
                        // 일반적으로 데이터 가져올 경우: 전체 데이터 업데이트 해준다.
                        mVideoData = videoVO;
                        mVideoId = mVideoData.getVideoId();
                        mVideoInfoAdaper.setHeaderData(videoVO);
                        mUrl = mVideoData.getVideoUrl();
                        mVideoUrl = Uri.parse(mVideoData.getVideoUrl());
                        mCoachId = mVideoData.getCoachId();
                        setExoPlayer(mVideoUrl);
                        Log.i(TAG, "mCoachId : " + mCoachId);

                        MemberVO memberVO = LoginUtils.getLoginUserVO();
                        String memberId = "";
                        if (memberVO != null) {
                            memberId = memberVO.getMemberId() + "";
                        }


                        HashMap map = new HashMap();
                        map.put("memberId", memberId);
                        map.put("exerciseType", mVideoData.getExerciseType());
                        map.put("bodypart01", mVideoData.getBodypart01());
                        map.put("videoId", mVideoData.getVideoId());

                        selectRecommendVideoListService(map);
                        // mCoachId 를 가져온 다음에 호출해야 함.
                        getIsCoachBookmarkService();

                    }
                }
                hideProgress();

            }

            @Override
            public void onFailure(Call<VideoVO> call, Throwable t) {
                Log.e(TAG, "## onFailure: " + t.getMessage());
                hideProgress();
            }
        });
    }


    public void postVideoBookmarkService(boolean isSelected, int videoId) {
//        bookmark insert
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final Map<String, Object> param = new HashMap<>();
//        {target_id}, #{target_type}, #{member_id}
        param.put("targetId", videoId);
        param.put("targetType", Constant.TargetType.VIDEO.value);
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        param.put("bookmarkYn", isSelected ? "Y" : "N");

        Log.d(TAG, "postVideoBookmarkService: param: " + param);

        RestService service = retrofit.create(RestService.class);
        final Call<BookmarkVO> insertOrDeleteBookmarkRequestCall = service.insertOrDeleteBookmark(param);

        insertOrDeleteBookmarkRequestCall.enqueue(new Callback<BookmarkVO>() {
            @Override
            public void onResponse(Call<BookmarkVO> call, Response<BookmarkVO> response) {
                BookmarkVO bookmarkVO = response.body();
                if (bookmarkVO != null) {
                    // 서버 리턴값 - 북마크 설정: 1, 북마크 해제: 2
                    String msg;
                    if (bookmarkVO.getResultType() == 1) {
                        mVideoInfoAdaper.setVideoBookmarkResultData(true);
                        msg = "북마크 되었습니다.";
                    } else {
                        mVideoInfoAdaper.setVideoBookmarkResultData(false);
                        msg = "북마크 해제 되었습니다.";
                    }

                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    // 총 북마크 업데이트
                    mVideoInfoAdaper.updateBookmarkCount(bookmarkVO.getBookmarkCount());
//                    getVideoRestIsBookmarkService();
                }
            }

            @Override
            public void onFailure(Call<BookmarkVO> call, Throwable t) {
                Log.e(TAG, "onFailure: rest 통신실패");
            }
        });
    }

    //        bookmark insert
    private void postCoachBookmarkService(boolean isSelected, int coachId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Map<String, Object> param = new HashMap<>();
        param.put("targetId", coachId);
        param.put("targetType", Constant.TargetType.COACH.value);
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        param.put("bookmarkYn", isSelected ? "Y" : "N");
        Log.d(TAG, "postCoachBookmarkService: param: " + param);

        RestService service = retrofit.create(RestService.class);
        final Call<BookmarkVO> insertOrDeleteBookmarkRequest = service.insertOrDeleteBookmark(param);

        insertOrDeleteBookmarkRequest.enqueue(new Callback<BookmarkVO>() {
            @Override
            public void onResponse(Call<BookmarkVO> call, Response<BookmarkVO> response) {
                BookmarkVO bookmarkVO = response.body();
                if (bookmarkVO != null) {
                    String msg;
                    if (bookmarkVO.getResultType() == 1) {
                        mVideoInfoAdaper.setCoachBookmarkResultData(true);
                        msg = "북마크 되었습니다";
                    } else {
                        mVideoInfoAdaper.setCoachBookmarkResultData(false);
                        msg = "북마크 해제 되었습니다";
                    }
                    mVideoInfoAdaper.updateCoachBookmarkCount(bookmarkVO.getBookmarkCount());
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookmarkVO> call, Throwable t) {
                Log.e(TAG, "onFailure: postVideoBookmarkService rest 통신실패");
            }
        });
    }


    private void selectRecommendVideoListService(Map<String, Object> param) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<List<VideoVO>> insertHistoryCall = service.selectRecommendVideoList(param);

        insertHistoryCall.enqueue(new Callback<List<VideoVO>>() {
            @Override
            public void onResponse(Call<List<VideoVO>> call, Response<List<VideoVO>> response) {

                mRecommendVideoList = response.body();
                if (CollectionUtils.isNotEmpty(mRecommendVideoList)) {
                    mVideoInfoAdaper.setCellData(mRecommendVideoList);

                } else {
                    List<VideoVO> emptyList = new ArrayList<>();
                    VideoVO emptyVO = new VideoVO();
                    emptyVO.setCellType(1); // empty cell type
                    emptyList.add(emptyVO);
                    mVideoInfoAdaper.setCellData(emptyList);

                }

            }

            @Override
            public void onFailure(Call<List<VideoVO>> call, Throwable t) {
                Log.e(TAG, "## onFailure: " + t.getMessage());

            }
        });
    }


    @Override
    protected void onDestroy() {
        if (playerView != null && playerView.getPlayer() != null) {
            playerView.getPlayer().stop();
        }

        super.onDestroy();
    }

    @Override
    public void videoOnBookmarkClicked(boolean isSelected, int videoId) {
        postVideoBookmarkService(isSelected, videoId);
    }

    @Override
    public void coachOnBookmarkClicked(boolean isSelected, int coachId) {
        postCoachBookmarkService(isSelected, coachId);
    }

    @Override
    protected void onStop() {
        if (playerView != null && playerView.getPlayer() != null) {
//            playerView.getPlayer().stop();
            playerView.getPlayer().setPlayWhenReady(false);
        }
        super.onStop();
    }

    public void callVideoActivity(int videoId) {
        Log.i(TAG, "videoId: " + videoId);
        Intent intent = new Intent(this, VideoActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("videoId", videoId);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.show(mContext);
        }
    }


    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
    }
}
