package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.util.Utils;

import java.util.List;

public class BookmarkVideoAdapter extends RecyclerView.Adapter<BookmarkVideoAdapter.ViewHolder> {
    Context mContext;
    private List<VideoVO> mVideoHistoryItems;
    private final static String TAG = "BookmarkVideoAdapter";
    private boolean mVideoIsBookmarked = false;

    public BookmarkVideoAdapter(Context context, List<VideoVO> videoHistoryItems) {
        mContext = context;
        mVideoHistoryItems = videoHistoryItems;

    }

    public void setVideoBookmarkResultData(boolean isBookmarked) {
        Log.d(TAG, "onResponse: 북마크 안되어 있음.");
        mVideoIsBookmarked = isBookmarked;
        notifyDataSetChanged();

    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_history, parent, false);
        return new ViewHolder(view);
    }

    public void setData(List<VideoVO> historyList) {
        this.mVideoHistoryItems = historyList;
        notifyDataSetChanged();
    }

    public void removeItemAt(int position) {
        if (position < mVideoHistoryItems.size()) {
            mVideoHistoryItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final VideoVO videoVO = mVideoHistoryItems.get(position);

        if (videoVO != null) {
            viewHolder.tvTitle.setText(videoVO.getTitle());
            viewHolder.tvExerciseType.setText(Utils.getExerciseType(videoVO.getExerciseType()));
            viewHolder.tvBodyPart01.setText(Utils.getBodyType(videoVO.getBodypart01()));
            viewHolder.tvBodyPart01.setBackgroundResource(Utils.getBodyByColorType(videoVO.getBodypart01()));
            viewHolder.tvBodyPart02.setText(Utils.getBodyType(videoVO.getBodypart02()));
            viewHolder.tvBodyPart02.setBackgroundResource(Utils.getBodyByColorType(videoVO.getBodypart02()));
            viewHolder.tvExerciseType.setBackgroundResource
                    (Utils.getExerciseByColorType(videoVO.getExerciseType()));
            viewHolder.tvBodyPart01.setText(Utils.getBodyType(videoVO.getBodypart01()));
            viewHolder.frameLayoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOptionMenu(v, position);
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callActivity(videoVO.getVideoId());

                }
            });
        }

    }

    private void callActivity(int videoId) {
        Log.i(TAG, "videoId: " + videoId);
        Intent intent = new Intent(mContext, VideoActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("videoId", videoId);
        intent.putExtras(extras);
        mContext.startActivity(intent);

    }


    @Override
    public int getItemCount() {
        return mVideoHistoryItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLevelIcon01, imgLevelIcon02, imgLevelIcon03;

        TextView tvTitle;
        TextView tvLevel;
        TextView tvExerciseType;
        TextView tvBodyPart01;
        TextView tvBodyPart02;
        Button btnMore;
        FrameLayout frameLayoutMore;


        public ViewHolder(View itemView) {

            super(itemView);
            frameLayoutMore = (FrameLayout) itemView.findViewById(R.id.frame_layout_more);

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


    public void openOptionMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.video_history_menu_items, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
//                        mCallback.videoMoreBtnClicked(false,
//                                mVideoHistoryItems.get(position).getVideoId());

                        break;
                    case R.id.share:
                        Toast.makeText(mContext, "22222222", Toast.LENGTH_SHORT).show();

                        break;
                }
                return true;
            }
        });
        popup.show();
    }

//    public interface VideoMoreBtnListener {
//        void videoMoreBtnClicked(boolean isSelected, int videoId);
//
//    }


}
