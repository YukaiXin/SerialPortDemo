package com.example.kaixinyu.serialportdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by kxyu on 2019/8/7
 */
public class MainActvity extends AppCompatActivity {
    private Button writeBtn, openBtn;
    private TextView tvRate, tvPortPath, txtTv, deviceNameTv;
    private boolean isOpenPort = false;
    private StringBuilder txtSb = new StringBuilder();
    private String portPath;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        deviceNameTv =findViewById(R.id.deviceNameTv);
        writeBtn = findViewById(R.id.ButtonWrite);
        openBtn = findViewById(R.id.openBtn);
        tvRate = findViewById(R.id.rateTv);
        tvPortPath = findViewById(R.id.portPathTv);
        txtTv = findViewById(R.id.showTxtTv);
        tvRate.setText("9600");
        portPath = DataConstants.serialMap.get(Build.DEVICE.toLowerCase());
        tvPortPath.setText(portPath);
        deviceNameTv.setText(Build.DEVICE);
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
                    byte[] mBuffer = new byte[1];
                    Arrays.fill(mBuffer, (byte)'P');
                    SerialPortUtil.getInstance().sendDataToSerialPort(mBuffer);
                    txtSb.append(" 发送 P ");
                    txtSb.append("      ");
                    txtTv.setText(txtSb.toString());

                    break;
                case R.id.openBtn:
                    if(isOpenPort){
                        SerialPortUtil.getInstance().closeSerialPort();
                        isOpenPort = false;
                        openBtn.setText("连接");
                    }else {
                        try {
                            SerialPortUtil.getInstance().openSerialPort(portPath, 9600, new SerialReadThread.ReadThreadCallBack() {
                                @Override
                                public void callBack(final String txt) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            txtSb.append("接收 ："+txt);
                                            txtSb.append("      ");
                                            txtTv.setText(txtSb.toString());
                                        }
                                    });
                                }
                            });
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
