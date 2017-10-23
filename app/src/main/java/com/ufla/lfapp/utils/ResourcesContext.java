package com.ufla.lfapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by carlos on 4/24/17.
 */

public class ResourcesContext {

    private Context context;
    private Resources resources;
    private Resources.Theme theme;
    private static ResourcesContext resourcesContext;
    public static boolean isTest = false;

    private ResourcesContext() {

    }

    private static ResourcesContext getInstance() {
        if (resourcesContext == null) {
            resourcesContext = new ResourcesContext();
        }
        return resourcesContext;
    }

    public static void updateContext(Context context) {
        getInstance().context = context;
    }

    public static String getString(int id) {
        if (isTest) {
            return ResourcesTest.getString(id);
        }
        ResourcesContext rc = ResourcesContext.getInstance();
        if (rc.resources == null) {
            rc.resources = rc.context.getResources();
        }
        return rc.resources.getString(id);
    }

//    public static int getColor(int id) {
//        if (resources == null) {
//            resources = Resources.getSystem();
//        }
//        if (Build.VERSION.SDK_INT >= 23) {
//            return ContextCompat.getColor(context, id);
//        }
//        return resources.getColor(id);
//    }
}
