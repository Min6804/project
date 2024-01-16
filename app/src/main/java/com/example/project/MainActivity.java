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

        findViewById(R.id.removebg_pr1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campage();
            }
        });

        findViewById(R.id.removebg_pr2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hopage();
            }
        });

        findViewById(R.id.removebg_pr3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpage();
            }
        });

        findViewById(R.id.removebg_pr4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propage();
            }
        });

        findViewById(R.id.removebg_pr5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reccpage();
            }
        });

        findViewById(R.id.removebg_pr6).setOnClickListener(new View.OnClickListener() {
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
