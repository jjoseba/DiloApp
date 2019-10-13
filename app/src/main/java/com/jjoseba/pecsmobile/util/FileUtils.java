package com.jjoseba.pecsmobile.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jjoseba.pecsmobile.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.core.content.FileProvider;

public class FileUtils {

    private static String storageBaseLocation;
    private static Uri cropTempResultURI;

    public static String getImagesPath(){
        return storageBaseLocation + File.separator;
    }

    public static Uri getTempImageURI(Context ctx){
        if (cropTempResultURI == null){
            String fileImagePath = getImagesPath() + "temp_image.jpg";
            cropTempResultURI = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", new File(fileImagePath));

        }
        return cropTempResultURI;
    }

    public static String copyFromUri(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return null;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dstFile.getPath();
    }

    public static String copyFile(String fileSourcePath, String fileDestinationName){

        String destPath = (fileDestinationName != null)
                ? fileDestinationName
                : fileSourcePath.substring(fileSourcePath.lastIndexOf(File.pathSeparator));
        File source = new File(fileSourcePath);
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

        return destination.getPath();
    }


    public static String copyFileTemp(Context ctx, Uri fileUri){
        String tempPath = "temp.jpg";
        File destination = new File(getImagesPath() + tempPath);
        return copyFromUri(ctx, fileUri, destination);
    }


    public static String copyFileToInternal(String filePath){

        String destPath = System.currentTimeMillis() + ".jpg";
        copyFile(filePath, destPath );

        return destPath;
    }

    public static void initialize(Context applicationContext) {
        File external = applicationContext.getExternalFilesDir(null);

        storageBaseLocation = (external!=null)?external.getAbsolutePath():"";
    }

    public static boolean deleteImage(String imageFilename) {
        File imageFile = new File(getImagesPath() + imageFilename);
        return imageFile.exists() && imageFile.delete();
    }

    public static String getPath(Context ctx, Uri uri) {
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
            try{
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path =  cursor.getString(column_index);
                cursor.close();
                return path;
            }
            catch (IllegalArgumentException e){

            }
        }
        // this is our fallback here
        return uri.getPath();
    }

}
