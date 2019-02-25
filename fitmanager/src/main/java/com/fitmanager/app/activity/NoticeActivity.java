//package com.fitmanager.app.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import com.fitmanager.app.R;
//import com.fitmanager.app.adapter.NoticeListAdapter;
//import com.fitmanager.app.model.MemberVO;
//import com.fitmanager.app.model.NoticeVo;
//import com.fitmanager.app.network.RestService;
//import com.fitmanager.app.util.AlertDialogUtil;
//import com.fitmanager.app.util.Constant;
//import com.fitmanager.app.util.OnSingleClickListener;
//import com.fitmanager.app.util.RetroUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
//
///**
// * Setting Fragment -> 공지사항 리스트 화면
// *
// * @author 정문기
// * @since 2018.07.05
// */
//public class NoticeActivity extends BaseActivity implements View.OnClickListener {
//
//    private MemberVO mMemberVO;
//    private static final String TAG = "NoticeActivity";
//    /**
//     * Application User Local DataBase
//     ************************************************************************************************************************************************/
//
//    /**
//     * Balance Adapter
//     ************************************************************************************************************************************************/
////    private NoticeListAdapter adapter;                                                                              // Notoice Adapter
//    private List<NoticeVo.List> dataList;                                                                           // Notice Data List
//    private int nextPageNo = 1;                                                                                        // 다음에 요청할 10개 리스트에 대한 페이지 번호
//    private boolean isLastPage = false;                                                                                // 리스트 목록을 모두 불러왔는지 여부
//    private boolean isLoading = false;                                                                                // 리스트 데이터 요청 중인지 여부 (중복 요청 방지)
//
//    /**
//     * UI
//     ************************************************************************************************************************************************/
//    private RelativeLayout layout_null_data;                                                                        // 빈 데이터 화면
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice);
//
//// Application User Local DataBase
//        mMemberVO = memberManager.getMemberData();
//
//        // BackButton Condition
//        isBackButtonNotice = false;
//
//        // Top Area
//        txt_centerTitle.setText(getString(R.string.str_notice));
//        iv_back.setVisibility(View.VISIBLE);
//
//        // UI resource
//        RecyclerView rv_notice_list = getRecyclerView(R.id.rv_notice_list);
//        layout_null_data = getRelativeLayout(R.id.layout_null_data);
//
////        // Notice Adatper 초기화
////        adapter = new NoticeListAdapter(mContext, R.layout.item_notice, new ArrayList<>(), noticeItemVo -> {
////            Intent intent = new Intent(mContext, NoticeDetailActivity.class);
////            intent.putExtra(Constant.INTENT_NOTICE, noticeItemVo);
////            startActivity(intent);
////        });
////        rv_notice_list.setAdapter(adapter);
//
//        // RecycleView 리스트 호출 관련된 스크롤 리스너
//        rv_notice_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            private int firstVisibleItem;
//            private int visibleItemCount;
//            private int totalItemCount;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == SCROLL_STATE_IDLE) {
//                    int count = totalItemCount - visibleItemCount;
//                    /*
//                     *  처음 보여지는 item(firstVisibleItem)이
//                     *  총 item 갯수(totalItemCount) - 현재 화면에 존재하는 item 갯수(visibleItemCount)
//                     *  와 같아지면 리스트 제일 하단에 도달한 것임.
//                     */
//                    if (firstVisibleItem >= count && totalItemCount != 0) {
//                        requestNoticeList();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                visibleItemCount = recyclerView.getChildCount();
//                totalItemCount = recyclerView.getLayoutManager().getItemCount();
//            }
//        });
//
//        // setOnClickListener
//        layout_back.setOnClickListener(this);
//
//
//        requestNoticeList();
//    }
//
//
//    /**
//     * 공지사항 리스트 요청
//     * <p>
//     * appkind / 아이디 / 국가코드 / 언어코드 / pageNumber / session
//     ************************************************************************************************************************************************/
//    private void requestNoticeList() {
//        if (isLastPage) {
//            return;
//        }
//
//        if (isLoading) {
//            return;
//        }
//
//        isLoading = true;
//
//
//        // Retrofit2 Call 모델 클래스 CreateService
//        Call<List<NoticeVo>> noticeListCall = RetroUtil.createService(Constant.SERVER_ADDR, RestService.class).requestNoticeList(Constant.APP_KIND, mMemberVO.getEmail(), nextPageNo);
//
//        // 위에 선언한 Call 객체로 비동기 방식으로 통신 시작
//        noticeListCall.enqueue(new Callback<List<NoticeVo>>() {
//            @Override
//            public void onResponse(Call<List<NoticeVo>> call, Response<List<NoticeVo>> response) {
//                // MainLooper
//                new Handler(Looper.getMainLooper()).post(() -> {
//                    // 통신 성공 여부
//                    if (response.isSuccessful()) {
//                        // 공지사항 리스트 초기화
//                        dataList = new ArrayList<>();
//
//                        // Call 객체에서 선언한 모델 클래스로 response를 body()로 결과 데이터를 받아옵니다.
//                        List<NoticeVo> itemVo = response.body();
//
//                        // Call 객체 데이터 null 여부를 확인합니다.
//                        if (itemVo != null && itemVo.size() != 0) {
//                            switch (itemVo.get(0).getReturnCode()) {
//                                case Constant.RS_SUCCESS:
//                                    if (itemVo.get(1).getList().size() != 0) {
//                                        dataList.addAll(itemVo.get(1).getList());
//
//                                        // 기존 리스트에 새로 받아온 리스트 추가
//                                        if (dataList.size() > 0) {
//                                            nextPageNo++;
//                                            List<NoticeVo.List> tokenDataList = adapter.getDataList();
//                                            tokenDataList.addAll(dataList);
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    } else {
//                                        if (adapter.getDataList().size() > 0) {
//                                            isLastPage = true;
//                                        } else {
//                                            layout_null_data.setVisibility(View.VISIBLE);
//                                            isLastPage = true;
//                                        }
//                                    }
//                                    break;
//
//                                case Constant.RS_ERRORS_FAIL:
//                                    AlertDialogUtil.showDoubleDialog(mContext, getString(R.string.error_else_retry), new OnSingleClickListener() {
//                                        @Override
//                                        public void onSingleClick(View v) {
//                                            Alegms:googlertDialogUtil.dismissDialog();
//                                            recreate();
//                                        }
//                                    }, new OnSingleClickListener() {
//                                        @Override
//                                        public void onSingleClick(View v) {
//                                            AlertDialogUtil.dismissDialog();
//                                            finish();
//                                        }
//                                    });
//                                    break;
//
//
//                                default:
//                                    AlertDialogUtil.showSingleDialog(mContext, getString(R.string.error_e00), new OnSingleClickListener() {
//                                        @Override
//                                        public void onSingleClick(View v) {
//                                            AlertDialogUtil.dismissDialog();
//                                            finish();
//                                        }
//                                    });
//                                    break;
//                            }
//                        } else {
//                            AlertDialogUtil.showSingleDialog(mContext, getString(R.string.error_e00), new OnSingleClickListener() {
//                                @Override
//                                public void onSingleClick(View v) {
//                                    AlertDialogUtil.dismissDialog();
//                                    finish();
//                                }
//                            });
//                        }
//                    } else {
//                        AlertDialogUtil.showSingleDialog(mContext, getString(R.string.error_e00), new OnSingleClickListener() {
//                            @Override
//                            public void onSingleClick(View v) {
//                                AlertDialogUtil.dismissDialog();
//                                finish();
//                            }
//                        });
//                    }
//                });
//
//                // 로딩 완료
//                isLoading = false;
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<NoticeVo>> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage());
//
//                // 로딩 완료
//                isLoading = false;
//
//                AlertDialogUtil.showDoubleDialog(mContext, getString(R.string.error_try_catch_retry), new OnSingleClickListener() {
//                    @Override
//                    public void onSingleClick(View v) {
//                        AlertDialogUtil.dismissDialog();
//                        recreate();
//                    }
//                }, new OnSingleClickListener() {
//                    @Override
//                    public void onSingleClick(View v) {
//                        AlertDialogUtil.dismissDialog();
//                        finish();
//                    }
//                });
//
//                // ProgressBar Dismiss
//            }
//        });
//    }
//
//    /**
//     * View ClickListener
//     ************************************************************************************************************************************************/
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.layout_back:
//                finish();
//                break;
//        }
//    }
//}
