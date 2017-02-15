package com.zxms.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityControl {
    public static List<Activity> activities = new ArrayList<>();

    /**
     * add activity
     * @param activity
     */
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * remove activity
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * finish all activities
     */
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
