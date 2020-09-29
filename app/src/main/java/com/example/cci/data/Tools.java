package com.example.cci.data;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.example.cci.R;

import java.io.IOException;

public class Tools {

    private static float getAPIVersion() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            double release= Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
            strBuild.append(release);

            //int versionRelease = (int) Double.parseDouble(Build.VERSION.RELEASE);
            //strBuild.append(versionRelease);

            //strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 1));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "TOOLS - get api version error" + e.getMessage());
        }
        return f;       //f.floatValue();
    }

    public static void systemBarLolipop(Activity act){
        if (getAPIVersion() >= 5.0) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static boolean InternetConnection(Activity act) {
        ConnectivityManager conMgr = (ConnectivityManager) act.getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
