package com.fitmanager.app.model;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MealVO implements Serializable {


    private int mealId;
    private int coachId;
    private int type;
    private String title;
    private List<MealImagesVO> mealImagesVOList = new ArrayList<>();
    private String content;
    private String imageUrl;
    private int hit;
    private String coachName;
    private String profileImg;
    private int bookmarkCount;
    private int commentCount;
    private boolean selected;
    String created;
    String updated;

    public List<MealImagesVO> getMealImagesVOList() {
        if (CollectionUtils.isEmpty(mealImagesVOList)) {
            MealImagesVO aaa = new MealImagesVO();
            aaa.setImageUrl("https://www.google.co.kr/imgres?imgurl=http%3A%2F%2Fimage.ajunews.com%2Fcontent%2Fimage%2F2018%2F10%2F01%2F20181001142554711768.jpg&imgrefurl=https%3A%2F%2Fwww.ajunews.com%2Fview%2F20181001142451817&docid=0SS-NWp5Hx8bdM&tbnid=uM-1JjaNm2ihDM%3A&vet=10ahUKEwic-cuGjYbfAhUMFYgKHRHFACQQMwj8ASgPMA8..i&w=556&h=366&bih=684&biw=1425&q=%EC%8B%9D%EB%8B%A8&ved=0ahUKEwic-cuGjYbfAhUMFYgKHRHFACQQMwj8ASgPMA8&iact=mrc&uact=8");
            mealImagesVOList.add(aaa);

            MealImagesVO bbb = new MealImagesVO();
            bbb.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI8CdqNmbFF4FwGKd_3OE2lRCgjeGs0XsbSJiRehuWx2PQmlD6");
            mealImagesVOList.add(bbb);

            MealImagesVO ccc = new MealImagesVO();
            ccc.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYHvsMorg45w05KEp0ZYWn_BEWHCeG8yntcgtTvrEw12uFRvn5mw");
            mealImagesVOList.add(ccc);
        }
        return mealImagesVOList;
    }

    @Data
    public static class MealImagesVO implements Serializable {
        private String imageUrl;

    }
}

