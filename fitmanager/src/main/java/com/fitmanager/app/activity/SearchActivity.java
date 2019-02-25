//package com.fitmanager.app.activity;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.fitmanager.app.R;
//import com.fitmanager.app.adapter.CoachListAdapter;
//import com.fitmanager.app.model.CoachVO;
//import com.fitmanager.app.network.RestService;
//import com.fitmanager.app.util.Constant;
//import com.fitmanager.app.util.FitProgressBar;
//import com.fitmanager.app.util.Utils;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class SearchActivity extends AppCompatActivity implements CoachListAdapter.CoachListListener, CoachListAdapter.EndlessScrollListener {
//    private static final String TAG = "SearchActivity";
//    private Toolbar mToolbar;
//    private MenuItem searchItem;
//    public static final int FILTER = 0;
//    private CoachListAdapter mCoachProfileAdapter;
//    private List<CoachVO> mCoachProfileItems = new ArrayList<>();
//    RecyclerView mRecyclerView;
//    LinearLayoutManager mLayoutManager;
//    static final boolean GRID_LAYOUT = false;
//    private SearchView mSearchView;
//    private static FitProgressBar mProgressBar = new FitProgressBar();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_coach_search);
//        setLayout();
//        mCoachProfileAdapter = new CoachListAdapter(this, mCoachProfileItems, CoachListAdapter.ADAPTER_TYPE_SEARCH);
//
//        if (GRID_LAYOUT) {
//            mLayoutManager = new GridLayoutManager(this, 2);
//        } else {
//            mLayoutManager = new LinearLayoutManager(this);
//        }
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mCoachProfileAdapter);
//    }
//
//
//    private void setLayout() {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("코치 검색");
//        mToolbar.setTitleTextColor(Color.WHITE);
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu( Menu menu) {
//        getMenuInflater().inflate( R.menu.coach_search_menu, menu);
//
//        MenuItem myActionMenuItem = menu.findItem( R.id.menu_search);
//        mSearchView = (SearchView) myActionMenuItem.getActionView();
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.d(TAG, "onQueryTextSubmit: " + query);
//
//                if(!mSearchView.isIconified()) {
//                    mSearchView.setIconified(true);
//                }
//                getRestCoachSearchList(query);
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Log.d(TAG, "onQueryTextSubmit: " + s);
//                return false;
//            }
//        });
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return true;
//    }
//
//
//    private void getRestCoachSearchList(String name) {
//        showProgress();
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constant.SERVER_ADDR)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        RestService service = retrofit.create(RestService.class);
//
//        final Call<List<CoachVO>> coachList = service.getCoachSearchList(name);
//
//        coachList.enqueue(new Callback<List<CoachVO>>() {
//            @Override
//            public void onResponse(Call<List<CoachVO>> call, Response<List<CoachVO>> response) {
//                Log.d(TAG, "onResponse: " + response);
//
//                List<CoachVO> resultList = response.body();
//                mCoachProfileItems.clear();
//
//                if (!Utils.isEmpty(resultList)) {
//                    mCoachProfileItems.addAll(resultList);
//                }
//                mCoachProfileAdapter.setData(mCoachProfileItems);
//                mRecyclerView.setAdapter(mCoachProfileAdapter);
//                hideProgress();
//            }
//
//            @Override
//            public void onFailure(Call<List<CoachVO>> call, Throwable t) {
//                Log.e(TAG, "## onFailure: ");
//            }
//        });
//    }
//
//
//    private void showProgress() {
//        if (mProgressBar != null) {
//            mProgressBar.show(getApplicationContext());
//        }
//    }
//
//
//    private void hideProgress() {
//        if (mProgressBar != null) {
//            mProgressBar.getDialog().dismiss();
//        }
//    }
//    @Override
//    public void callCoachFilterActivity() {
//
//    }
//
//    @Override
//    public void callSearchActivity() {
//
//    }
//
//    @Override
//    public void callCoachActivity(int coachId) {
//
//    }
//
//    @Override
//    public Context getCoachContext() {
//        return this;
//    }
//
//    @Override
//    public boolean onLoadMore(int position) {
//        return false;
//    }
//
//}