package com.example.guru_test_database.member;

import java.io.Serializable;

public class Memo implements Serializable {
    public String title;
    public String content;
    public boolean share;
    public boolean isChecked;
    public double latitude;
    public double longitude;
    public String type;

    @Override
    public String toString() {
        return "Memo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", share=" + share +
                ", isChecked=" + isChecked +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type=" + type +
                '}';
    }
}
