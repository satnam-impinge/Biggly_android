package com.example.biggly.utility;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

public class Validator {


    public static  boolean isValidateEmail(Context context, TextInputLayout textInputLayout, EditText editText, String message) {
        textInputLayout.setErrorEnabled(true);

        if (TextUtils.isEmpty(editText.getText().toString()) ) {

            textInputLayout.setError(message);

            return false;

        } else if (!editText.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {

            textInputLayout.setError(message);
            return false;

        }
        textInputLayout.setError("");
        textInputLayout.setErrorEnabled(false);

        return true;
    }
    public static  boolean isValidateEmail(Context context, EditText editText, String message) {

        if (TextUtils.isEmpty(editText.getText().toString()) ) {

            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

            return false;

        } else if (!editText.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {

            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    public static  boolean isValidateText(Context context, EditText editText, String message, int validateLength) {

        if (TextUtils.isEmpty(editText.getText().toString()) ) {

            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

            return false;

        } else if (editText.getText().toString().length()<validateLength) {

            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            return false;

        }

//        textInputLayout.setError("");
//        textInputLayout.setErrorEnabled(false);
        return true;
    }



    public static  boolean isValidImage(Context context,  MultipartBody.Part body, String message) {

        if(body == null){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

            return false;
        }

//        textInputLayout.setError("");
//        textInputLayout.setErrorEnabled(false);
        return true;
    }

    public static  boolean isValidateText(Context context,TextInputLayout textInputLayout, EditText editText, String message, int validateLength) {

        if (TextUtils.isEmpty(editText.getText().toString()) ) {

            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

            return false;

        } else if (editText.getText().toString().length()<validateLength) {

            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            return false;

        }

//        textInputLayout.setError("");
//        textInputLayout.setErrorEnabled(false);
        return true;
    }
    public static boolean isValidatepassword(Context context, TextInputLayout textInputLayout1,TextInputLayout textInputLayout2,EditText editText1, EditText editText2, String message,int validateLength) {
        textInputLayout1.setErrorEnabled(true);
        textInputLayout2.setErrorEnabled(true);

        if (!isValidateText(context,textInputLayout1,editText1,message,validateLength) ) {

            return false;

        }else if (!isValidateText(context,textInputLayout2,editText2,message,validateLength) ) {


            textInputLayout2.setError(message);

            return false;

        }else if (!editText1.getText().toString().trim().matches(editText2.getText().toString().trim())) {

            textInputLayout1.setError(message);
            return false;

        }
        textInputLayout1.setErrorEnabled(false);
        textInputLayout1.setError("");
        textInputLayout2.setErrorEnabled(false);
        textInputLayout2.setError("");

        return true;
    }
}
