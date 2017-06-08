package com.example.lwl.download;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwl on 2017/6/8.
 */
public class ActivityCollector{
        private static List<Activity> activities = new ArrayList<>();

        public static void addActivity(Activity activity) {
            activities.add(activity);
        }

        public static void removeActivity(Activity activity) {
            activities.remove(activity);
        }

        public static void fillActivity() {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
            activities.clear();
        }

        public static Activity getTopActivity() {
            return activities.get(activities.size()-1);

        }
}
