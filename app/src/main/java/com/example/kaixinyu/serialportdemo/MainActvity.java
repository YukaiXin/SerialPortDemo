package com.example.kaixinyu.serialportdemo;

import android.bluetooth.BluetoothClass;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixinyu.serialportdemo.Utils.SerialPortUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kxyu on 2019/8/7
 */
public class MainActvity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    private final String TAG = MainActvity.class.getSimpleName();
    private Button writeBtn, openBtn;
    private TextView tvRate, tvPortPath, txtTv, deviceNameTv;
    private boolean isStart  = false;
    private boolean isOpenPort = false;
    private StringBuilder txtSb = new StringBuilder();
    private String portPath;
    private Spinner spinnerPort;
    private List<String> mDevices;
    private int mDeviceIndex;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            byte[] mBuffer = new byte[1];
            Arrays.fill(mBuffer, (byte)'P');
            SerialPortUtil.getInstance().sendDataToSerialPort(mBuffer);
            txtSb.append(" 发送 P ");
            txtSb.append("      ");
            txtTv.setText(txtSb.toString());
            return false;
        }
    });
    private final String SERIAL_PORT_PATH = "tty";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        spinnerPort = findViewById(R.id.spinnerPort);
        deviceNameTv =findViewById(R.id.deviceNameTv);
        writeBtn = findViewById(R.id.ButtonWrite);
        openBtn = findViewById(R.id.openBtn);
        tvRate = findViewById(R.id.rateTv);
        tvPortPath = findViewById(R.id.portPathTv);
        txtTv = findViewById(R.id.showTxtTv);
        tvRate.setText("9600");
        portPath = DataConstants.serialMap.get(Build.DEVICE.toLowerCase());
        tvPortPath.setText(portPath);

        deviceNameTv.setText(Build.MODEL);
        initEvent();
        initSpinners();
    }

    /**
     * 初始化设备列表
     */
    private void initSpinners() {

        SerialPortFinder serialPortFinder = new SerialPortFinder();
        // 设备
        mDevices = new ArrayList<>();

        for (String dev: serialPortFinder.getAllDevicesPath()){
            if(dev.contains(SERIAL_PORT_PATH)){
                mDevices.add(dev);
            }
            Log.i(TAG,dev);
        }

        for (String a: serialPortFinder.getAllDevices()){
            Log.i("kxyu_t","   "+a);
        }

        if(mDevices.size() == 0){
            mDevices.add("没有设备");
        }


        ArrayAdapter<String> deviceAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, mDevices);
        deviceAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerPort.setAdapter(deviceAdapter);
        spinnerPort.setOnItemSelectedListener(this);
        spinnerPort.setSelection(mDeviceIndex);
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
                    if(isStart){
                        writeBtn.setText("发送 80");
                        isStart = false;
                    }else {
                        isStart = true;
                        writeBtn.setText("停止");
                        handler.sendEmptyMessage(0);
                    }

                    break;
                case R.id.openBtn:
                    if(isOpenPort){
                        SerialPortUtil.getInstance().closeSerialPort();
                        isOpenPort = false;
                        openBtn.setText("连接");
                    }else {
                        boolean isSuccess = false;
                        try {
                             isSuccess = SerialPortUtil.getInstance().openSerialPort(portPath, 9600, new SerialReadThread.ReadThreadCallBack() {
                                @Override
                                public void callBack(final String txt) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            txtSb.append("接收 ："+txt);
                                            txtSb.append("      ");
                                            txtTv.setText(txtSb.toString());
                                            if(isStart) {
                                                handler.sendEmptyMessageDelayed(0, 1000);
                                            }
                                        }
                                    });
                                }
                            });
                        }finally {
                            isOpenPort = isSuccess;
                            if(isSuccess){
                                Toast.makeText(getApplicationContext(), "打开成功",Toast.LENGTH_SHORT).show();
                                openBtn.setText("断开");
                            }else {
                                Toast.makeText(getApplicationContext(), "打开失败",Toast.LENGTH_SHORT).show();
                                openBtn.setText("打开");
                            }

                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         portPath = mDevices.get(position);
         tvPortPath.setText(portPath);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
