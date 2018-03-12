package sun.geofferysun.weblongclick.utils;

/**
 * Created by gumenghao on 18/3/12.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import sun.geofferysun.weblongclick.R;

public class ADFilterTool {

    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }

}
