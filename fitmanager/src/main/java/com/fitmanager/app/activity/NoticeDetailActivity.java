package com.fitmanager.app.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.fitmanager.app.R;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.model.NoticeVo;
import com.fitmanager.app.util.Constant;

/**
 * 공지사항 상세보기
 * @author 정문기
 * @since 2018.07.05
 */
public class NoticeDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "NoticeDetailActivity";

    /**
     * Application User Local DataBase
     ************************************************************************************************************************************************/
    private MemberVO mMemberVO;                                                                                      // 유저 정보

    /**
     * UI
     ************************************************************************************************************************************************/
    private AppCompatTextView txt_notice_title, txt_notice_time, txt_notice_message;                                // 공지사항 제목, 공지사항 시간, 공지사항 메세지

    /**
     * 공지사항 ItemVo
     ************************************************************************************************************************************************/
    private NoticeVo.List noticeVo;                                                                                 // 공지사항 Information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        // Application User Local DataBase
        mMemberVO = memberManager.getMemberData();

        // BackButton Condition
        isBackButtonNotice = false;

        // Top Area
        txt_centerTitle.setText(getString(R.string.str_notice));
        iv_back.setVisibility(View.VISIBLE);

        // UI resource
        txt_notice_title = getAppCompatTextView(R.id.txt_notice_title);
        txt_notice_time = getAppCompatTextView(R.id.txt_notice_time);
        txt_notice_message = getAppCompatTextView(R.id.txt_notice_message);

        // setOnClickListener
        layout_back.setOnClickListener(this);

        // Notice Info Get Intent
        if(this.getIntent() != null)
            noticeVo = (NoticeVo.List) this.getIntent().getSerializableExtra(Constant.INTENT_NOTICE);
//        requestNoticeDetail();
    }



    /**
     * View ClickListener
     ************************************************************************************************************************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
        }
    }
}
