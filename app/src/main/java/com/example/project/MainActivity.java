package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayPage();
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
    }

    private void openCameraPage() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void openVaccinationPage() {
        Intent intent = new Intent(this, VaccinationActivity.class);
        startActivity(intent);
    }

    private void openPlayPage() {
        Intent intent = new Intent(this, PlayActivity.class);
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
}
