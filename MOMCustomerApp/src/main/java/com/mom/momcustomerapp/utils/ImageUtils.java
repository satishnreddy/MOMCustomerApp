package com.mom.momcustomerapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.data.application.Consts;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
 * Created by Nishant on 28/03/15.
 */

public class ImageUtils {
    private static final int MAX_IMAGE_SIZE = 1600;
    private static final int FALLBACK_MAX_IMAGE_SIZE = 1000;

    /**
     * @param options BitmapFactory.Options
     * @return SampleSize by which image should be sampled.
     */
    public static int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > MAX_IMAGE_SIZE || width > MAX_IMAGE_SIZE) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > MAX_IMAGE_SIZE && (halfWidth / inSampleSize) > MAX_IMAGE_SIZE) {
                inSampleSize *= 2;
            }

            if ((inSampleSize == 1) && (height - MAX_IMAGE_SIZE > 400)) {
                inSampleSize = inSampleSize + 1;
            }
        }

        return inSampleSize;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * @param context  The context.
     * @param imageUri Uri of the image to be resized.
     * @return Bitmap with minimum required size for given Uri
     */
    public static Bitmap decodeSampledBitmapFromFile(Context context, Uri imageUri) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

//          BitmapFactory.decodeFile(path, options);

            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, options);


            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String saveBitmapStorage(Bitmap bitmap) {
        if (bitmap != null)
        {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + Consts.STORAGE_CACHE_FOLDER);
            myDir.mkdirs();

            String fname = System.currentTimeMillis() + ".jpeg";

            File file = new File(myDir, fname);

            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                return file.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String saveBitmapAsJpegInStorage(Bitmap bitmap, String filename) {
        if (bitmap != null) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + Consts.STORAGE_FOLDER);
            myDir.mkdirs();

            String fname = filename + ".jpeg";
            File file = new File(myDir, fname);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                return file.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String saveFile(byte[] image) {
        if (image != null) {
            File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "ShopXie" + File.separator);
            myDir.mkdirs();

            String fname = getImageName();
            File file = new File(myDir, fname);
            if (file.exists()) {
                file.delete();
            }

            try {
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                bos.write(image);
                bos.flush();
                out.flush();
                out.close();
                bos.close();
                return file.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getBitmapOriginalFromStorage() {
        String folderRoot = Environment.getExternalStorageDirectory().toString() + Consts.STORAGE_FOLDER + "/";
        String fname = "label.jpeg";

        File file = new File(folderRoot, fname);
        if (file.exists()) {
            try {
                return BitmapFactory.decodeFile(folderRoot + fname);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Bitmap getBitmapBigFromStorage(Uri uri) {
        File file = new File(uri.getPath());
        if (file.exists()) {
            try {
                // First decode with inJustDecodeBounds=true to check dimensions
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(uri.getPath(), options);

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, 400, 400);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                return BitmapFactory.decodeFile(uri.getPath(), options);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Bitmap getBitmapSmallFromStorage(Context context, Uri uri) {

        Bitmap bitmap;

        File file = new File(uri.getPath());
        if (file.exists()) {
            try {
                bitmap = BitmapFactory.decodeFile(uri.getPath());
                return getScaledBitmap(context, bitmap, 300, 300);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Bitmap getBitmapThumbFromStorage(Context context, Uri uri) {

        Bitmap bitmap;

        File file = new File(uri.getPath());
        if (file.exists()) {
            try {
                bitmap = BitmapFactory.decodeFile(uri.getPath());
                return getScaledBitmap(context, bitmap, 100, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static Bitmap getScaledBitmap(Context context, Bitmap bitmap, int maxHeight, int maxWidth) {
        int maxH = (int) dipToPixels(context, maxHeight);
        int maxW = (int) dipToPixels(context, maxWidth);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) (width / height);
            width = maxW;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) (height / width);
            height = maxH;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxH;
            width = maxW;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap getScaledFixedBitmap(Bitmap bitmap, int maxSize, boolean fixedWidth, boolean isGrayscale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        saveImage(bitmap);

        if (fixedWidth) {
            float scale = (float) maxSize / (float) width;
            width = maxSize;
            height = Math.round(height * scale);
        } else {
            float scale = (float) maxSize / (float) height;
            width = Math.round(width * scale);
            height = maxSize;
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        if (isGrayscale) {
            newBitmap = toGrayscale(newBitmap);
        }
        saveImage(newBitmap);
        return newBitmap;
    }

    public static void saveImage(Bitmap bitmap) {
        if (bitmap != null) {
            File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "ShopXie" + File.separator);
            myDir.mkdirs();

            String fname = "label.bmp";
            File file = new File(myDir, fname);
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap getLabel() {
        Bitmap bitmap = null;
        File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "ShopXie" + File.separator);
        String fname = "label.bmp";
        File file = new File(myDir, fname);
        if (file.exists()) {
            try {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static String getImageName() {
        Date now = new Date();
        String name = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now);
        name = "ShopXie_" + name + ".jpeg";

        return name;
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static float pixelsToDip(Context context, float pxValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pxValue, metrics);
    }


    public static ArrayList<Integer> getPieChartColors(int numPieSlices, int baseColor) {
        // Inspiration http://stackoverflow.com/a/19389478

        ArrayList<Integer> colors = new ArrayList<>(numPieSlices);
        colors.add(0, baseColor);
        float hsv[] = new float[3];
        Color.RGBToHSV(Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor), hsv);
        double step = (240.0 / (double) numPieSlices);
        float baseHue = hsv[0];
        for (int i = 1; i < numPieSlices; i++) {
            float nextColorHue = ((float) (baseHue + step * ((float) i))) % ((float) 240.0);
            colors.add(i, Color.HSVToColor(new float[]{nextColorHue, hsv[1], hsv[2]}));
        }

        if (numPieSlices > 2) {
            int holder;
            for (int i = 0, j = numPieSlices / 2; j < numPieSlices; i += 2, j += 2) {
                // Swap
                holder = colors.get(i);
                colors.set(i, colors.get(j));
                colors.set(j, holder);
            }
        }
        return colors;
    }


    public static ArrayList<Integer> getChartColors(Context context, int numPieSlices) {
        // Inspiration http://stackoverflow.com/a/19389478

        ArrayList<Integer> colors = new ArrayList<>(numPieSlices);
        String[] colorsStrings = context.getResources().getStringArray(R.array.colors);

        for (int i = 0; i < numPieSlices; i++) {
            if (i < colorsStrings.length) {
                int color = Color.parseColor(colorsStrings[i]);
                colors.add(color);
            } else {
                int position = i % colorsStrings.length;
                int color = Color.parseColor(colorsStrings[position]);
                colors.add(color);
            }

        }
        return colors;
    }

    private static byte[] pixToRaster(byte[] src) {
        int[] p0 = {0, 0x80};
        int[] p1 = {0, 0x40};
        int[] p2 = {0, 0x20};
        int[] p3 = {0, 0x10};
        int[] p4 = {0, 0x08};
        int[] p5 = {0, 0x04};
        int[] p6 = {0, 0x02};

        byte[] data = new byte[src.length / 8];
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            // 不行，没有加权
            data[i] = (byte) (p0[src[k]] + p1[src[k + 1]] + p2[src[k + 2]]
                    + p3[src[k + 3]] + p4[src[k + 4]] + p5[src[k + 5]]
                    + p6[src[k + 6]] + src[k + 7]);
            k = k + 8;
        }
        return data;
    }

    public static byte[] genRasterData(Bitmap mBitmap) {
        byte[] dithered = thresholdToBWPic(mBitmap); // 阀值算法
        byte[] data = pixToRaster(dithered);
        return data;

    }

    private static byte[] thresholdToBWPic(Bitmap mBitmap) {
        int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
        byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight()];

        mBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight());

        // for the toGrayscale, we need to select a red or green or blue color
        format_K_threshold(pixels, mBitmap.getWidth(), mBitmap.getHeight(), data);

        return data;
    }

    public static void format_K_threshold(int[] orgpixels, int xsize, int ysize, byte[] despixels) {

        int graytotal = 0;
        int i, j;
        int gray;

        int k = 0;
        for (i = 0; i < ysize; i++) {

            for (j = 0; j < xsize; j++) {

                gray = orgpixels[k] & 0xff;
                graytotal += gray;
                k++;
            }
        }
        int grayave = graytotal / ysize / xsize;

        k = 0;
        for (i = 0; i < ysize; i++) {

            for (j = 0; j < xsize; j++) {

                gray = orgpixels[k] & 0xff;

                if (gray > grayave)
                    despixels[k] = 0;// white
                else
                    despixels[k] = 1;

                k++;
            }
        }
    }

}
