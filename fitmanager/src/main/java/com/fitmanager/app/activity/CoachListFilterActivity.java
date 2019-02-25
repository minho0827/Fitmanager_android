package com.fitmanager.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fitmanager.app.R;
import com.fitmanager.app.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class CoachListFilterActivity extends AppCompatActivity implements View.OnClickListener {

    final static String TAG = "CoachListFilterActivity";
    TextView tvReset;
    Button btnFinish;
    ToggleButton btnMan;
    ToggleButton btnGirl;
    ToggleButton btnPilates;
    ToggleButton btnYoga;
    ToggleButton btnWeightTR;
    ToggleButton btnHomeTR;
    ToggleButton btnCrossfit;
    ToggleButton btnBodyCorrection;
    ToggleButton btnPostpartum;
    ToggleButton btnBalletfits;
    ToggleButton btnPaulDance;

    TextView tvApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisetype_filter);
        setLayout();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setLayout() {
        btnBalletfits =  findViewById(R.id.btn_balletfits);
        btnFinish =  findViewById(R.id.btn_finish);
        btnPilates = findViewById(R.id.btn_pilates);
        btnYoga = findViewById(R.id.btn_yoga);
        btnWeightTR = findViewById(R.id.btn_weightTR);
        btnHomeTR = findViewById(R.id.btn_home_tr);
        btnCrossfit = findViewById(R.id.btn_crossfit);
        btnBodyCorrection = findViewById(R.id.btn_body_correction);
        btnPostpartum = findViewById(R.id.btn_postpartum);
        btnPaulDance = findViewById(R.id.btn_paul_dance);
        tvApply =  findViewById(R.id.tv_apply);
        btnMan = findViewById(R.id.btn_man);
        btnGirl = findViewById(R.id.btn_girl);
        tvReset = findViewById(R.id.tv_reset);

        btnPaulDance.setOnClickListener(this);
        btnPilates.setOnClickListener(this);
        btnBalletfits.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        btnYoga.setOnClickListener(this);
        btnWeightTR.setOnClickListener(this);
        btnHomeTR.setOnClickListener(this);
        btnCrossfit.setOnClickListener(this);
        btnBodyCorrection.setOnClickListener(this);
        btnPostpartum.setOnClickListener(this);
        btnGirl.setOnClickListener(this);
        btnMan.setOnClickListener(this);
        tvReset.setOnClickListener(this);

        Intent intent = getIntent();
        String gender = StringUtils.trimToEmpty(intent.getStringExtra("gender"));
        ArrayList<String> filterList = intent.getStringArrayListExtra("filter_list");

        if ("12".equals(gender)) {
            setButtonState(btnMan, true);
            setButtonState(btnGirl, true);
        } else if (gender.contains("1")) {
            setButtonState(btnMan, true);
        } else if (gender.contains("2")) {
            setButtonState(btnGirl, true);
        }

        if (!Utils.isEmpty(filterList)) {
            for (String filter : filterList) {
                if ("1".equals(filter)) {
                    setButtonState(btnPilates, true);

                } else if ("2".equals(filter)) {
                    setButtonState(btnYoga, true);

                } else if ("3".equals(filter)) {
                    setButtonState(btnWeightTR, true);

                } else if ("5".equals(filter)) {
                    setButtonState(btnHomeTR, true);

                } else if ("4".equals(filter)) {
                    setButtonState(btnBalletfits, true);

                } else if ("6".equals(filter)) {
                    setButtonState(btnCrossfit, true);

                } else if ("7".equals(filter)) {
                    setButtonState(btnBodyCorrection, true);

                } else if ("8".equals(filter)) {
                    setButtonState(btnPaulDance, true);

                } else if ("9".equals(filter)) {
                    setButtonState(btnPostpartum, true);

                }

            }
        }
    }

    private void setButtonState(ToggleButton btn, boolean isChecked) {
        btn.setChecked(isChecked);
        if (isChecked) {
            btn.setBackgroundResource(R.color.maincolor);
        } else {
            btn.setBackgroundResource(R.color.gray);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // X버튼
            case R.id.btnFinish:

                finish();
                break;

            //초기화
            case R.id.tv_reset:
                setButtonState(btnMan, false);
                btnPilates.setBackgroundResource(R.color.gray);

                setButtonState(btnGirl, false);
                btnPilates.setBackgroundResource(R.color.gray);

                setButtonState(btnPilates, false);
                btnPilates.setBackgroundResource(R.color.gray);

                setButtonState(btnYoga, false);
                btnYoga.setBackgroundResource(R.color.gray);

                setButtonState(btnWeightTR, false);
                btnWeightTR.setBackgroundResource(R.color.gray);

                setButtonState(btnHomeTR, false);
                btnHomeTR.setBackgroundResource(R.color.gray);

                setButtonState(btnBalletfits, false);
                btnBalletfits.setBackgroundResource(R.color.gray);

                setButtonState(btnCrossfit, false);
                btnCrossfit.setBackgroundResource(R.color.gray);

                setButtonState(btnBodyCorrection, false);
                btnBodyCorrection.setBackgroundResource(R.color.gray);

                setButtonState(btnPostpartum, false);
                btnPostpartum.setBackgroundResource(R.color.gray);

                setButtonState(btnPaulDance, false);
                btnPaulDance.setBackgroundResource(R.color.gray);


                break;


            //적용하기
            case R.id.tv_apply:
                String gender = "";
                ArrayList<String> filterList = new ArrayList<>();
                if (btnMan.isChecked()) {
                    gender += btnMan.getTag().toString();
                }
                if (btnGirl.isChecked()) {
                    gender += btnGirl.getTag().toString();
                }
                if (btnPilates.isChecked()) {
                    filterList.add(btnPilates.getTag().toString());
                }
                if (btnYoga.isChecked()) {
                    filterList.add(btnYoga.getTag().toString());
                }
                if (btnWeightTR.isChecked()) {
                    filterList.add(btnWeightTR.getTag().toString());
                }
                if (btnHomeTR.isChecked()) {
                    filterList.add(btnHomeTR.getTag().toString());
                }
                if (btnBalletfits.isChecked()) {
                    filterList.add(btnBalletfits.getTag().toString());
                }
                if (btnCrossfit.isChecked()) {
                    filterList.add(btnCrossfit.getTag().toString());
                }
                if (btnBodyCorrection.isChecked()) {
                    filterList.add(btnBodyCorrection.getTag().toString());
                }
                if (btnPostpartum.isChecked()) {
                    filterList.add(btnPostpartum.getTag().toString());
                }
                if (btnPaulDance.isChecked()) {
                    filterList.add(btnPaulDance.getTag().toString());
                }

                Intent intent = new Intent();
                intent.putStringArrayListExtra("WORK_KIND_FILTER", filterList);
                intent.putExtra("GENDER", gender);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btn_man:
                setButtonState(btnMan, btnMan.isChecked());
                break;

            case R.id.btn_girl:
                setButtonState(btnGirl, btnGirl.isChecked());
                break;

            // 필라테스 1-1
            case R.id.btn_pilates:
                if (btnPilates.isChecked()) {
                    btnPilates.setBackgroundResource(R.color.maincolor);
                } else {
                    btnPilates.setBackgroundResource(R.color.gray);
                }
                break;

            // 요가 1-2
            case R.id.btn_yoga:
                if (btnYoga.isChecked()) {
                    btnYoga.setBackgroundResource(R.color.maincolor);
                } else {
                    btnYoga.setBackgroundResource(R.color.gray);
                }
                break;
            // 웨이트트레이닝 1-3
            case R.id.btn_weightTR:
                if (btnWeightTR.isChecked()) {
                    btnWeightTR.setBackgroundResource(R.color.maincolor);
                } else {
                    btnWeightTR.setBackgroundResource(R.color.gray);
                }
                break;

            // 홈트레이닝 1-5
            case R.id.btn_home_tr:
                if (btnHomeTR.isChecked()) {
                    btnHomeTR.setBackgroundResource(R.color.maincolor);
                } else {
                    btnHomeTR.setBackgroundResource(R.color.gray);
                }
                break;

            // 발레핏 1-4
            case R.id.btn_balletfits:
                if (btnBalletfits.isChecked()) {
                    btnBalletfits.setBackgroundResource(R.color.maincolor);
                } else {
                    btnBalletfits.setBackgroundResource(R.color.gray);
                }
                break;

            // 크로스핏 1-6
            case R.id.btn_crossfit:
                if (btnCrossfit.isChecked()) {
                    btnCrossfit.setBackgroundResource(R.color.maincolor);
                } else {
                    btnCrossfit.setBackgroundResource(R.color.gray);
                }
                break;

            // 체형교정 1-7
            case R.id.btn_body_correction:
                if (btnBodyCorrection.isChecked()) {
                    btnBodyCorrection.setBackgroundResource(R.color.maincolor);
                } else {
                    btnBodyCorrection.setBackgroundResource(R.color.gray);
                }

                break;

            // 산전산후 1-9
            case R.id.btn_postpartum:
                if (btnPostpartum.isChecked()) {
                    btnPostpartum.setBackgroundResource(R.color.maincolor);
                } else {
                    btnPostpartum.setBackgroundResource(R.color.gray);
                }
                break;

            case R.id.btn_paul_dance:
                if (btnPaulDance.isChecked()) {
                    btnPaulDance.setBackgroundResource(R.color.maincolor);
                } else {
                    btnPaulDance.setBackgroundResource(R.color.gray);
                }
                break;

        }
    }
}
