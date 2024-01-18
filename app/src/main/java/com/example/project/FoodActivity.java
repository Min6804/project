package com.example.project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class FoodActivity extends AppCompatActivity {
    public static String mIP = "192.168.0.98";
    public static int mPort = 2023;
    public static String CMD = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ripage);
        Button btn1 = findViewById(R.id.babButton);
        Button btn2 = findViewById(R.id.mulButton);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "W";
                Socket_AsyncTask socket_on = new Socket_AsyncTask();
                socket_on.execute();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "F";
                Socket_AsyncTask socket_on = new Socket_AsyncTask();
                socket_on.execute();
            }
        });

    }
    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>{
        Socket socket;
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(FoodActivity.mIP);
                socket = new Socket(inetAddress,FoodActivity.mPort);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeBytes(CMD);
                dos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
