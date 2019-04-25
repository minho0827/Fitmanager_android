package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fitmanager.app.R;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.util.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CoachVideoListAdapter extends RecyclerView.Adapter<CoachVideoListAdapter.ViewHolder> {

    private List<VideoVO> mVideoItems = null;
    Context mContext;
    private final String TAG = "VideoListAdapter";


    public CoachVideoListAdapter(Context context, List<VideoVO> mCoachVideoList) {
        if (mCoachVideoList == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.mContext = context;
        this.mVideoItems = mCoachVideoList;
    }

    private void callActivity(int videoId) {
        Log.i(TAG, "videoId: " + videoId);
        Intent intent = new Intent(mContext, VideoActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("videoId", videoId);
        intent.putExtras(extras);
        mContext.startActivity(intent);

    }

    public void setData(List<VideoVO> mVideoList) {
        this.mVideoItems = mVideoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "mVideoItems size : " + mVideoItems.size());
        return mVideoItems.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_history, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final VideoVO videoVO = mVideoItems.get(position);
        if (videoVO != null) {

            viewHolder.tvTitle.setText(videoVO.getTitle());
            viewHolder.tvExerciseType.setText(Utils.getExerciseType(videoVO.getExerciseType()));
            viewHolder.tvBodyPart01.setText(Utils.getBodyType(videoVO.getBodypart01()));
            viewHolder.tvBodyPart01.setBackgroundResource(Utils.getBodyByColorType(videoVO.getBodypart01()));
            viewHolder.tvBodyPart02.setText(Utils.getBodyType(videoVO.getBodypart02()));
            viewHolder.tvBodyPart02.setBackgroundResource(Utils.getBodyByColorType(videoVO.getBodypart02()));
            viewHolder.tvExerciseType.setBackgroundResource
                    (Utils.getExerciseByColorType(videoVO.getExerciseType()));
            getExerciseLevelDrawble(videoVO.getLevel(), viewHolder);


            Glide.with(mContext)
                    .load(videoVO.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .centerCrop())
                    .transition(withCrossFade())
                    .into(viewHolder.thumbnail);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callActivity(videoVO.getVideoId());

                }
            });
        }

    }


    private void getExerciseLevelDrawble(int level, ViewHolder viewHolder) {
        switch (level) {
            case 1:
                viewHolder.imgLevelIcon01.setVisibility(View.VISIBLE);
                viewHolder.imgLevelIcon02.setVisibility(View.INVISIBLE);
                viewHolder.imgLevelIcon03.setVisibility(View.INVISIBLE);
                break;
            case 2:
                viewHolder.imgLevelIcon01.setVisibility(View.VISIBLE);
                viewHolder.imgLevelIcon02.setVisibility(View.VISIBLE);
                viewHolder.imgLevelIcon03.setVisibility(View.INVISIBLE);
                break;
            case 3:
                viewHolder.imgLevelIcon01.setVisibility(View.VISIBLE);
                viewHolder.imgLevelIcon02.setVisibility(View.VISIBLE);
                viewHolder.imgLevelIcon03.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //cell
        ImageView imgLevelIcon01, imgLevelIcon02, imgLevelIcon03;
        TextView tvLevel;
        TextView tvTitle;
        TextView tvExerciseType;
        TextView tvBodyPart01;
        ImageView thumbnail;
        TextView tvBodyPart02;


        public ViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            imgLevelIcon01 = (ImageView) itemView.findViewById(R.id.img_level_icon01);
            imgLevelIcon02 = (ImageView) itemView.findViewById(R.id.img_level_icon02);
            imgLevelIcon03 = (ImageView) itemView.findViewById(R.id.img_level_icon03);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLevel = (TextView) itemView.findViewById(R.id.tv_level);
            tvExerciseType = (TextView) itemView.findViewById(R.id.tv_exerciseType);
            tvBodyPart01 = (TextView) itemView.findViewById(R.id.bodyPart_01);
            tvBodyPart02 = (TextView) itemView.findViewById(R.id.bodyPart_02);


        }

    }


}
