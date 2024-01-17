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
                campage();
            }
        });

        findViewById(R.id.ho).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hopage();
            }
        });

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpage();
            }
        });

        findViewById(R.id.pro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propage();
            }
        });

        findViewById(R.id.rec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reccpage();
            }
        });

        findViewById(R.id.ri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ripage();
            }
        });
    }

    private void campage() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void hopage() {
        Intent intent = new Intent(this, VaccinationActivity.class);
        startActivity(intent);
    }

    private void playpage() {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    private void ripage() {
        Intent intent = new Intent(this, FoodActivity.class);
        startActivity(intent);
    }

    private void reccpage() {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    private void propage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
