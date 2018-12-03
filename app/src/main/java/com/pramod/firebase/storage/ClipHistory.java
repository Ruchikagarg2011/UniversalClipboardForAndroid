package com.pramod.firebase.storage;

import android.support.annotation.NonNull;

import com.pramod.firebase.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class ClipHistory implements  Comparable{

    String deviceName;
    String clipContent;
    String messageType;
    String timestamp;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getClipContent() {
        return clipContent;
    }

    public void setClipContent(String clipContent) {
        this.clipContent = clipContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public ClipHistory(String deviceName, String clipContent, String messageType, String timestamp) {
        this.deviceName = deviceName;
        this.clipContent = clipContent;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

    public ClipHistory(Map<String, String> map) {
        deviceName = map.get("deviceName");
        clipContent = map.get("clipContent");
        messageType = map.get("messageType");
        timestamp = map.get("timestamp");
    }

    public boolean isText() {
        return getMessageType().equals(Constants.TYPE_TEXT);
    }


    @Override
    public boolean equals(Object obj) {
        ClipHistory second = (ClipHistory) obj;
        if (second == null) {
            return false;
        }
        return second.getMessageType().equals(messageType)
                && second.getClipContent().equals(clipContent);
    }


    @Override
    public int compareTo(@NonNull Object o) {
        ClipHistory clipHistory = (ClipHistory)o;
        Date o1_date =new Date();
        Date o2_date = new Date();
        try {
             o1_date = convertToDate(this.getTimestamp());
             o2_date = convertToDate(((ClipHistory) o).getTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(this.getTimestamp().equals(((ClipHistory) o).getTimestamp())){
            return 0;
        }else{
            //return (((ClipHistory) o).getTimestamp().compareTo(this.getTimestamp()));
            return o2_date.compareTo(o1_date);
        }
    }

    private Date convertToDate(String o1_date_obj) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateString = format.format(new Date());
        Date  date  = format.parse(o1_date_obj);
        return date;
    }
}
