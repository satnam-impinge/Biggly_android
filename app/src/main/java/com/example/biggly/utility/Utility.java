package com.example.biggly.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.biggly.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import retrofit2.Response;

public class Utility {
    private static Utility utility;
    private static Context ctx;
    private ProgressDialog mProgressDialog;

    public static Utility getInstance(Context context){

        if(utility == null){
            utility = new Utility();

        }

        ctx = context;
        return utility;

    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        Toast.makeText(context,context.getResources().getString(R.string.please_check_internet_connection),Toast.LENGTH_SHORT).show();
        return false;
    }

    public  void showLoading(String message) {
        mProgressDialog = new ProgressDialog(ctx);
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public  void hideLoading() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void fullWindow(Activity context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = context.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            context.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }

    public static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        //return useWhiteIcon ? R.mipmap.ic_stat_call_white : R.mipmap.ic_launcher;
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }


    public static class ErrorResponseHandler {

        public static String parseError(Response<?> response){
            String errorMsg = null;
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                errorMsg = jObjError.getString("message");
                return errorMsg ;
            } catch (Exception e) {
            }
            return errorMsg;
        }
    }



}
