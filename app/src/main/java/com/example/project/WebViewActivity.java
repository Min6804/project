package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.TextureView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class WebViewActivity extends AppCompatActivity {

    private Camera2Helper mCamera2Helper;
    private SurfaceView surfaceView; // surfaceView 선언 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        surfaceView = findViewById(R.id.surfaceView); // R.id.surfaceView를 가진 SurfaceView를 찾음
        surfaceView.getHolder().addCallback(surfaceCallback);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        TextureView yourTextureView = findViewById(R.id.realTextureViewId);
        mCamera2Helper = new Camera2Helper(this, yourTextureView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera2Helper.openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera2Helper.closeCamera();
    }

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCamera2Helper.setPreviewSurface(holder.getSurface());
        }


        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Ignored
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignored
        }
    };
}
