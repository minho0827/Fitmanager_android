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

import com.fitmanager.app.R;
import com.fitmanager.app.activity.CoachInfomationActivity;
import com.fitmanager.app.model.CoachViewHistoryVO;
import com.fitmanager.app.util.ImageUtils;

import java.util.List;

public class BookmarkCoachAdapter extends RecyclerView.Adapter<BookmarkCoachAdapter.ViewHolder> {
    Context mContext;
    private List<CoachViewHistoryVO> mBookmarkCoachList;
    private int mCoachId;
    private static final String TAG = "BookmarkCoachAdapter";

    public BookmarkCoachAdapter(Context context, List<CoachViewHistoryVO> mBookmarkCoachItems) {
        mContext = context;
        mBookmarkCoachList = mBookmarkCoachItems;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_coach_bookmark_item_row, parent, false);
        return new ViewHolder(view);
    }

    public void setData(List<CoachViewHistoryVO> bookmarkCoachList) {
        this.mBookmarkCoachList = bookmarkCoachList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final CoachViewHistoryVO coachViewHistoryVO = mBookmarkCoachList.get(position);

        if (coachViewHistoryVO != null) {


            ImageUtils.getProfileImage(mContext, viewHolder.imgProfile,
                    coachViewHistoryVO.getProfileImg());
            viewHolder.coachName.setText(coachViewHistoryVO.getCoachName());

            if (coachViewHistoryVO.getMeal_updatedCount() > 0) {
                viewHolder.tvUpdateMealCount.setVisibility(View.VISIBLE);
                viewHolder.tvUpdateMealCount.setText("+" + coachViewHistoryVO.getMeal_updatedCount() + "개 의 식단");
            } else {
                viewHolder.tvUpdateMealCount.setVisibility(View.INVISIBLE);
            }

            if (coachViewHistoryVO.getVideo_updatedCount() > 0) {
                viewHolder.tvUpdateMealCount.setVisibility(View.VISIBLE);
                viewHolder.tvUpdateVideoCount.setText("+" + coachViewHistoryVO.getVideo_updatedCount() + "개의 영상");
            } else {
                viewHolder.tvUpdateVideoCount.setVisibility(View.INVISIBLE);
            }
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(coachViewHistoryVO.getCoachId());
            }
        });
    }

    private void callActivity(int coachId) {
        Log.i(TAG, "coachId: " + coachId);
        Intent intent = new Intent(mContext, CoachInfomationActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("coachId", coachId);
        intent.putExtras(extras);
        mContext.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return mBookmarkCoachList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView coachName, tvUpdateMealCount, tvUpdateVideoCount;
        ImageView imgProfile;


        public ViewHolder(View itemView) {
            super(itemView);
            coachName = itemView.findViewById(R.id.tv_name);
            tvUpdateMealCount = itemView.findViewById(R.id.tv_update_meal_count);
            tvUpdateVideoCount = itemView.findViewById(R.id.tv_update_video_count);
            imgProfile = itemView.findViewById(R.id.img_profile);


        }

    }
}
