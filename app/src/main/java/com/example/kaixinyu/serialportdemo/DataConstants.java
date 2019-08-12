package com.example.kaixinyu.serialportdemo;

import android.util.ArrayMap;

/**
 * Created by kxyu on 2019/8/8
 */
public class DataConstants {

    public static ArrayMap<String, String> serialMap = new ArrayMap<>();

    static {
        /**
         * 商米机器
         * https://docs.sunmi.com/general-function-modules/external-usb-serial-devices/
         */
        serialMap.put("s2", "/dev/ttyHSL3");
        serialMap.put("t2","/dev/ttyHSL3");
        serialMap.put("x2","/dev/ttyHSL3");
        serialMap.put("d2","/dev/ttyS1");
        serialMap.put("d2_d", "/dev/ttyS3");
        /**
         * 联迪机器
         */
        serialMap.put("c7", "/dev/ttyHSL0");
    }

}
