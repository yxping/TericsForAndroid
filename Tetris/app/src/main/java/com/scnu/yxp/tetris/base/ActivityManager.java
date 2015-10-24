package com.scnu.yxp.tetris.base;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by yxp on 2015/10/23.
 */
public class ActivityManager {
    private static ActivityManager am;
    private static ArrayList<Activity> lists;

    public ActivityManager(){

    }

    public void addActivity(Activity activity) {
        if(lists == null) return;
        lists.add(activity);
    }

    public void closeAllActivity() {
        for (Activity a : lists) {
            if(!a.isFinishing())
                a.finish();
        }
    }

    public static ActivityManager getInstance(){
        if(am != null) return am;
        am = new ActivityManager();
        lists = new ArrayList<Activity>();
        return am;
    }
}
