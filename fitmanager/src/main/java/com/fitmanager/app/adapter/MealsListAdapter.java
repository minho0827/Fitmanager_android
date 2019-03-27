package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitmanager.app.R;
import com.fitmanager.app.activity.MealDetailActivity;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.model.MealVO;
import com.fitmanager.app.util.Utils;

import java.util.HashMap;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class MealsListAdapter extends RecyclerView.Adapter<MealsListAdapter.ViewHolder> {
    private List<MealVO> mMealItems = null;
    Context mContext;

    static int TYPE_HEADER = 0;
    static int TYPE_CELL = 1;
    static int TYPE_FOOTER = 2;

    HashMap<Integer, Integer> mViewPagerState = new HashMap<>();
    FragmentManager fragmentManager;

    private static final int MAX_CONTENT_LINE_COUNT = 5;
    private CoachVO coachVO = new CoachVO();

    private final String TAG = "MealsListAdapter";

    public MealsListAdapter(Context context, List<MealVO> mealsData) {

        if (mealsData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.mContext = context;
        this.mMealItems = mealsData;
        Log.i(TAG, "mealsData :" + mealsData);
    }

    public void setCoachVO(CoachVO coachVO) {
        this.coachVO = coachVO;
        notifyItemChanged(0);
    }


    private void callActivity(int mealId) {

        Intent intent = new Intent(mContext, MealDetailActivity.class);
        Bundle extras = new Bundle();
        Log.i(TAG, "target_id" + mealId);
        Log.i(TAG, "profile_img" + coachVO.getProfileImg());
        extras.putString("profile_img", coachVO.getProfileImg());
        extras.putInt("targetId", mealId);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

    public void setData(List<MealVO> mealList) {
        this.mMealItems = mealList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMealItems.size() + 2;


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == mMealItems.size() + 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_CELL;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meallist_header, parent, false);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meallist_footer, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meals_item_row, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    private MealVO getItem(int position) {
        if (position < 0) {
            return null;
        } else if (getItemViewType(position) == TYPE_HEADER
                || getItemViewType(position) == TYPE_FOOTER) {
            return null;
        } else {
            if (mMealItems.size() <= position - 1) {
                return null;
            }
            return mMealItems.get(position - 1);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        Log.d(TAG, "onBindViewHolder > position: " + position);

        if (viewHolder.isHeader) {
            viewHolder.mealNumCount.setText(mMealItems.size() + "");
            viewHolder.coachName.setText(coachVO.getCoachName());

        } else if (viewHolder.isFooter) {
            if (mMealItems.size() == 0) {
                viewHolder.tvFooter.setText("현재 등록된 식단이 없습니다.");
                viewHolder.horizontalLine.setVisibility(View.GONE);

            } else {
//                viewHolder.tvFooter.setText("최근 30일 이내에 소식만 확인 가능합니다.");
            }
//            viewHolder.mealNumCount.setText(mMealItems.size() + "");
//            viewHolder.coachName.setText("Footer: " + coachVO.getName());
        } else if (viewHolder.isCell) {

            final MealVO mealVO = getItem(position);
            if (mealVO != null) {

                MealViewPagerAdpaer mealViewPagerAdapter = new MealViewPagerAdpaer(mealVO.getMealImagesVOList());
                viewHolder.viewPager.setAdapter(mealViewPagerAdapter);
                viewHolder.viewPager.setId(position + 1);


                viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager, true);

                viewHolder.tvContent.setText(mealVO.getContent());
                viewHolder.tvContent.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = viewHolder.tvContent.getLineCount();
                        if (viewHolder.tvContent.getLineCount() >= MAX_CONTENT_LINE_COUNT) {
                            viewHolder.tvViewMore.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvViewMore.setVisibility(View.GONE);
                        }
                    }
                });

                viewHolder.tvMealType.setText(Utils.getMealTypeText(mealVO.getType()));
                viewHolder.tvMealType.setBackgroundResource(Utils.getMealType(mealVO.getType()));
                viewHolder.tvTitle.setText(mealVO.getTitle());
//                viewHolder.tvDate.setText(Utils.getDisplayTime(mealVO.getCreated()));
//                viewHolder.imgCheckIcon.setBackgroundResource(Utils.getDisplayDrawbleImage(mealVO.getCreated()));
                final int mealId = mealVO.getMealId();

            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewPager viewPager;
        TabLayout tabLayout;


        // header
        TextView coachName;
        TextView mealNumCount;

        // body
        ImageView imgCheckIcon;
        ImageView imgNewIcon;
        TextView tvMealType;
        TextView tvDate;
        TextView tvTitle;
        ImageView img_newIcon;
        boolean isCell;
        boolean isHeader;
        boolean isFooter;

        ExpandableTextView tvContent;
        TextView tvViewMore;

        //footer
        TextView tvFooter;
        View horizontalLine;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_CELL) {
                isCell = true;
                imgNewIcon = (ImageView) itemView.findViewById(R.id.img_new);
//                tvDate = (TextView) itemView.findViewById(R.id.tv_date);
                tvMealType = (TextView) itemView.findViewById(R.id.tv_mealtype);
//                imgCheckIcon = (ImageView) itemView.findViewById(R.id.check_icon);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                img_newIcon = (ImageView) itemView.findViewById(R.id.img_new);
                viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
                tabLayout = (TabLayout) itemView.findViewById(R.id.tab_layout);

                tvContent = (ExpandableTextView) itemView.findViewById(R.id.tv_content);
                tvViewMore = (TextView) itemView.findViewById(R.id.tv_view_more);

                tvContent.setAnimationDuration(1000L);
                tvContent.setInterpolator(new OvershootInterpolator());
                tvContent.setExpandInterpolator(new OvershootInterpolator());
                tvContent.setCollapseInterpolator(new OvershootInterpolator());

                tvViewMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        tvContent.expand();
                        tvViewMore.setVisibility(View.GONE);
                    }
                });

                tvViewMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        tvContent.expand();
                        tvViewMore.setVisibility(View.GONE);
                    }
                });

            } else if (viewType == TYPE_HEADER) {
                isHeader = true;
                coachName = (TextView) itemView.findViewById(R.id.header_coachname);
                mealNumCount = (TextView) itemView.findViewById(R.id.meal_num_count);
            } else if (viewType == TYPE_FOOTER) {
                isFooter = true;
                horizontalLine = (View) itemView.findViewById(R.id.horizontal_line);
                tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
            }
        }
    }

}

