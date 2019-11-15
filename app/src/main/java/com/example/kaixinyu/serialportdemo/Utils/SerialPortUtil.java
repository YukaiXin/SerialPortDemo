package com.example.kaixinyu.serialportdemo.Utils;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixinyu.serialportdemo.SerialPort;
import com.example.kaixinyu.serialportdemo.SerialReadThread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by kxyu on 2019/8/7
 */
public class SerialPortUtil {

    private final String TAG = SerialPortUtil.class.getSimpleName();

    private static SerialPortUtil instance = null;
    private SerialPort serialPort;
    private OutputStream outputStream;
    private InputStream inputStream;
    private SerialReadThread readThread;
    private SerialPortUtil(){

    }

    public static SerialPortUtil getInstance(){
        if (instance == null){
            instance = new SerialPortUtil();
        }
        return instance;
    }


    /**
     * 打开串口
     * @param portPath
     * @param baudRate
     * @param callBack
     */
    public boolean openSerialPort(String portPath, int baudRate, SerialReadThread.ReadThreadCallBack callBack){
        boolean isOpen = false;
        try {
            Log.i("kxyu_port","  "+portPath+"  baudRate  : "+baudRate);
            serialPort = new SerialPort(new File(portPath), baudRate,0);
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();

            if(readThread != null){
                readThread.close();
                readThread = null;
            }
            readThread = new SerialReadThread(inputStream);
            readThread.setCallBack(callBack);
            readThread.start();
            Log.i("kxyu_port"," 串口打开成功 ");
            isOpen = true;
        }catch (IOException e){
            Log.i(TAG, "串口连接失败");
            e.printStackTrace();
            isOpen = false;
        }finally {
            return isOpen;
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort(){
        try{
            if(inputStream != null){
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
            if(serialPort != null){
                serialPort.close();
                serialPort = null;
            }
            if(readThread != null){
                readThread.close();
                readThread = null;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 向串口发送数据
     * @param data
     */
    public void sendDataToSerialPort(byte[] data){


        Log.i("kxyu_p","  向串口发送数据  ");
        if(readThread != null&& !readThread.isAlive()){
            Log.i("kxyu_p","  readThread11111111  ");
            readThread.start();
        }
        try{
            int dataLength = data.length;
            if(dataLength > 0 && outputStream != null){
                outputStream.write(data);
//                outputStream.write('\n');
//                outputStream.flush();
            }

        }catch (IOException e){
            Log.i("kxyu_s"," 发送失败   ");
            e.printStackTrace();
        }
    }


}
