package com.fitmanager.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fitmanager.app.R;

public class VideoFilterActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvSholder;
    TextView tvStomach;
    TextView tvChest;
    TextView tvThigh;
    TextView tvArm;
    TextView tvBody;
    TextView tvHip;
    TextView tvWaist;
    TextView tvCalf;
    TextView tvBodyBack;
    TextView tvBackstab;
    TextView btnApply;
    TextView tvLowBody;




    Button btnFinish;
    TextView tvPilates;
    TextView tvYoga;
    TextView tvWeightTR;
    TextView tvHomeTR;
    TextView tvCrossfit;
    TextView tvBodyCorrection;
    TextView tvPostpartum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_filter);

        setLayout();

    }

    private void setLayout() {
        tvSholder = (TextView) findViewById(R.id.tvSholder);
        tvStomach = (TextView) findViewById(R.id.tvStomach);
        tvChest = (TextView) findViewById(R.id.tvChest);
        tvThigh = (TextView) findViewById(R.id.tvThigh);
        tvArm = (TextView) findViewById(R.id.tvArm);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvHip = (TextView) findViewById(R.id.tvHip);
        tvWaist = (TextView) findViewById(R.id.tvWaist);
        tvCalf = (TextView) findViewById(R.id.tvCalf);
        tvBodyBack = (TextView) findViewById(R.id.tvBodyBack);
        tvBackstab = (TextView) findViewById(R.id.tvBackstab);
        btnApply = (TextView) findViewById(R.id.btnApply);
        tvLowBody = (TextView) findViewById(R.id.tvLowBody);

        btnFinish = (Button) findViewById(R.id.btnFinish);
        tvPilates = (TextView) findViewById(R.id.tvPilates);
        tvYoga = (TextView) findViewById(R.id.btnYoga);
        tvWeightTR = (TextView) findViewById(R.id.tvWeightTR);
        tvHomeTR = (TextView) findViewById(R.id.tvHomeTR);
        tvCrossfit = (TextView) findViewById(R.id.tvCrossfit);
        tvBodyCorrection = (TextView) findViewById(R.id.tvBodyCorrection);
        tvPostpartum = (TextView) findViewById(R.id.tvPostpartum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 어깨 2-2
            case R.id.tvSholder:
                break;

            // 복근 2-5
            case R.id.tvStomach:
                break;

            // 가슴 2-12
            case R.id.tvChest:
                break;

            // 허벅지 2-10
            case R.id.tvThigh:
                break;

            // 팔 2-6
            case R.id.tvArm:
                break;

            // 전신 2-7
            case R.id.tvBody:
                break;

            // 엉덩이 2-3
            case R.id.tvHip:
                break;

            // 허리
            case R.id.tvWaist:
                break;

            // 종아리 2-8
            case R.id.tvCalf:
                break;

            // 등 2-4
            case R.id.tvBodyBack:
                break;

            // 뒷태 2-9
            case R.id.tvBackstab:
                break;

            // 하체 2-1
            case R.id.tvLowBody:
                break;
        }
    }
}
