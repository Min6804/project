package com.example.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;  // ImageView를 import 추가
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import android.util.Log;

public class CameraActivity extends AppCompatActivity {
    public static String mIP = "192.168.0.98";
    public static int mPort = 2023;
    public static String CMD = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 레이아웃 파일이 activity_main.xml일 경우
        ImageView camImage = findViewById(R.id.cam);

        // camImage가 null인지 확인
        if (camImage != null) {
            camImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CameraActivity", "camImage clicked");
                }
            });
        } else {

            Log.e("CameraActivity", "camImage is null");
        }

        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        WebView myWebView = findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setSupportMultipleWindows(true);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl("http://192.168.0.98:8081/");

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"왼쪽",Toast.LENGTH_SHORT).show();
                CMD = "L";
                new Socket_AsyncTask().execute();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"오른쪽",Toast.LENGTH_SHORT).show();
                CMD = "R";
                new Socket_AsyncTask().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        WebView myWebView = findViewById(R.id.webView);
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            finish();
            super.onBackPressed();
        }
    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        protected Void doInBackground(Void... params) {
            try {
                InetAddress inetAddress = InetAddress.getByName(mIP);
                socket = new Socket(inetAddress, mPort);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeBytes(CMD);
                dos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
