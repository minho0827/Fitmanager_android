package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fitmanager.app.R;
import com.fitmanager.app.activity.MealDetailActivity;
import com.fitmanager.app.model.MealVO;
import com.fitmanager.app.util.Utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BookmarkMealListAdapter extends RecyclerView.Adapter<BookmarkMealListAdapter.ViewHolder> {
    Context mContext;
    private List<MealVO> mBookmarkMealList;
    private final static String TAG = "BookmarkMealAdapter";
    private boolean editMode = false;
    OnCheckedListener onCheckedListener;
    boolean mIsAllSelectChecked = false;

    public BookmarkMealListAdapter(Context context, List<MealVO> bookmarkMealItems) {
        mContext = context;
        mBookmarkMealList = bookmarkMealItems;
        this.onCheckedListener = (OnCheckedListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_meal_bookmark_checked, parent, false);
        return new ViewHolder(view);
    }

    public void setData(List<MealVO> mealBookmarkList) {
        this.mBookmarkMealList = mealBookmarkList;
        Log.i(TAG, "mBookmarkMealList :" + mealBookmarkList.size());
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final MealVO mealVO = mBookmarkMealList.get(position);
        if (mealVO != null) {
            Glide.with(mContext)
                    .load(mealVO.getImageUrl())
                    .centerCrop()
                    .crossFade()
                    .into(viewHolder.img_meal);

            viewHolder.title.setText(mealVO.getTitle());
            viewHolder.title.setBackgroundResource(Utils.getTitleBackgroundColorType(mealVO.getType()));
            viewHolder.tvMealType.setBackgroundResource(Utils.getMealTypeBgSquare(mealVO.getType()));
//            viewHolder.tvMealType.setText(Utils.getMealTypeText(mealVO.getType()));
            viewHolder.tvMealType.setText(Utils.getMealTypeText(mealVO.getType()));

            viewHolder.layoutMealCheck.setOnClickListener(null);

            if (isEditMode()) {
                viewHolder.btnCheck.setVisibility(View.VISIBLE);
                viewHolder.btnCheck.setChecked(mealVO.isSelected());
            } else {
                viewHolder.btnCheck.setVisibility(View.GONE);
            }

            viewHolder.layoutMealCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditMode()) {
                        boolean checked = !viewHolder.btnCheck.isChecked();
                        // set checked
                        mealVO.setSelected(checked);
                        viewHolder.btnCheck.setChecked(checked);
                        Log.d(TAG, "mealVO[" + mealVO.getMealId() + "].isSelected(): " + checked);
                        onCheckedListener.onItemChecked();
                    } else {
                        Intent intent = new Intent(mContext, MealDetailActivity.class);
                        Bundle extras = new Bundle();
                        Log.i(TAG, "mealId :" + mBookmarkMealList.get(position).getMealId());
                        extras.putInt("targetId", mBookmarkMealList.get(position).getMealId());
                        intent.putExtras(extras);
                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mBookmarkMealList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, tvMealType;
        ImageView img_meal;
        CheckBox btnCheck;
        View layoutMealCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutMealCheck = itemView.findViewById(R.id.layout_meal_check);
            tvMealType = (TextView) itemView.findViewById(R.id.tv_mealtype);
            title = (TextView) itemView.findViewById(R.id.title);
            btnCheck = (CheckBox) itemView.findViewById(R.id.btn_check);
            img_meal = (ImageView) itemView.findViewById(R.id.img_meal);


        }

    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        if (editMode == false) {
            clearSelection();
        }
        notifyDataSetChanged();
    }

    private void clearSelection() {
        if (CollectionUtils.isNotEmpty(mBookmarkMealList)) {
            for (MealVO mealVO : mBookmarkMealList) {
                mealVO.setSelected(false);
            }
        }
    }

    public void selectAll(boolean select) {
        if (CollectionUtils.isNotEmpty(mBookmarkMealList)) {
            for (MealVO mealVO : mBookmarkMealList) {
                mealVO.setSelected(select);
            }
        }
        notifyDataSetChanged();
    }



    public List<MealVO> getSelectedItems() {
        List<MealVO> mealList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mBookmarkMealList)) {
            for (MealVO mealVO : mBookmarkMealList) {
                if (mealVO.isSelected()) {
                    mealList.add(mealVO);
                }
            }
        }
        return mealList;
    }

    public interface OnCheckedListener {
        void onItemChecked();
    }

}
