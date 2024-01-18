package com.example.project;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class RecordActivity extends AppCompatActivity {
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private Button btnRecord;
    private Button btnPlay;
    private Button btnSend;
    private Handler handler;
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recpage);
        btnRecord = findViewById(R.id.btnRecord);
        btnPlay = findViewById(R.id.btnPlay);
        btnSend = findViewById(R.id.btnSend);

        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();
        handler = new Handler();

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isRecording) {
                        startRecording();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopRecording();
                                btnRecord.setEnabled(false);
                                btnPlay.setVisibility(View.VISIBLE);
                                btnSend.setVisibility(View.VISIBLE);
                            }
                        }, 2000);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlayback();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendFileTask().execute(audioFilePath);
            }
        });
    }

    private void startRecording() {
        if (isMicrophoneAvailable()) {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String fileName = "recorded_audio_" + timeStamp + ".3gp";

                audioFilePath = getExternalFilesDir(null).getAbsolutePath() + "/" + fileName;

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setOutputFile(audioFilePath);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
            } catch (IOException | IllegalStateException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {

            //Log.e("Recording", "Microphone not available");
        }
    }

    private void stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = new MediaRecorder();
                isRecording = false;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void startPlayback() {
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isMicrophoneAvailable() {
        return true;
    }

    private class SendFileTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String filePath = params[0];
                File file = new File(filePath);

                Socket socket = new Socket("192.168.0.98", 2023);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                dos.writeUTF(file.getName());
                dos.writeLong(file.length());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(file);
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                fis.close();
                dos.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}