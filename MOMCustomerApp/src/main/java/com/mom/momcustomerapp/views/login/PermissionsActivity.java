package com.mom.momcustomerapp.views.login;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.customviews.CameraButton;
import com.mom.momcustomerapp.views.shared.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class PermissionsActivity extends BaseActivity {

    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_LOCATION = 2;
    public static final int REQUEST_CONTACTS = 3;
    /*public static final int REQUEST_READ_SMS = 4;
    public static final int REQUEST_RECEIVE_SMS = 5;*/
    public static final int REQUEST_CALL_PHONE = 6;
    public static final int REQUEST_READ_PHONE_STATE = 7;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 8;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_permissions);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionsGranted();
        } else {
            int permissionWrite = checkSelfPermission(WRITE_EXTERNAL_STORAGE);
            int permissionCamera = checkSelfPermission(CAMERA);
            int permissionLocation = checkSelfPermission(ACCESS_FINE_LOCATION);
            int permissionContacts = checkSelfPermission(READ_CONTACTS);
//            int permissionReadSMS = checkSelfPermission(READ_SMS);
            /*int permissionReceiveSMS = checkSelfPermission(RECEIVE_SMS);*/
            int permissionCall = checkSelfPermission(CALL_PHONE);
            int permissionPhoneState = checkSelfPermission(READ_PHONE_STATE);
            int permissionRead = checkSelfPermission(READ_EXTERNAL_STORAGE);

            if (permissionWrite == PackageManager.PERMISSION_GRANTED
                    && permissionCamera == PackageManager.PERMISSION_GRANTED
                    && permissionLocation == PackageManager.PERMISSION_GRANTED
                    && permissionContacts == PackageManager.PERMISSION_GRANTED
                    /*&& permissionReadSMS == PackageManager.PERMISSION_GRANTED*/
                    && permissionCall == PackageManager.PERMISSION_GRANTED
                    && permissionPhoneState == PackageManager.PERMISSION_GRANTED
                    && permissionRead == PackageManager.PERMISSION_GRANTED
                /*&& permissionReceiveSMS == PackageManager.PERMISSION_GRANTED*/) {

                permissionsGranted();
            } else {
                List<String> listPermissionsNeeded = new ArrayList<>();
                if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE);
                }
                if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(CAMERA);
                }
                if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
                }
                if (permissionContacts != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(READ_CONTACTS);
                }
                /*if (permissionReadSMS != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(READ_SMS);
                }
                if (permissionReceiveSMS != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(RECEIVE_SMS);
                }*/
                if (permissionCall != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(CALL_PHONE);
                }
                if (permissionPhoneState != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(READ_PHONE_STATE);
                }
                if (permissionRead!= PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
                }
                if (!listPermissionsNeeded.isEmpty()) {
                    requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CameraButton.REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
            }
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_WRITE_EXTERNAL_STORAGE");
            }
        }

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_CAMERA");
            }
        }

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_CAMERA");
            }
        }

        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_CAMERA");
            }
        }

        /*if (requestCode == REQUEST_READ_SMS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_CAMERA");
            }
        }

        if (requestCode == REQUEST_RECEIVE_SMS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_CAMERA");
            }
        }*/

        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_CALL_PHONE");
            }
        }

        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d("Nish", "permissionsGranted : REQUEST_READ_PHONE_STATE");
            }
        }

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            //Log.d("Nish", "permissionsGranted : ALL");
        }

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            //Log.d("Nish", "permissionsGranted : ALL");
        }

        permissionsGranted();
    }

    private void permissionsGranted() {
        finish();
    }

}
