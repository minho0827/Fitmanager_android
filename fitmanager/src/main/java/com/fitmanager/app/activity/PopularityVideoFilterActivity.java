package com.fitmanager.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.fitmanager.app.R;
import com.fitmanager.app.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopularityVideoFilterActivity extends AppCompatActivity {

    final static String TAG = "PopularityVideoFilterActivity";
    @BindView(R.id.tv_reset)
    AppCompatTextView tvReset;
    @BindView(R.id.tv_apply)
    AppCompatTextView tvApply;
    @BindView(R.id.btn_viewed)
    ToggleButton btnViewed;
    @BindView(R.id.btn_created)
    ToggleButton btnCreated;
    @BindView(R.id.btn_man)
    ToggleButton btnMan;
    @BindView(R.id.btn_girl)
    ToggleButton btnGirl;
    @BindView(R.id.btn_pilates)
    ToggleButton btnPilates;
    @BindView(R.id.btn_yoga)
    ToggleButton btnYoga;
    @BindView(R.id.btn_weightTR)
    ToggleButton btnWeightTR;
    @BindView(R.id.btn_home_tr)
    ToggleButton btnHomeTR;
    @BindView(R.id.btn_crossfit)
    ToggleButton btnCrossfit;
    @BindView(R.id.btn_body_correction)
    ToggleButton btnBodyCorrection;
    @BindView(R.id.btn_postpartum)
    ToggleButton btnPostpartum;
    @BindView(R.id.btn_balletfits)
    ToggleButton btnBalletfits;
    @BindView(R.id.btn_paul_dance)
    ToggleButton btnPaulDance;
    @BindView(R.id.btn_lower_body)
    ToggleButton btnLowerBody;
    @BindView(R.id.btn_shoulder)
    ToggleButton btnShoulder;
    @BindView(R.id.btn_hip)
    ToggleButton btnHip;
    @BindView(R.id.btn_body_back)
    ToggleButton btnBodyBack;
    @BindView(R.id.btn_abdomen)
    ToggleButton btnAbdomen;
    @BindView(R.id.btn_arm)
    ToggleButton btnArm;
    @BindView(R.id.btn_whole_body)
    ToggleButton btnWholeBody;
    @BindView(R.id.btn_waist)
    ToggleButton btnWaist;
    @BindView(R.id.btn_backstab)
    ToggleButton btnBackstab;
    @BindView(R.id.btn_chest)
    ToggleButton btnChest;
    boolean isChecked = false;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_body_type_filter);
        ButterKnife.bind(this);
        btnFinish = (Button) findViewById(R.id.btn_finish);


        Intent intent = getIntent();
        ArrayList<String> exerciseTypeList = intent.getStringArrayListExtra("exerciseType");
        ArrayList<String> bodypartList = intent.getStringArrayListExtra("bodypart");
        String gender = StringUtils.trimToEmpty(intent.getStringExtra("gender"));
        String order = StringUtils.trimToEmpty(intent.getStringExtra("order"));


        // 필터 창 종료 버튼
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if ("12".equals(gender)) {
            setButtonState(btnMan, true);
            setButtonState(btnGirl, true);
        } else if (gender.contains("1")) {
            setButtonState(btnMan, true);
        } else if (gender.contains("2")) {
            setButtonState(btnGirl, true);
        }


        if ("view_count".equals(order)) {
            setButtonState(btnViewed, true);
        } else if ("created".equals(order)) {
            setButtonState(btnCreated, true);

        }

        if (!Utils.isEmpty(exerciseTypeList)) {
            for (String filter : exerciseTypeList) {
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
        if (!Utils.isEmpty(bodypartList)) {
            for (String filter : bodypartList) {
                if ("1".equals(filter)) {
                    setButtonState(btnLowerBody, true);

                } else if ("2".equals(filter)) {
                    setButtonState(btnShoulder, true);

                } else if ("3".equals(filter)) {
                    setButtonState(btnHip, true);

                } else if ("5".equals(filter)) {
                    setButtonState(btnBodyBack, true);

                } else if ("4".equals(filter)) {
                    setButtonState(btnAbdomen, true);

                } else if ("6".equals(filter)) {
                    setButtonState(btnArm, true);

                } else if ("7".equals(filter)) {
                    setButtonState(btnWholeBody, true);

                } else if ("8".equals(filter)) {
                    setButtonState(btnWaist, true);

                } else if ("9".equals(filter)) {
                    setButtonState(btnBackstab, true);

                } else if ("10".equals(filter)) {
                    setButtonState(btnChest, true);

                }

            }

        }
    }


    @OnClick(R.id.btn_viewed)
    void btnViewedOnClick() {
        if (btnViewed.isChecked()) {
            if (btnCreated.isChecked()) {
                btnCreated.setChecked(false);
                btnCreated.setBackgroundResource(R.color.gray);
            }
            btnViewed.setBackgroundResource(R.color.maincolor);
        } else {
            btnViewed.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_created)
    void btnCreatedOnclick() {
        if (btnCreated.isChecked()) {
            if (btnViewed.isChecked()) {
                btnViewed.setChecked(false);
                btnViewed.setBackgroundResource(R.color.gray);
            }
            btnCreated.setBackgroundResource(R.color.maincolor);
        } else {
            btnCreated.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_man)
    void btnManOnClick() {
        if (btnMan.isChecked()) {
            btnMan.setBackgroundResource(R.color.maincolor);
        } else {
            btnMan.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_girl)
    void btnGirlOnClick() {
        if (btnGirl.isChecked()) {
            btnGirl.setBackgroundResource(R.color.maincolor);
        } else {
            btnGirl.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_pilates)
    void btnPilatesOnClick() {
        if (btnPilates.isChecked()) {
            btnPilates.setBackgroundResource(R.color.maincolor);
        } else {
            btnPilates.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_yoga)
    void btnYogaOnClick() {
        if (btnYoga.isChecked()) {
            btnYoga.setBackgroundResource(R.color.maincolor);
        } else {
            btnYoga.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_weightTR)
    void btnWeightTROnClick() {
        if (btnWeightTR.isChecked()) {
            btnWeightTR.setBackgroundResource(R.color.maincolor);
        } else {
            btnWeightTR.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_home_tr)
    void btnHomeTROnClick() {
        if (btnHomeTR.isChecked()) {
            btnHomeTR.setBackgroundResource(R.color.maincolor);
        } else {
            btnHomeTR.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_crossfit)
    void btnCrossfitOnClick() {
        if (btnCrossfit.isChecked()) {
            btnCrossfit.setBackgroundResource(R.color.maincolor);
        } else {
            btnCrossfit.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_body_correction)
    void btnBodyCorrectionOnClick() {
        if (btnBodyCorrection.isChecked()) {
            btnBodyCorrection.setBackgroundResource(R.color.maincolor);
        } else {
            btnBodyCorrection.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_postpartum)
    void btnPostpartumOnClick() {
        if (btnPostpartum.isChecked()) {
            btnPostpartum.setBackgroundResource(R.color.maincolor);
        } else {
            btnPostpartum.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_balletfits)
    void btnBalletfitsOnClick() {
        if (btnBalletfits.isChecked()) {
            btnBalletfits.setBackgroundResource(R.color.maincolor);
        } else {
            btnBalletfits.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_paul_dance)
    void btnPaulDanceOnClick() {
        if (btnPaulDance.isChecked()) {
            btnPaulDance.setBackgroundResource(R.color.maincolor);
        } else {
            btnPaulDance.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_lower_body)
    void btnLowerBodyOnClick() {
        if (btnLowerBody.isChecked()) {
            btnLowerBody.setBackgroundResource(R.color.maincolor);
        } else {
            btnLowerBody.setBackgroundResource(R.color.gray);
        }

    }

    @OnClick(R.id.btn_shoulder)
    void btnShoulderOnClick() {
        if (btnShoulder.isChecked()) {
            btnShoulder.setBackgroundResource(R.color.maincolor);
        } else {
            btnShoulder.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_hip)
    void btnHipOnClick() {
        if (btnHip.isChecked()) {
            btnHip.setBackgroundResource(R.color.maincolor);
        } else {
            btnHip.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_body_back)
    void btnBodyBackOnClick() {
        if (btnBodyBack.isChecked()) {
            btnBodyBack.setBackgroundResource(R.color.maincolor);
        } else {
            btnBodyBack.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_abdomen)
    void btnAbdomenOnClick() {
        if (btnAbdomen.isChecked()) {
            btnAbdomen.setBackgroundResource(R.color.maincolor);
        } else {
            btnAbdomen.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_arm)
    void btnArmOnClick() {
        if (btnArm.isChecked()) {
            btnArm.setBackgroundResource(R.color.maincolor);
        } else {
            btnArm.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_whole_body)
    void btnWholeBodyOnClick() {
        if (btnWholeBody.isChecked()) {
            btnWholeBody.setBackgroundResource(R.color.maincolor);
        } else {
            btnWholeBody.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_waist)
    void btnWaistOnClick() {
        if (btnWaist.isChecked()) {
            btnWaist.setBackgroundResource(R.color.maincolor);
        } else {
            btnWaist.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_backstab)
    void btnBackstabOnClick() {
        if (btnBackstab.isChecked()) {
            btnBackstab.setBackgroundResource(R.color.maincolor);
        } else {
            btnBackstab.setBackgroundResource(R.color.gray);
        }
    }

    @OnClick(R.id.btn_chest)
    void btnChestOnClick() {
        if (btnChest.isChecked()) {
            btnChest.setBackgroundResource(R.color.maincolor);
        } else {
            btnChest.setBackgroundResource(R.color.gray);
        }

    }


    @OnClick(R.id.tv_apply)
    void tvApplyOnClick() {
        ArrayList<String> exerciseTypeList = new ArrayList<>();
        if (btnPilates.isChecked()) {
            exerciseTypeList.add(btnPilates.getTag().toString());
        }
        if (btnYoga.isChecked()) {
            exerciseTypeList.add(btnYoga.getTag().toString());
        }
        if (btnWeightTR.isChecked()) {
            exerciseTypeList.add(btnWeightTR.getTag().toString());
        }
        if (btnHomeTR.isChecked()) {
            exerciseTypeList.add(btnHomeTR.getTag().toString());
        }
        if (btnBalletfits.isChecked()) {
            exerciseTypeList.add(btnBalletfits.getTag().toString());
        }
        if (btnCrossfit.isChecked()) {
            exerciseTypeList.add(btnCrossfit.getTag().toString());
        }
        if (btnBodyCorrection.isChecked()) {
            exerciseTypeList.add(btnBodyCorrection.getTag().toString());
        }
        if (btnPostpartum.isChecked()) {
            exerciseTypeList.add(btnPostpartum.getTag().toString());
        }
        if (btnPaulDance.isChecked()) {
            exerciseTypeList.add(btnPaulDance.getTag().toString());

        }


        ArrayList<String> bodypartList = new ArrayList<>();
        if (btnLowerBody.isChecked()) {
            bodypartList.add(btnLowerBody.getTag().toString());
        }
        if (btnShoulder.isChecked()) {
            bodypartList.add(btnShoulder.getTag().toString());
        }
        if (btnHip.isChecked()) {
            bodypartList.add(btnHip.getTag().toString());
        }
        if (btnBodyBack.isChecked()) {
            bodypartList.add(btnBodyBack.getTag().toString());
        }
        if (btnAbdomen.isChecked()) {
            bodypartList.add(btnAbdomen.getTag().toString());
        }
        if (btnWholeBody.isChecked()) {
            bodypartList.add(btnWholeBody.getTag().toString());
        }
        if (btnWaist.isChecked()) {
            bodypartList.add(btnWaist.getTag().toString());
        }
        if (btnBackstab.isChecked()) {
            bodypartList.add(btnBackstab.getTag().toString());
        }
        if (btnChest.isChecked()) {
            bodypartList.add(btnChest.getTag().toString());
        }

        String gender = "";
        if (btnMan.isChecked()) {
            gender += btnMan.getTag().toString();
        }
        if (btnGirl.isChecked()) {
            gender += btnGirl.getTag().toString();
        }

        String order = "";
        if (btnViewed.isChecked()) {
            order = btnViewed.getTag().toString();
        } else if (btnCreated.isChecked()) {
            order = btnCreated.getTag().toString();
        }


        Intent intent = new Intent();
        intent.putStringArrayListExtra("exerciseType", exerciseTypeList);
        intent.putStringArrayListExtra("bodypart", bodypartList);
        intent.putExtra("gender", gender);
        intent.putExtra("order", order);
        setResult(RESULT_OK, intent);
        finish();

    }

    @OnClick(R.id.tv_reset)
    void tvResetOnClick() {
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
        setButtonState(btnLowerBody, false);
        btnLowerBody.setBackgroundResource(R.color.gray);
        setButtonState(btnShoulder, false);
        btnShoulder.setBackgroundResource(R.color.gray);
        setButtonState(btnHip, false);
        btnHip.setBackgroundResource(R.color.gray);
        setButtonState(btnBodyBack, false);
        btnBodyBack.setBackgroundResource(R.color.gray);
        setButtonState(btnAbdomen, false);
        btnArm.setBackgroundResource(R.color.gray);
        setButtonState(btnAbdomen, false);
        btnArm.setBackgroundResource(R.color.gray);
        setButtonState(btnWholeBody, false);
        btnWholeBody.setBackgroundResource(R.color.gray);
        setButtonState(btnWaist, false);
        btnWaist.setBackgroundResource(R.color.gray);
        setButtonState(btnBackstab, false);
        btnBackstab.setBackgroundResource(R.color.gray);
        setButtonState(btnChest, false);
        btnChest.setBackgroundResource(R.color.gray);
        setButtonState(btnViewed, false);
        btnViewed.setBackgroundResource(R.color.gray);
        setButtonState(btnCreated, false);
        btnCreated.setBackgroundResource(R.color.gray);
    }

    private void setButtonState(ToggleButton btn, boolean isChecked) {
        btn.setChecked(isChecked);

        if (isChecked) {
            btn.setBackgroundResource(R.color.maincolor);
        } else {
            btn.setBackgroundResource(R.color.gray);
        }
    }
}
