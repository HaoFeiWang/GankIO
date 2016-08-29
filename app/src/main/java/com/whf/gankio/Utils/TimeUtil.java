package com.whf.gankio.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String getFormatTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            long millisecond = sdf.parse(time).getTime();//发表时间（毫秒）
            long difference = new Date().getTime() - millisecond;//当前时间与发表时间差
            int minutes = (int) (difference / (1000 * 60));

            if (minutes < 5){
                return "刚刚";
            }else if (minutes < 30){
                return minutes + "分钟前";
            }else if (minutes < 60){
                return "半小时前";
            } if (minutes < 60 * 24){
                return minutes / 60 + "小时前";
            }else if (minutes < 60 * 24 * 30){
                return minutes / (60 * 24) + "天前";
            }else {
                return time.substring(0,9);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "...";
    }
}