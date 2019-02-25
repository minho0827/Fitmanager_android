package com.fitmanager.app.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fitmanager.app.R;
import com.fitmanager.app.model.MealVO.MealImagesVO;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MealViewPagerAdpaer extends PagerAdapter {

    private List<MealImagesVO> mMealImagesList;

    public MealViewPagerAdpaer(List<MealImagesVO> mealImagesList) {
        this.mMealImagesList = mealImagesList;
    }

    @Override
    public int getCount() {
        return mMealImagesList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager_meal_row, container, false);
        ImageView imgMeal = (ImageView) itemView.findViewById(R.id.img_meal);
        MealImagesVO mealImagesVO = mMealImagesList.get(position);
        String imageUrl = mealImagesVO.getImageUrl();
        if (StringUtils.isNotEmpty(imageUrl)) {
            Glide.with(container.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    .crossFade()
                    .into(imgMeal);

        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
