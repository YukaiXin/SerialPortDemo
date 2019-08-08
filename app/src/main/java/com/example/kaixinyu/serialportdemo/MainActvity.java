package com.example.kaixinyu.serialportdemo;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by kxyu on 2019/8/7
 */
public class MainActvity extends AppCompatActivity {
    private OutputStream mOutputStream;
    private InputStream mIn;
    private Button writeBtn, openBtn;
    private TextView tvRate, tvPortPath;
    private SerialReadThread readThread;
    private SerialPort serialPort;
    private boolean isOpenPort = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        writeBtn = findViewById(R.id.ButtonWrite);
        openBtn = findViewById(R.id.openBtn);
        tvRate = findViewById(R.id.rateTv);
        tvPortPath = findViewById(R.id.portPathTv);
        tvRate.setText("9600");
        tvPortPath.setText("/dev/ttyS1");
        initEvent();

    }

    private void initEvent(){
        writeBtn.setOnClickListener(onClickListener);
        openBtn.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ButtonWrite:
                    byte[] mBuffer = new byte[1024];
                    Arrays.fill(mBuffer, (byte) 0x55);
                    try {
                        mOutputStream.write(mBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.openBtn:
                    if(isOpenPort){
                        if(readThread != null){
                            readThread.close();
                        }
                        try {
                            mIn.close();
                            mOutputStream.close();
                            serialPort.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            isOpenPort = false;
                            openBtn.setText("连接");
                        }

                    }else {
                        try {
                            serialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);//your serial port dev
                            mOutputStream = serialPort.getOutputStream();
                            mIn = serialPort.getInputStream();

                            if (readThread == null){
                                readThread = new SerialReadThread(mIn);
                            }
                            readThread.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            isOpenPort = true;
                            openBtn.setText("断开");
                        }
                    }

                    break;
            }
        }
    };
}
