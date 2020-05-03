package com.example.guru_test_database;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseFragment extends Fragment {

    private static Typeface mtypeface;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mtypeface==null){
            mtypeface= Typeface.createFromAsset(getActivity().getAssets(),"dovemayo.otf");
        }


        setGlobalFont(getActivity().getWindow().getDecorView());
    }



    private void setGlobalFont(View view){


        if(view != null) {
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                int vgCnt = viewGroup.getChildCount();
                for(int i = 0; i<vgCnt; i++) {
                    View v = viewGroup.getChildAt(i);
                    if(v instanceof TextView) {
                        ((TextView) v).setTypeface(mtypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }


    }
}