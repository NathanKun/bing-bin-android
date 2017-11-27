package io.bingbin.bingbinandroid.utils;

import android.app.Activity;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Common utils
 *
 * @author Junyang HE
 */
public abstract class CommonUtil {

    /**
     * Get file from a content uri
     * @param uri       input uri
     * @param activity  activity
     * @return          file stored in cache
     */
    public static File uriToFile(Uri uri, Activity activity) {
        try {
            InputStream input = activity.getContentResolver().openInputStream(uri);
            File file = new File(activity.getCacheDir(), "cacheFileAppeal.srl");
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;

            assert input != null;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
            input.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
