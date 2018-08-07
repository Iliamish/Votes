package com.alva.vibory.Classes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;



/**
 * Created by alexsid on 08.05.2018.
 */

public class FileUtils {

    public static boolean checkImageFileExist(String fileName, Context context) {

        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imagePath = new File(directory, fileName);
        if (imagePath.exists()) {
            return true;
        }

        return false;
    }

    public static String saveImageFile(Bitmap bitmap, String imageName, Context context) {

        Log.d("Utils", "saveImageFileWithBitmap = " + imageName);

        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagePath = new File(directory, imageName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(imagePath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }

        Log.d("Utils", imagePath.getAbsolutePath());

        return imagePath.getAbsolutePath();
    }

    public static String getPath(Uri uri, Context context) {
        String result = "";

        if (uri.toString().contains("storage")) {
            return uri.toString();
        }

        Cursor cursor = null;
        try {
            String[] data = {MediaStore.Images.Media.DATA};

            cursor = context.getContentResolver().query(uri, data, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }



}
