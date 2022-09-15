package com.example.biggly;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private ArrayList<String> permissionsToRequest = new ArrayList<>();
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();

    onPermissionResult onPermissionResult;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void fullWindow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }



    public void askPermission(onPermissionResult onPermissionResult){

        this.onPermissionResult = onPermissionResult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            else {
                if(onPermissionResult != null){
                    onPermissionResult.onPermissionGranted();
                }
            }
        }else {
            if(onPermissionResult != null){
                onPermissionResult.onPermissionGranted();
            }
        }
    }



    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {

                case ALL_PERMISSIONS_RESULT:
                    for (String perms : permissionsToRequest) {
                        if (hasPermission(perms)) {

                        } else {

                            permissionsRejected.add(perms);
                        }
                    }

                    if (permissionsRejected.size() > 0) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                                showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                    //Log.d("API123", "permisionrejected " + permissionsRejected.size());
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                        if (Environment.isExternalStorageManager()){
                                                            if (onPermissionResult != null) {
                                                                onPermissionResult.onPermissionGranted();
                                                            }
                                                            // If you don't have access, launch a new activity to show the user the system's dialog
                                                            // to allow access to the external storage
                                                        }else{
                                                            Intent intent = new Intent();
                                                            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                            intent.setData(uri);
                                                            startActivity(intent);
                                                        }
                                                    }else {
                                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                    }
                                                }
                                            }
                                        });
                                return;
                            } else {
                                showMessageOKCancel("These permissions are mandatory for the application. Please allow access from settings.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                    if (Environment.isExternalStorageManager()){
                                                        if (onPermissionResult != null) {
                                                            onPermissionResult.onPermissionGranted();
                                                        }
    // If you don't have access, launch a new activity to show the user the system's dialog
    // to allow access to the external storage
                                                    }else{
                                                        Intent intent = new Intent();
                                                        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                        intent.setData(uri);
                                                        startActivity(intent);
                                                    }
                                                }else {
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                        Uri.parse("package:" + getPackageName()));
                                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);

                                                }
                                            }
                                        });
                            }
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager()){
                                    if (onPermissionResult != null) {
                                        onPermissionResult.onPermissionGranted();
                                    }
                                    // If you don't have access, launch a new activity to show the user the system's dialog
                                    // to allow access to the external storage
                                }else{
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            }
                        }

                    } else {
                        if (onPermissionResult != null) {
                            onPermissionResult.onPermissionGranted();
                        }
                    }

                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<String> getPermissionsToRequest() {
        return permissionsToRequest;
    }

    public void setPermissionsToRequest(ArrayList<String> permissionsToRequest) {


        this.permissionsToRequest = findUnAskedPermissions(permissionsToRequest);
    }

    public interface  onPermissionResult{
        public void onPermissionGranted();
        public void onPermissiondenied();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
