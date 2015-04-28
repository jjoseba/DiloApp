package com.jjoseba.pecsmobile.util;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private static String storageBaseLocation;
    private static String IMAGES_DIR = "images";

    public static String getImagesPath(){
        return storageBaseLocation + File.separator;
    }

    public static String copyFileToInternal(String filePath){

        String destPath = System.currentTimeMillis() + ".jpg";
        File source = new File(filePath);
        File destination = new File(getImagesPath() + destPath);

        try {
            InputStream is=new FileInputStream(source);
            OutputStream os=new FileOutputStream(destination);
            byte[] buff=new byte[1024];
            int len;
            while((len=is.read(buff))>0){
                os.write(buff,0,len);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destPath;
    }

    public static void initialize(Context applicationContext) {
        File external = applicationContext.getExternalFilesDir(null);

        storageBaseLocation = (external!=null)?external.getAbsolutePath():"";
    }

    public static String getPath(Activity ctx, Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

}
