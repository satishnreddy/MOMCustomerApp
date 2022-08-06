package com.mom.momcustomerapp.customviews;

import android.app.Activity;
import android.content.ComponentName;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import androidx.core.content.FileProvider;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;


import com.mom.momcustomerapp.BuildConfig;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/*
 * Created by Nishant on 30-03-2015.
 */
public class CameraButton extends androidx.appcompat.widget.AppCompatImageButton
        implements View.OnClickListener, CameraButtonChooserDialogFragment.ChooserDialogClickListener
{

    public static final int TAKE_PICTURE_REQUEST_CODE = 727;
    public static final int SELECT_PICTURE_REQUEST_CODE = 737;
    public static final int AFTER_IMAGE_CROPPING = 747;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;

    private Activity mActivity;
    private Uri sourceImageUri, outputImageUri;
    private CameraButtonResultListener mCameraButtonResultListener;
    private List<Intent> intents;
    private String imageName;
    private int mAspectX = 0, mAspectY = 0;

    public CameraButton(Context context) {
        super(context);
        init(context);
    }

    public CameraButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof Activity) {
            mActivity = (Activity) context;
            try {
                mCameraButtonResultListener = (CameraButtonResultListener) mActivity;
            } catch (ClassCastException e) {
                throw new ClassCastException(mActivity.toString() + " must implement CameraButtonResultListener");
            }
        }

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (mCameraButtonResultListener != null)
        {
            getPickIntent();
        } else {
            throw new ClassCastException("Please set CameraButtonResultListener");
        }
    }

    public void handleActivityResults(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == TAKE_PICTURE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                beginCrop(true);
            }
        } else if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                sourceImageUri = data.getData();
                beginCrop(false);
            }
        } else if (requestCode == AFTER_IMAGE_CROPPING) {
            handleCrop(resultCode);
        }
    }

    @Override
    public void onDialogItemClick(int position)
    {
        Intent intent = intents.get(position);
        try {
            if (intent != null) {
                if (MediaStore.ACTION_IMAGE_CAPTURE.equalsIgnoreCase(intent.getAction())) {
                    mActivity.startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
                } else if (Intent.ACTION_PICK.equalsIgnoreCase(intent.getAction())
                        || Intent.ACTION_GET_CONTENT.equalsIgnoreCase(intent.getAction())) {
                    mActivity.startActivityForResult(intent, SELECT_PICTURE_REQUEST_CODE);
                }
            }
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (e instanceof FileUriExposedException) {
                }
            }
        }
    }


    public CameraButtonResultListener getCameraButtonResultListener()
    {
        return mCameraButtonResultListener;
    }

    public void setCameraButtonResultListener(CameraButtonResultListener cameraButtonResultListener) {
        this.mCameraButtonResultListener = cameraButtonResultListener;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setAspectRatio(int aspectX, int aspectY) {
        mAspectX = aspectX;
        mAspectY = aspectY;
    }

    private boolean mayRequestStorageNCamera()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }

        int permissionWrite = mActivity.checkSelfPermission(WRITE_EXTERNAL_STORAGE);
        int permissionCamera = mActivity.checkSelfPermission(CAMERA);

        if (permissionWrite == PackageManager.PERMISSION_GRANTED && permissionCamera == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            mActivity.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return false;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPickIntent();
            }
        }

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPickIntent();
            }
        }

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            getPickIntent();
        }
    }

    private void getPickIntent() {
        if (!mayRequestStorageNCamera()) {
            return;
        }

        List<ResolveInfo> resolveInfos = new ArrayList<>();
        intents = getCameraIntentsList(resolveInfos);
        intents.addAll(getGalleryIntentsList(resolveInfos));

        showChooserDialog(resolveInfos);
    }

    private List<Intent> getCameraIntentsList(List<ResolveInfo> resolveInfos)
    {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + Consts.STORAGE_FOLDER);
        myDir.mkdirs();

        if (TextUtils.isEmpty(imageName))
        {
            String fname = ImageUtils.getImageName();

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                sourceImageUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID+
                        ".fileprovider", new File(myDir, fname));
            } else {
                sourceImageUri = Uri.fromFile(new File(myDir, fname));
            }






        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                sourceImageUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID +
                        ".fileprovider", new File(myDir, imageName));
            } else {
                sourceImageUri = Uri.fromFile(new File(myDir, imageName));
            }
        }

        if (sourceImageUri == null){
            Toast.makeText(mActivity,"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
            mActivity.finish();
            return new ArrayList<>();
        }

        final List<Intent> intents = new ArrayList<>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = mActivity.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, sourceImageUri);

            if (packageName.toLowerCase().contains("camera") || packageName.toLowerCase().contains("gallery")) {
                intents.add(intent);
                resolveInfos.add(res);
            }
        }
        return intents;
    }

    private List<Intent> getGalleryIntentsList(List<ResolveInfo> resolveInfos) {
        final List<Intent> galleryIntentsList = new ArrayList<>();
        final List<Intent> nonGalleryIntentsList = new ArrayList<>();

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/jpeg");
        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        final PackageManager packageManager = mActivity.getPackageManager();
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        List<String> packageNames = new ArrayList<>();

        galleryIntentsList.add(null);
        resolveInfos.add(null);

        for (ResolveInfo res : listGallery)
        {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            galleryIntentsList.add(intent);
            resolveInfos.add(res);
            packageNames.add(packageName);
        }

        galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        listGallery.clear();
        listGallery = packageManager.queryIntentActivities(galleryIntent, 0);

        galleryIntentsList.add(null);
        resolveInfos.add(null);

        for (ResolveInfo res : listGallery) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);

            if (!packageNames.contains(packageName)) {
                galleryIntentsList.add(intent);
                resolveInfos.add(res);
                packageNames.add(packageName);
            }
        }

        if (galleryIntentsList.isEmpty()) {
            return nonGalleryIntentsList;
        }

        return galleryIntentsList;
    }




    String mCropType = "";

    public void setCropType(String aCropType) {
        mCropType = aCropType;
    }

    private void beginCrop(boolean isCameraIntent) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + Consts.STORAGE_FOLDER);
        myDir.mkdirs();

        if (TextUtils.isEmpty(imageName)) {
            String fname = ImageUtils.getFileName(mActivity, sourceImageUri);
            String extention = fname.substring(fname.lastIndexOf(".") + 1);
            if (!extention.contains(".jp")) {
                fname = fname.substring(0, fname.lastIndexOf(".")) + ".jpeg";
            }

            if (isCameraIntent) {
                outputImageUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID +
                        ".fileprovider", new File(myDir, fname));
            } else {
            outputImageUri = Uri.fromFile(new File(myDir, fname));
            }
        } else {
            if (isCameraIntent) {
                outputImageUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID +
                        ".fileprovider", new File(myDir, imageName));
            } else {
            outputImageUri = Uri.fromFile(new File(myDir, imageName));
            }
        }

        if (isCameraIntent){

            try {

                InputStream inputStream = mActivity.getContentResolver().openInputStream(sourceImageUri);

                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                File targetFile = new File(myDir, "profile_pic.jpeg");
                //neededRotation(targetFile);
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);

                outputImageUri = Uri.fromFile(targetFile);
                outStream.close();
                inputStream.close();

                boolean shouldReturnWithoutCompression = false;

                if (shouldReturnWithoutCompression){
                    handleCrop(Activity.RESULT_OK);
                    return;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(mActivity,mActivity.getString(R.string.sww),Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mActivity,mActivity.getString(R.string.sww),Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(mActivity,mActivity.getString(R.string.sww),Toast.LENGTH_SHORT).show();
            }
        }


//        Bitmap bitmap = null;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), outputImageUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if(bitmap.getWidth() > bitmap.getHeight())
//        {
//            // landscape
//
//            String land = "land";
//        }else
//        {
//            String pot = "pot";
//        }


        if(isCameraIntent)
        {
            Intent intent = new Intent(mActivity, ImageCroppingActivity.class);
            intent.setData(outputImageUri);
            intent.putExtra("aspectX", mAspectX);
            intent.putExtra("aspectY", mAspectY);
            intent.putExtra("maxX", 1600);
            intent.putExtra("maxY", 1600);
            intent.putExtra("cropType", mCropType);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mActivity.startActivityForResult(intent, AFTER_IMAGE_CROPPING);
        }
            else {
            Intent intent = new Intent(mActivity, ImageCroppingActivity.class);
            intent.setData(sourceImageUri);
            intent.putExtra("aspectX", mAspectX);
            intent.putExtra("aspectY", mAspectY);
            intent.putExtra("maxX", 1600);
            intent.putExtra("maxY", 1600);
            intent.putExtra("cropType", mCropType);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mActivity.startActivityForResult(intent, AFTER_IMAGE_CROPPING);
        }
    }


    private void handleCrop(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            mCameraButtonResultListener.imageSaved(outputImageUri);
        }
    }



    /* Creates a dialog for an chooser */
    private void showChooserDialog(List<ResolveInfo> resolveInfoName)
    {
        CameraButtonChooserDialogFragment dialogFragment = new CameraButtonChooserDialogFragment();
        dialogFragment.setResolveInfoData(resolveInfoName);
        dialogFragment.setDialogClickListener(this);
        dialogFragment.show(mActivity.getFragmentManager(), "chooserdialog");
    }

    public interface CameraButtonResultListener {
        void imageSaved(Uri uri);
    }

    /*File Providor Methods*/
    String currentPhotoPath;

    private File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent(Activity activity)
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(activity);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

}
