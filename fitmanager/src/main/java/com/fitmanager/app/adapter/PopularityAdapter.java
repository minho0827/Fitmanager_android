package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fitmanager.app.R;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.fragment.PopularityVideoFragment;
import com.fitmanager.app.model.VideoVO;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class PopularityAdapter extends RecyclerView.Adapter<PopularityAdapter.ViewHolder> {
    private List<VideoVO> mVideoItems = new ArrayList<>();
    private SparseBooleanArray seletedItems;
    int totalVideoCount = 0;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    static final String TAG = "PopularityAdapter";
    PopularityVideoFragment mPopularityVideoFragment;


    private EndlessScrollListener endlessScrollListener;

    private void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

    public PopularityAdapter(PopularityVideoFragment popularityVideoFragment, List<VideoVO> popularityData) {
        if (popularityData == null) {
            throw new IllegalArgumentException("modelData must not be nul");
        }
        this.mPopularityVideoFragment = popularityVideoFragment;
        this.mVideoItems = popularityData;
        seletedItems = new SparseBooleanArray();
        setEndlessScrollListener(mPopularityVideoFragment);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_popularity_header, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_popularity_row, parent, false);

        }
        return new ViewHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (viewHolder.isHeader) {

        } else if (viewHolder.isCell) {

            Log.d(TAG, "## position: " + position + ", getItemCount: " + getItemCount());

            if(position == getItemCount() - 1) {
                if (endlessScrollListener != null) {
                    endlessScrollListener.onLoadMore(position);
                }
            }

            final VideoVO videoVO = getItem(position);
            if (videoVO != null) {
                Glide.with(mPopularityVideoFragment)
                        .load(videoVO.getProfileImg())
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(mPopularityVideoFragment.getContext()))
                        .crossFade()
                        .into(viewHolder.profileImg);


                Glide.with(mPopularityVideoFragment)
                        .load(videoVO.getImageUrl())
                        .centerCrop()
                        .crossFade()
                        .into(viewHolder.thumbnail);

                viewHolder.tvTitle.setText(videoVO.getTitle());
//                viewHolder.tvName.setText(videoVO.getName());
                viewHolder.tvViewCount.setText(videoVO.getViewHistoryCount() + "");
//                viewHolder.tvBookmarkCount.setText(videoVO.getBookmarkCount());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        callActivity(videoVO.getVideoId());
                    }
                });

            }
        }
    }

    private VideoVO getItem(int position) {
        if (position < 0) {
            return null;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return null;
        } else {
            if (mVideoItems.size() <= position - 1) {
                return null;
            }
            return mVideoItems.get(position - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_CELL;
        }
    }

    private void callActivity(int videoId) {

        Intent intent = new Intent(mPopularityVideoFragment.getActivity(), VideoActivity.class);
        Bundle extras = new Bundle();
        Log.i(TAG, "videoId" + videoId);
        extras.putInt("videoId", videoId);
        intent.putExtras(extras);
        mPopularityVideoFragment.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return mVideoItems.size() + 1;
    }

    public void setData(List<VideoVO> popularityList) {
        this.mVideoItems = popularityList;
        notifyDataSetChanged();
    }

    public int getTotalVideoCount() {
        return totalVideoCount;
    }

    public void setTotalVideoCount(int totalVideoCount) {
        this.totalVideoCount = totalVideoCount;

    }



    class ViewHolder extends RecyclerView.ViewHolder {

        // cell
        ImageView thumbnail;
        ImageView profileImg;
        TextView tvTitle;
//        TextView tvName;
        TextView tvBookmarkCount;
        TextView tvViewCount;
        boolean isCell;
        boolean isHeader;


        // header
        Button btnBodyFilter;
        Button btnCoachSearch;
        private Context mContext;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            mContext = itemView.getContext();

            if (viewType == TYPE_CELL) {
                isCell = true;
                thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
                profileImg = (ImageView) itemView.findViewById(R.id.profile_img);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
//                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvViewCount = (TextView) itemView.findViewById(R.id.tv_view_count);
//                tvBookmarkCount = (TextView) itemView.findViewById(R.id.bookmark_count);

            } else if (viewType == TYPE_HEADER) {
                isHeader = true;
                btnBodyFilter = (Button) itemView.findViewById(R.id.body_filter);
                btnCoachSearch = (Button) itemView.findViewById(R.id.btn_title_search);

                btnBodyFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopularityVideoFragment.callBodyTypeFilterActivity();

                    }
                });
                btnCoachSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "코치검색", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        }
    }

    public interface EndlessScrollListener {
        /**
         * Loads more data.
         * @param position
         * @return true loads data actually, false otherwise.
         */
        boolean onLoadMore(int position);
    }

}
