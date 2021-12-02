package com.example.guru_test_database;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public MyPagerAdapter(FragmentManager fm, int count){
        super(fm);
        this.tabCount=count;
    }

    @Override
    public Fragment getItem(int i){
        switch(i){
            case 0:
                return new MemoFragment();
            case 1:
                return new com.example.guru_test_database.TodoFragment();
        }
        return null;
    }

    @Override
    public int getCount(){return tabCount;}

} //MyPagerAdapter
