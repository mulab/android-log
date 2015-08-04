package mu.lab;

import android.content.Context;

import java.io.File;

/**
 * Deprecated, use {@link mu.lab.util.Log}
 */
@Deprecated
public class Log {

    public static File getLogFile() {
        return mu.lab.util.Log.getLogFile();
    }

    public static void clearLogFile(){
        mu.lab.util.Log.clearLogFile();
    }

    private Log(){
    }

    public static synchronized void init(Context context) {
        mu.lab.util.Log.init(context);
    }

    public static int w(String tag, String msg) {
        return mu.lab.util.Log.w(tag, msg);
    }

    public static int w(String tag, Throwable tr) {
        return mu.lab.util.Log.w(tag, tr);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return mu.lab.util.Log.w(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        return mu.lab.util.Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return mu.lab.util.Log.d(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        return mu.lab.util.Log.i(tag, msg);
    }

    public static int e(String tag, String msg) {
        return mu.lab.util.Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return mu.lab.util.Log.e(tag, msg, tr);
    }

}
