package com.passurvey.utility;

import android.os.Environment;
import android.util.Log;
import androidx.annotation.NonNull;
import com.passurvey.BuildConfig;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyLog {
    public static void i(@NonNull String tag, final String msg) {
        if (BuildConfig.DEBUG)
            Log.i(tag, msg);
    }
    public static void v(@NonNull String tag, final String msg) {
        if (BuildConfig.DEBUG)
            Log.v(tag, msg);

    }
    public static void d(@NonNull String tag, final String msg) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg);

    }
    public static void e(@NonNull String tag, final String msg) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg);

    }
    public static void w(@NonNull String tag, final String msg) {
        if (BuildConfig.DEBUG)
            Log.w(tag, msg);
    }


    public static void writeLogIntoFile(@NonNull String text,@NonNull String fileName) {
        final File logFile = new File(Environment.getExternalStorageDirectory() +"/"+ fileName + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // BufferedWriter for performance, true to set append to file flag
            final BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
