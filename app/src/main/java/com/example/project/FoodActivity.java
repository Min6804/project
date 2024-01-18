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
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class FoodActivity extends AppCompatActivity {
    public static String mIP = "192.168.0.98";
    public static int mPort = 2023;
    public static String CMD = "0";
    private DataHandler dataHandler;
    private Spinner spinner;
    private EditText editText;
    private TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ripage);
        Button btn1 = findViewById(R.id.babButton);
        Button btn2 = findViewById(R.id.mulButton);
        dataHandler = new DataHandler(this);
        spinner = findViewById(R.id.spainner);
        editText = findViewById(R.id.edittext);
        resultTextView = findViewById(R.id.resultTextView);

        Button updateButton = findViewById(R.id.updateButton);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.bab,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dataHandler.open();
        Cursor cursor = dataHandler.getAllData();
        StringBuilder resultStringBuilder = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String column1Value = cursor.getString(cursor.getColumnIndex("column1"));
                String column2Value = cursor.getString(cursor.getColumnIndex("column2"));

                resultStringBuilder.append("").append(column1Value)
                        .append(", 시간: ").append(column2Value).append("\n");
            } while (cursor.moveToNext());
        }
        resultTextView.setText(resultStringBuilder);
        cursor.close();
        dataHandler.close();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String originalColumn1Value = spinner.getSelectedItem().toString();
                String newColumn1Value = spinner.getSelectedItem().toString();
                String newColumn2Value = editText.getText().toString();

                dataHandler.open();
                int result = dataHandler.updateData(originalColumn1Value, newColumn1Value, newColumn2Value);
                dataHandler.close();

                if (result > 0) {
                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                }
                dataHandler.open();
                Cursor cursor = dataHandler.getAllData();
                StringBuilder resultStringBuilder = new StringBuilder();
                String p="";
                if (cursor.moveToFirst()) {
                    do {
                        String column1Value = cursor.getString(cursor.getColumnIndex("column1"));
                        String column2Value = cursor.getString(cursor.getColumnIndex("column2"));
                        p=p+column1Value+column2Value;
                        resultStringBuilder.append("").append(column1Value)
                                .append(", 시간: ").append(column2Value).append("\n");

                    } while (cursor.moveToNext());
                }
                resultTextView.setText(resultStringBuilder);
                cursor.close();
                dataHandler.close();
                Toast.makeText(getApplicationContext(),p,Toast.LENGTH_SHORT).show();
                CMD = p;
                Socket_AsyncTask socketl = new Socket_AsyncTask();
                socketl.execute();
            }
        });
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
