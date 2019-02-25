//package com.fitmanager.app.activity;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//
//import com.arlib.floatingsearchview.FloatingSearchView;
//import com.fitmanager.app.R;
//
//
//public class VideoSearchActivity extends AppCompatActivity {
//    private boolean mSearchViewAdded = false;
//    private FloatingSearchView mSearchView;
//    private WindowManager mWindowManager;
//    private Toolbar mToolbar;
//    private MenuItem searchItem;
//    private boolean searchActive = false;
//    private FloatingActionButton fab;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_coach_search);
//        setLayout();
//        setSearchView();
//
//        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
//                toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                        InputMethodManager.HIDE_IMPLICIT_ONLY);
//        if (mToolbar != null) {
//            mToolbar.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (!mSearchViewAdded && mWindowManager != null) {
////                        mWindowManager.addView(mSearchView,mSearchView.getSearchViewLayoutParams(VideoSearchActivity.this);
//                        mSearchViewAdded = true;
//                    }
//                }
//            });
//        }
//
//    }
//
//    private void setSearchView() {
//        mSearchView = new FloatingSearchView(this);
////        mSearchView.setOnSearchListener(this);
////        mSearchView.setSearchResultsListener(this);
//        mSearchView.setSearchHint("제목을 검색해주세요");
//        mSearchView.getDisplay();
//    }
//
//    private void setLayout() {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("비디오 검색");
//        mToolbar.setTitleTextColor(Color.WHITE);
//
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        searchItem = menu.findItem(R.id.search);
//        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                mSearchView.getDisplay();
//                openKeyboard();
//                return true;
//            }
//        });
//        if (searchActive)
//            mSearchView.getDisplay();
//            return true;
//
//
//    }
//
//    private void openKeyboard() {
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                mSearchView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
//                mSearchView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
//            }
//        }, 200);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }
//
////    @Override
////    public void onSearch(String query) {
////
////    }
////
////    @Override
////    public void searchViewOpened() {
////        Toast.makeText(VideoSearchActivity.this, "Search View Opened", Toast.LENGTH_SHORT).show();
////    }
////
////    @Override
////    public void searchViewClosed() {
//////        Util.showSnackBarMessage(fab, "Search View Closed");
////    }
////
////    @Override
////    public void onItemClicked(String item) {
////        Toast.makeText(VideoSearchActivity.this, item + " clicked", Toast.LENGTH_SHORT).show();
////    }
////
////    @Override
////    public void onScroll() {
////
////    }
////
////    @Override
////    public void error(String localizedMessage) {
////
////    }
////
////    @Override
////    public void onCancelSearch() {
//////        Util.showSnackBarMessage(fab, "Search View Cleared");
////        searchActive = false;
////        mSearchView.hide();
////    }
//}