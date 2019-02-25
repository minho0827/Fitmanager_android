package com.fitmanager.app.util;

import com.fitmanager.app.model.MealVO;

import java.util.ArrayList;
import java.util.List;

public class FitmagerStorage {

    public static List<MealVO.MealImagesVO> mealDeatailImages = new ArrayList<>();

    private static int resultIndex = 0;

    public static void clearStorage() {

        mealDeatailImages.clear();
        resultIndex = 0;

    }
}
