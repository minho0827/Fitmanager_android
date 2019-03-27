package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fitmanager.app.R;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.util.Utils;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private List<VideoVO> mVideoItems = null;
    Context mContext;
    private SparseBooleanArray seletedItems;
    static int TYPE_HEADER = 0;
    static int TYPE_CELL = 1;
    static int TYPE_FOOTER = 2;
    private VideoVO videoVO = new VideoVO();

    private CoachVO mCoachVO = new CoachVO();

    private final String TAG = "VideoListAdapter";


    public VideoListAdapter(Context context, List<VideoVO> mCoachVideoList) {
        if (mCoachVideoList == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.mContext = context;
        this.mVideoItems = mCoachVideoList;
        seletedItems = new SparseBooleanArray();
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
        return mVideoItems.size() + 2;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header_videolist, parent, false);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_footer_videolist, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_videolist_row, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;

        } else if (position == mVideoItems.size() + 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_CELL;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (viewHolder.isHeader) {
            viewHolder.videoNumCount.setText(mVideoItems.size() + "");
            viewHolder.coachName.setText(mCoachVO.getCoachName());

        } else if (viewHolder.isCell) {

            final VideoVO videoVO = getItem(position);
            if (videoVO != null) {
                if (videoVO.getCellType() == 0) {
                    viewHolder.frameLayoutNone.setVisibility(View.GONE);
                    viewHolder.cardView.setVisibility(View.VISIBLE);
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
                            .centerCrop()
                            .crossFade()
                            .into(viewHolder.thumbnail);

                } else {
                    viewHolder.cardView.setVisibility(View.GONE);
                    viewHolder.frameLayoutNone.setVisibility(View.VISIBLE);


                }
                final int videoId = videoVO.getVideoId();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callActivity(videoId);

                    }
                });
            }

        }

    }


    private VideoVO getItem(int position) {
        if (position < 0) {
            return null;
        } else if (getItemViewType(position) == TYPE_HEADER
                || getItemViewType(position) == TYPE_FOOTER) {
            return null;

        } else {
            if (mVideoItems.size() <= position - 1) {
                return null;
            }
            return mVideoItems.get(position - 1);
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

    public void setCoachVO(CoachVO coachVO) {
        this.mCoachVO = coachVO;
        notifyItemChanged(0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //cell
        ImageView imgPreview;
        ImageView imgLevelIcon01, imgLevelIcon02, imgLevelIcon03;
        LinearLayout linearLayout;
        FrameLayout frameLayout;
        TextView tvTitle;
        FrameLayout frameLayoutNone;
        //        TextView tvLevel;
        TextView tvExerciseType;
        TextView tvBodyPart01;
        TextView tvBodyPart02;
        TextView tvEmptyView;
        CardView cardView;
        boolean isCell;
        boolean isHeader;
        boolean isFooter;
        ImageView thumbnail;

        //Header
        TextView coachName;
        TextView videoNumCount;

        //Footer
        TextView tvFooter;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_CELL) {
                isCell = true;
                thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
                frameLayoutNone = (FrameLayout) itemView.findViewById(R.id.fl_layout_none);
                imgPreview = (ImageView) itemView.findViewById(R.id.img_preview);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                imgLevelIcon01 = (ImageView) itemView.findViewById(R.id.img_level_icon01);
                imgLevelIcon02 = (ImageView) itemView.findViewById(R.id.img_level_icon02);
                imgLevelIcon03 = (ImageView) itemView.findViewById(R.id.img_level_icon03);
                tvEmptyView = (TextView) itemView.findViewById(R.id.tv_empty);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
                frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_layout);
                tvExerciseType = (TextView) itemView.findViewById(R.id.tv_exerciseType);
                tvBodyPart01 = (TextView) itemView.findViewById(R.id.bodyPart_01);
                tvBodyPart02 = (TextView) itemView.findViewById(R.id.bodyPart_02);

            } else if (viewType == TYPE_HEADER) {
                isHeader = true;
                coachName = (TextView) itemView.findViewById(R.id.header_coachname);
                videoNumCount = (TextView) itemView.findViewById(R.id.video_num_count);

            } else if (viewType == TYPE_FOOTER) {
                isFooter = true;
                tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
            }


        }

    }


}
