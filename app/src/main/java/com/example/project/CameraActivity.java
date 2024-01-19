package com.example.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class CameraActivity extends AppCompatActivity {

    public static String mIP = "192.168.0.98";
    public static int mPort = 2023;
    public static String CMD = "0";
    private Camera2Helper mCamera2Helper;
    private TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setSupportMultipleWindows(true);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl("http://192.168.0.98:8081/");

        textureView = findViewById(R.id.cameraPreview);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_camera);
            Button btnCamIcon = findViewById(R.id.btnCamIcon);
            btnCamIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // "cam" 아이콘을 클릭하면 CameraActivity로 이동
                    Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(cameraIntent);
                }
            });
        }

        textureView.setSurfaceTextureListener(surfaceTextureListener);

        textureView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                captureImage();
                return true;
            }
            return false;
        });

        Button btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(v -> navigateToCameraActivity2());



            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        1);
            } else {
                initCamera();
            }


    }

    private void initCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCamera2Helper = new Camera2Helper(this, textureView);
            mCamera2Helper.setCameraCallback(bitmap -> {
            });
            mCamera2Helper.openCamera();
        }
    }

    private final TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            mCamera2Helper.setPreviewSurface(new Surface(surface));
        }


        @Override
        public void onSurfaceTextureSizeChanged(@NonNull android.graphics.SurfaceTexture surface, int width, int height) {
            // Ignored
        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull android.graphics.SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull android.graphics.SurfaceTexture surface) {
            // Ignored
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initCamera();
        }
    }

    private void captureImage() {
        if (mCamera2Helper != null) {
            Bitmap capturedBitmap = mCamera2Helper.captureStillPicture();
            if (capturedBitmap != null) {
                String imagePath = saveBitmapToExternalStorage(capturedBitmap);
                sendImageToCameraActivity2(imagePath);
            }
        }
    }

    private String saveBitmapToExternalStorage(Bitmap bitmap) {
        String fileName = "captured_image.jpg";
        File directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        try (OutputStream stream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendImageToCameraActivity2(String imagePath) {
        Intent intent = new Intent(this, CameraActivity2.class);
        intent.putExtra("imagePath", imagePath);
        startActivity(intent);
    }

    private void navigateToCameraActivity2() {
        Intent galleryIntent = new Intent(CameraActivity.this, CameraActivity2.class);
        startActivity(galleryIntent);
    }

    private void navigateToMain() {
        finish();
    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        protected Void doInBackground(Void... params) {
            try {
                InetAddress inetAddress = InetAddress.getByName(CameraActivity.mIP);
                socket = new Socket(inetAddress, CameraActivity.mPort);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeBytes(CMD);
                dos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}