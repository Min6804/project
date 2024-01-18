package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AlarmHelper alarmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmHelper = new AlarmHelper();

        // 각 버튼에 클릭 리스너 등록
        findViewById(R.id.cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraPage();
            }
        });

        findViewById(R.id.ho).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVaccinationPage();
            }
        });

        findViewById(R.id.pro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePage();
            }
        });

        findViewById(R.id.rec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecordPage();
            }
        });

        findViewById(R.id.ri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFoodPage();
            }
        });

        findViewById(R.id.ball).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlarmPage();
            }
        });
    }

    // 각 페이지로 이동하는 메서드들
    private void openCameraPage() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void openVaccinationPage() {
        Intent intent = new Intent(this, VaccinationActivity.class);
        startActivity(intent);
    }

    private void openRecordPage() {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    private void openFoodPage() {
        Intent intent = new Intent(this, FoodActivity.class);
        startActivity(intent);
    }

    private void openProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // AlarmActivity로 이동하는 메서드
    private void openAlarmPage() {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
        setAlarm();
    }

    // 알람 설정 메서드
    private void setAlarm() {

        long alarmTimeInMillis = System.currentTimeMillis() + 10000;
        AlarmHelper.startAlarm(this, alarmTimeInMillis);
    }
}
