package com.example.trial;

public class UserHelperClass {

    public UserHelperClass(){
    }

    public UserHelperClass(String type, String key, String file_name, String date, String time) {
        this.type = type;
        this.key = key;
        this.file_name = file_name;
        this.date = date;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    String type;
    String key;
    String file_name;
    String date;
    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
