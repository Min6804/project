package com.example.project;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class AlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static String BIP = "192.168.0.98";
    public static int BPort = 2023;
    public static String BMD = "0";
    private DataHandler dataHandler;
    private Spinner spinner;
    private EditText editText; // Added editText declaration
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ball);

        dataHandler = new DataHandler(this);
        spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextView);

        Button updateButton = findViewById(R.id.updateButton);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.bab,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Added editText initialization
        editText = findViewById(R.id.editText);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataHandler.open();
                Cursor cursor = dataHandler.getAllData();
                StringBuilder resultStringBuilder = new StringBuilder();

                if (cursor.moveToFirst()) {
                    do {
                        String column1Value = cursor.getString(cursor.getColumnIndex("column1"));
                        int column2Value = cursor.getInt(cursor.getColumnIndex("column2"));

                        resultStringBuilder.append("").append(column1Value)
                                .append(", 시간: ").append(column2Value).append("\n");

                    } while (cursor.moveToNext());
                }
                resultTextView.setText(resultStringBuilder.toString());
                String b = resultStringBuilder.toString();
                BMD = b;

                Socket_AsyncTask socketTask = new Socket_AsyncTask();
                socketTask.execute();

                cursor.close();
                dataHandler.close();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TimePickerDialog에서 시간이 설정되었을 때 호출되는 메서드
        String selectedTime = hourOfDay + ":" + minute;
        editText.setText(selectedTime);
    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                InetAddress inetAddress = InetAddress.getByName(BIP);
                socket = new Socket(inetAddress, BPort);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeBytes(BMD);
                dos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
