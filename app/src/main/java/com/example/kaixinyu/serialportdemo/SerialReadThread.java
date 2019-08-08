package com.example.kaixinyu.serialportdemo;

import android.os.SystemClock;
import android.util.Log;


import com.example.kaixinyu.serialportdemo.Utils.ByteUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kxyu on 2019/8/7
 * 读串口线程
 */
public class SerialReadThread extends Thread {

    private static final String TAG = SerialReadThread.class.getSimpleName();

    public interface ReadThreadCallBack{
        void callBack(String txt);
    }

    private ReadThreadCallBack callBack;
    private BufferedInputStream mInputStream;

    public SerialReadThread(InputStream is) {
        mInputStream = new BufferedInputStream(is);
    }

    @Override
    public void run() {
        byte[] received = new byte[1024];
        int size;

        Log.i(TAG," 开始读线程 ");

        while (true) {

            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {
                int available = mInputStream.available();

                if (available > 0) {
                    size = mInputStream.read(received);
                    if (size > 0) {
                        onDataReceive(received, size);
                    }
                } else {
                    SystemClock.sleep(1000);
                }
            } catch (IOException e) {
                Log.i(TAG,"读取数据失败 "+e.getMessage());
            }
        }

        Log.i(TAG,"结束读进程");
    }

    /**
     * 处理获取到的数据
     * @param received
     * @param size
     */
    private void onDataReceive(byte[] received, int size) {
        // TODO: 2018/3/22 解决粘包、分包等
        String hexStr = ByteUtil.bytes2HexStr(received, 0, size);
        Log.i(TAG,hexStr+"");
        if(callBack != null){
            callBack.callBack(hexStr);
        }
    }

    /**
     * 停止读线程
     */
    public void close() {

        try {
            mInputStream.close();
        } catch (IOException e) {
            Log.i(TAG,"异常", e);
        } finally {
            super.interrupt();
        }
    }

    public void setCallBack(ReadThreadCallBack callBack) {
        this.callBack = callBack;
    }
}
