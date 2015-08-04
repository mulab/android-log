package mu.lab.util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Log utility
 */
public class Log {
    public static final String LogTag = Log.class.getName();

    public static final String DEBUG = "DEBUG";
    public static final String INFO = "INFO";
    public static final String WARN = "WARN";
    public static final String ERROR = "ERROR";

    private static Handler mHandler;
    private static LogHandlerThread mHandlerThread;
    private static boolean initialized = false;

    private static final String LOG_FILE_NAME = "log.txt";
    private static Context mContext = null;

    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private enum MESSAGE_FLAG{
        APPEND,
        CLEAR
    }

    public static File getLogFile() {
        if(!initialized) return null;
        return new File(mContext.getExternalFilesDir(null), LOG_FILE_NAME);
    }

    public static void clearLogFile(){
        if(initialized){
            Message message = mHandler.obtainMessage(MESSAGE_FLAG.CLEAR.ordinal());
            mHandler.sendMessage(message);
        }
    }

    private Log(Context context){
        mContext = context;
        mHandlerThread = new LogHandlerThread(LogTag);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), mHandlerThread);
    }

    public static synchronized void init(Context context) {
        if(!initialized){
            new Log(context);
        }
        initialized = true;
    }

    public static int w(String tag, String msg) {
        if(initialized){
            logToFile(WARN, tag, msg);
        }
        return android.util.Log.w(tag, msg);
    }

    public static int w(String tag, Throwable tr) {
        if (initialized) {
            logToFile(WARN, tag, getStackTraceString(tr));
        }
        return android.util.Log.w(tag, tr);
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (initialized) {
            logToFile(WARN, tag, msg + '\n' + getStackTraceString(tr));
        }
        return android.util.Log.w(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        if(initialized){
            logToFile(DEBUG, tag, msg);
        }
        return android.util.Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (initialized) {
            logToFile(DEBUG, tag, msg + '\n' + getStackTraceString(tr));
        }
        return android.util.Log.d(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        if(initialized){
            logToFile(INFO, tag, msg);
        }
        return android.util.Log.i(tag, msg);
    }

    public static int e(String tag, String msg) {
        if(initialized){
            logToFile(ERROR, tag, msg);
        }
        return android.util.Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (initialized) {
            logToFile(ERROR, tag, msg + '\n' + getStackTraceString(tr));
        }
        return android.util.Log.e(tag, msg, tr);
    }

    public static String getStackTraceString(Throwable tr){
        return android.util.Log.getStackTraceString(tr);
    }

    private static void logToFile(String level, String tag, String msg) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        Date date = new Date(System.currentTimeMillis());
        String dateString = dateFormatter.format(date);
        String logRecord = String.format("%s\t%s\t%s\t%s\t%s%n", level, dateString, SystemClock.elapsedRealtime(), tag, msg);
        Message message = mHandler.obtainMessage(MESSAGE_FLAG.APPEND.ordinal(), logRecord);
        mHandler.sendMessage(message);
    }

    static class LogHandlerThread extends HandlerThread implements Callback{

        public LogHandlerThread(String name) {
            super(name);
        }

        private FileOutputStream outputStream;
        private final static boolean shouldAppend = true;

        @Override
        public boolean handleMessage(Message msg) {
            //android.util.Log.i("Log", msg.obj.toString());
            File file = getLogFile();
            if(file == null) return false;
            if(!isExternalStorageWritable()){
                android.util.Log.e(LogTag, "External storage not writable, can not write log to log file");
                return false;
            }
            try{
                switch (MESSAGE_FLAG.values()[msg.what]) {
                case APPEND:
                    outputStream = new FileOutputStream(file, shouldAppend);
                    outputStream.write(msg.obj.toString().getBytes());
                    outputStream.close();
                    break;
                case CLEAR:
                    boolean deleteResult = file.delete();
                    if (!deleteResult) {
                        android.util.Log.w(LogTag, "delete log file failed");
                    }
                    break;
                default:
                    break;
                }

            }catch(Exception e){
                android.util.Log.e(LogTag, e.getMessage(), e);
                return false;
            }
            return true;
        }

    }
}
