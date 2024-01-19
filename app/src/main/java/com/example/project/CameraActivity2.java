package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class CameraActivity2 extends AppCompatActivity {

    private LinearLayout galleryLayout; // 추가: LinearLayout 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campage2);

        galleryLayout = findViewById(R.id.galleryLayout);

        // 예시로 ImageView를 동적으로 생성하여 galleryLayout에 추가하는 코드
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        imageView.setImageResource(R.drawable.cam);
        galleryLayout.addView(imageView);

        // addImageToGallery 메서드 호출 (코드 추가)
        addImageToGallery("your_image_path");

        // navigateToCameraActivity 메서드 호출 (코드 추가)
        navigateToCameraActivity();
    }

    private void addImageToGallery(String imagePath) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 20);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


    }

    private void navigateToCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
        finish();
    }
}
