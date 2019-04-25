package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fitmanager.app.R;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class VideoHistoryAdapter extends RecyclerView.Adapter<VideoHistoryAdapter.ViewHolder> {
    Context mContext;
    private List<VideoVO> mVideoHistoryItems;
    private final static String TAG = "VideoHistoryAdapter";
    VideoMoreBtnListener mCallback;

    public VideoHistoryAdapter(Context context, List<VideoVO> videoHistoryItems) {
        mContext = context;
        mVideoHistoryItems = videoHistoryItems;
        mCallback = (VideoHistoryAdapter.VideoMoreBtnListener) context;



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
            notifyDataSetChanged();

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


            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final CharSequence[] items = {"감상한 동영상에서 삭제", "동영상 북마크하기", "공유"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setTitle("Select The Action");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                        }
                    });
                    builder.show();
                    return true;
                }
            });
            viewHolder.frame_layout_more.setOnClickListener(new View.OnClickListener() {
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


    public void openOptionMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.video_history_menu_items, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        HashMap map = new HashMap();
                        map.put("videoId", mVideoHistoryItems.get(position).getVideoId());
                        map.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
                        Log.i(TAG, "memberId : " + LoginUtils.getLoginUserVO().getMemberId());
                        Log.i(TAG, "videoId : " + mVideoHistoryItems.get(position).getVideoId());
                        mCallback.videoMoreBtnClicked(map, position);


                        break;
                    case R.id.share:
                        Toast.makeText(mContext, "22222222", Toast.LENGTH_SHORT).show();

                        break;
                }
//                mHistoryVideoList.get(position).getVideoId();
//                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle() + " position " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }



    @Override
    public int getItemCount() {
        return mVideoHistoryItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLevelIcon01, imgLevelIcon02, imgLevelIcon03;
        FrameLayout frame_layout_more;
        TextView tvTitle;
        TextView tvLevel;
        TextView tvExerciseType;
        TextView tvBodyPart01;
        TextView tvBodyPart02;
        ImageView thumbnail;


        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            frame_layout_more = (FrameLayout)itemView.findViewById(R.id.frame_layout_more);
            thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
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
    public interface VideoMoreBtnListener {
        void videoMoreBtnClicked(Map<String, Object> param, int position);

    }

}
