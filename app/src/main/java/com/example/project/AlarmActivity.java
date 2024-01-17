package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "MAIN";

    private TextView timeText;
    private AlarmHelper alarmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ball);

        timeText = findViewById(R.id.time_text);
        alarmHelper = new AlarmHelper();

        Button timeBtn = findViewById(R.id.time_btn);

        // 시간 설정
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        // 알람 취소
        Button alarmCancelBtn = findViewById(R.id.alarm_cancel_btn);
        alarmCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    /**
     * 시간을 정하면 호출되는 메소드
     * @param view 화면
     * @param hourOfDay 시간
     * @param minute 분
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d(TAG, "## onTimeSet ## ");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        // 화면에 시간지정
        updateTimeText(c);

        // 알람설정정
        startAlarm(c.getTimeInMillis());
    }

    /**
     * 화면에 사용자가 선택한 시간을 보여주는 메소드
     * @param c 시간
     */
    private void updateTimeText(Calendar c) {
        Log.d(TAG, "## updateTimeText ## ");
        String timeText = "알람시간: ";
        timeText += DateFormat.format("hh:mm a", c);
        this.timeText.setText(timeText);
    }

    /**
     * 알람 시작
     * @param alarmTimeInMillis 알람 시간 (long)
     */
    private void startAlarm(long alarmTimeInMillis) {
        Log.d(TAG, "## startAlarm ## ");
        alarmHelper.startAlarm(this, alarmTimeInMillis);
    }

    /**
     * 알람 취소
     */
    private void cancelAlarm() {
        Log.d(TAG, "## cancelAlarm ## ");
        alarmHelper.cancelAlarm(this);
        timeText.setText("알람 취소");
    }
}
