package com.cdsautomatico.apparkame2.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by alan on 12/12/16.
 */

public class BitmapManipulation {

    Bitmap bitmap;

    public BitmapManipulation(URL remoteImage, int inSampleSize) throws IOException{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        bitmap = BitmapFactory
                .decodeStream(remoteImage.openConnection().getInputStream(), null, options);
    }

    public BitmapManipulation(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void fit(int targetW, int targetH) {
        int currentW = bitmap.getWidth();
        int currentH = bitmap.getHeight();

        int scaleWidth = (currentW * targetH / currentH);
        int scaleHeight = (targetW * currentH / currentW);

        if(scaleWidth < targetW){
            resize(targetW, scaleHeight);
        }else{
            resize(scaleWidth, targetH);
        }

        crop(targetW, targetH);
    }

    public void crop(int targetW, int targetH){
        Bitmap newBitmap;

        int currentW = bitmap.getWidth();
        int currentH = bitmap.getHeight();

        int startX = (currentW - targetW ) / 2;
        int startY = (currentH - targetH) / 2;

        if(startX < 0){
            startX = 0;
        }

        if(startY < 0){
            startY = 0;
        }

        newBitmap = bitmap.createBitmap(bitmap, startX, startY, targetW, targetH);

        if(bitmap != newBitmap) {
            bitmap.recycle();
            bitmap = newBitmap;
        }
    }

    public void resize(Integer targetW, Integer targetH){
        Bitmap newBitmap;

        if(targetW == null && targetH == null){
            return;
        }

        if(targetW == null){
            targetW = (bitmap.getWidth() * targetH) / bitmap.getHeight();
        }

        if(targetH == null){
            targetH = (bitmap.getHeight() * targetW) / bitmap.getWidth();
        }

        newBitmap = bitmap.createScaledBitmap(bitmap, targetW, targetH, true);

        if(bitmap != newBitmap) {
            bitmap.recycle();
            bitmap = newBitmap;
        }
    }

    public String getBase64String(int quality){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
