package com.example.guru_test_database;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    MemoFragment memoFragment = new MemoFragment();
    TodoFragment todoFragment = new TodoFragment();

    int tabCount;

    public MyPagerAdapter(FragmentManager fm, int count){
        super(fm);
        this.tabCount=count;
    }

    @Override
    public Fragment getItem(int i){
        switch(i){
            case 0:
                return memoFragment;
            case 1:
                return todoFragment;
        }
        return null;
    }

    @Override
    public int getCount(){return tabCount;}

} //MyPagerAdapter
