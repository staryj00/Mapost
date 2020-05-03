package com.example.guru_test_database.member;

import java.io.Serializable;

public class Member implements Serializable {

    public String id;
    public String name;
    public String imageUri;

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }

}
