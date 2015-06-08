package com.jjoseba.pecsmobile.util;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Joseba on 08/06/2015.
 */
public class ImageUtils {

    public static String saveViewImage(View view){
        String destPath = System.currentTimeMillis() + ".jpg";
        File destination = new File(FileUtils.getImagesPath() + destPath);

        try {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();

            FileOutputStream ostream = new FileOutputStream(destination);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destPath;
    }
}
