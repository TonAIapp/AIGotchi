package com.digwex;

import android.app.Activity;
import android.content.Intent;

public class AndroidUtils {
    public static void exitApplicationAnRemoveFromRecent(Activity activity) {

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            activity.finishAndRemoveTask();
        } else {
            activity.finish();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);

        intent.addCategory(Intent.CATEGORY_HOME)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                        Intent.FLAG_ACTIVITY_NO_ANIMATION);

        activity.startActivity(intent);
        System.exit(0);
    }
}
