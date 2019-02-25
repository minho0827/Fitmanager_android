package com.fitmanager.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fitmanager.app.R;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;

public class Utils {

    private static final String PERCENT_06X = "%06X";

    public static String getEllipsedStr(String str, int maxLen) {
        if (StringUtils.isEmpty(str) || str.length() < maxLen) {
            return "";
        } else {
            return str.substring(0, maxLen) + "...";
        }
    }

    /**
     * 터치한 지점이 해당 뷰의 유효한 지점인지 체크 후 리턴 (전체 화면 내에서의 비교 || ButtonSelectorListener)
     */
    public static boolean isAvailableTouch(View v, MotionEvent event) {
        Rect r = new Rect();
        v.getGlobalVisibleRect(r);

        return 0.0f <= event.getX() && event.getX() <= (r.right - r.left) && 0.0f <= event.getY() && event.getY() <= (r.bottom - r.top);
    }


    /**
     * int 값의 color를 16진수 hex값 형태로 반환 (ButtonSelectorListener)
     */
    public static String getTextHexColor(int colorValue) {
        return String.format(PERCENT_06X, (0xFFFFFF & colorValue));
    }

    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) return obj == null || StringUtils.isEmpty(obj.toString());
        else if (obj instanceof Collection<?>)
            return obj == null || ((Collection<?>) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static String getDisplayTime(String strDT) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.0");
        DateTime targetDT = formatter.parseDateTime(strDT);
        int days = Days.daysBetween(DateTime.now().toLocalDate(), targetDT.toLocalDate()).getDays();

        Period p = new Period(DateTime.now(), targetDT);
        int hours = p.getHours();
        int minutes = p.getMinutes();

        if (days == 0 && hours == 0) {
            return String.format("%d분 전", Math.abs(minutes));
        } else if (days == 0) {
            return String.format("%d시간 전", Math.abs(hours));
        } else {
            return String.format("%d일 전", Math.abs(days));
        }
    }

    public static String getDisplayHourMinuteTime(String strDT) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.0");
        DateTime targetDT = formatter.parseDateTime(strDT);
        int days = Days.daysBetween(DateTime.now().toLocalDate(), targetDT.toLocalDate()).getDays();

        Period p = new Period(DateTime.now(), targetDT);
        int hours = p.getHours();
        int minutes = p.getMinutes();

        if (days == 0 && hours == 0) {
            return String.format("%d분 전", Math.abs(minutes));
        } else if (days == 0) {
            return String.format("%d시간 전", Math.abs(hours));
        } else {
            return String.format("%y%m%d", Math.abs(days));
        }
    }

    public static int getDisplayDrawbleImage(String strDT) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.0");
        DateTime targetDT = formatter.parseDateTime(strDT);
        int days = Days.daysBetween(DateTime.now().toLocalDate(), targetDT.toLocalDate()).getDays();

        Period p = new Period(DateTime.now(), targetDT);
        int hours = p.getHours();
        int minutes = p.getMinutes();

        if (days == 0 && hours == 0) {
            return R.drawable.n_icon;
        } else if (days == 0) {
            return R.drawable.n_icon;
        } else {
            return R.drawable.feedicon;
        }
    }

    public static String getReplyDisplayTime(String strDT) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.0");
        DateTime targetDT = formatter.parseDateTime(strDT);
        int days = Days.daysBetween(DateTime.now().toLocalDate(), targetDT.toLocalDate()).getDays();

        Period p = new Period(DateTime.now(), targetDT);
        int hours = p.getHours();
        int minutes = p.getMinutes();
        if (minutes == 0) {
            return String.format("방금 전", Math.abs(minutes));
        } else if (days == 0 && hours == 0) {
            return String.format("%d분 전", Math.abs(minutes));
        } else if (days == 0) {
            return String.format("%d시간 전", Math.abs(hours));
        } else if (days == 1) {
            return targetDT.toString("어제 a hh시 mm분");
        } else {
            return targetDT.toString("yyyy년 MM월 dd일");
        }
    }

    public static String getExerciseType(int exerciseType) {

        String exerciseStr = "";
        switch (exerciseType) {
            case 1:
                exerciseStr = "  필라테스  ";
                break;
            case 2:
                exerciseStr = "  요가  ";
                break;
            case 3:
                exerciseStr = "  웨이트  ";
                break;
            case 4:
                exerciseStr = "  발레핏  ";
                break;
            case 5:
                exerciseStr = "  홈트레이닝  ";
                break;
            case 6:
                exerciseStr = "  크로스핏  ";
                break;
            case 7:
                exerciseStr = "  체형교정  ";
                break;
            case 8:
                exerciseStr = "  폴댄스  ";
                break;
            default:
                break;
        }
        return exerciseStr;
    }


    public static int getExerciseByColorType(int mealType) {
        int color = 0;

        switch (mealType) {
            case 1:
                color = R.drawable.xml_colorbg_main;
                break;
            case 2:
                color = R.drawable.xml_colorbg_main;
                break;
            case 3:
                color = R.drawable.xml_colorbg_main;
                break;
            case 4:
                color = R.drawable.xml_colorbg_main;
                break;
            case 5:
                color = R.drawable.xml_colorbg_main;
                break;
            case 6:
                color = R.drawable.xml_colorbg_main;
                break;
            case 7:
                color = R.drawable.xml_colorbg_main;
                break;
            case 8:
                color = R.drawable.xml_colorbg_main;
                break;

        }

        return color;
    }


    public static String getBodyType(int exerciseType) {

        String bodyTypeStr = "";
        switch (exerciseType) {
            case 1:
                bodyTypeStr = "  하체  ";
                break;
            case 2:
                bodyTypeStr = "  어깨  ";
                break;
            case 3:
                bodyTypeStr = "  엉덩이  ";
                break;
            case 4:
                bodyTypeStr = "  등  ";
                break;
            case 5:
                bodyTypeStr = "  복근  ";
                break;
            case 6:
                bodyTypeStr = "  팔  ";
                break;
            case 7:
                bodyTypeStr = "  전신  ";
                break;
            case 8:
                bodyTypeStr = "  허리  ";
                break;
            case 9:
                bodyTypeStr = "  뒷태  ";
                break;
            case 10:
                bodyTypeStr = "  가슴  ";
                break;

            default:
                break;
        }
        return bodyTypeStr;
    }


    public static String getLevelType(int level) {

        String levelStr = "";
        switch (level) {
            case 1:
                levelStr = "쉬움";
                break;
            case 2:
                levelStr = "중간";
                break;
            case 3:
                levelStr = "어려움";
                break;

            default:
                break;
        }
        return levelStr;
    }

    public static int getBodyByColorType(int mealType) {
        int color = 0;

        if (mealType > 0) {
            color = R.drawable.xml_colorbg_main;
        }
        return color;
    }


    public static int getMealTypeBgSquare(int mealType) {
        int mealColor = 0;

        switch (mealType) {
            case 1:
                mealColor = R.drawable.xml_colorbg_purple_square;
                break;
            case 2:
                mealColor = R.drawable.xml_colorbg_pink_square;
                break;
            case 3:
                mealColor = R.drawable.xml_colorbg_cyan_square;
                break;
            case 4:
                mealColor = R.drawable.xml_colorbg_orange_square;
                break;


        }
        return mealColor;
    }


    public static int getMealType(int mealType) {
        int mealColor = 0;

        switch (mealType) {
            case 1:
                mealColor = R.drawable.xml_colorbg_purple;
                break;
            case 2:
                mealColor = R.drawable.xml_colorbg_pink;
                break;
            case 3:
                mealColor = R.drawable.xml_colorbg_cyan;
                break;
            case 4:
                mealColor = R.drawable.xml_colorbg_orange;
                break;


        }
        return mealColor;

    }

    public static int getTitleBackgroundColorType(int mealType) {
        int mealColor = 0;

        switch (mealType) {
            case 1:
                mealColor = R.drawable.xml_colorbg_purple_square;
                break;
            case 2:
                mealColor = R.drawable.xml_colorbg_pink_square;
                break;
            case 3:
                mealColor = R.drawable.xml_colorbg_cyan_square;
                break;
            case 4:
                mealColor = R.drawable.xml_colorbg_orange_square;
                break;


        }
        return mealColor;
    }


    public static String getMealTypeText(int mealType) {

        String mealStr = "";
        switch (mealType) {
            case 1:
                mealStr = "  아침  ";
                break;
            case 2:
                mealStr = "  점심  ";
                break;
            case 3:
                mealStr = "  저녁  ";
                break;
            case 4:
                mealStr = "  간식  ";
                break;

            default:
                break;
        }
        return mealStr;
    }

    public static void hideKeyboard(final View view, final Context context) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }, 50);

    }

    public static String encryptPassword(String pwd) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pwd.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void showProgress(FitProgressBar progressBar, Context ctx) {
        if (progressBar != null) {
            progressBar.show(ctx);
        }
    }


    public static void hideProgress(FitProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.hide();
        }
    }

    /**
     * 7. 현재 네트워크가 연결되어 있는지 확인
     */
    public static boolean isNetworkConnected(Context context) {
        boolean isConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnectedOrConnecting()) {
                isConnected = true;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnectedOrConnecting()) {
                isConnected = true;
            }
        }

        return isConnected;
    }

    public static boolean isWifiConnection(Context context) {
        //Create object for ConnectivityManager class which returns network related info
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //If connectivity object is not null
        if (connectivity != null) {
            //Get network info - WIFI internet access
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (info != null) {
                //Look for whether device is currently connected to WIFI network
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 4. 현재 디바이스의 App 버전을 확인
     */
    public static String getCurrentVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //키보드 show
    public static void showKeyboard(View v, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

    }
}
