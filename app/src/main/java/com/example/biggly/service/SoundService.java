package com.example.biggly.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.biggly.R;
import com.example.biggly.SecondFragment;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;


public class SoundService extends Service{


    private static final String TAG = "SoundService";
    private Intent intent;
    private SecondFragment mFragmentInstance;

    public static class ACTION {

        public static final String PLAY_ACTION = "music.action.play";
        public static final String START_ACTION = "music.action.start";
        public static final String STOP_ACTION = "music.action.stop";

    }



    private MyBinder mBinder  =new  MyBinder();


    public class MyBinder extends Binder{
        // Return this instance of MyService so clients can call public methods
      public   SoundService getService() {
            return SoundService.this;
        }
    }

    /**
     * This is how the client gets the IBinder object from the service. It's retrieve by the "ServiceConnection"
     * which you'll see later.
     */
    @Override
    public IBinder onBind( Intent intent ){
        return mBinder;
    }

    public SoundService() {
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SoundService.class.getSimpleName(), "onCreate()");

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
//



        if (intent == null || intent.getAction() == null) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }

        switch (intent.getAction()) {
            case ACTION.START_ACTION:
                Log.i(TAG, "Received start Intent ");
                Intent notificationIntent = new Intent(this, SoundService.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                String channelId ="";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   channelId = createNotificationChannel("my_service", "My Background Service");
                } else {
                    // If earlier version channel ID is not used
                    // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                    channelId=  "";
                }

                NotificationCompat.Builder notificationBuilder =new  NotificationCompat.Builder(this, channelId );
                Notification notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(PRIORITY_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .build();
                startForeground(133768, notification);

              //  mFragmentInstance.callCompleteProfile();
//                destroyPlayer();
//                initPlayer();
//                play();
                break;


            case ACTION.STOP_ACTION:
                Log.i(TAG, "Received Stop Intent");
                //  destroyPlayer();
                try {

                    stopForeground(true);
                    stopSelf();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            default:
                stopForeground(true);
                stopSelf();
        }
        return START_NOT_STICKY;

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.RED);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
    }



    public void setListener(SecondFragment secondFragment){

        this.mFragmentInstance = secondFragment;
    }






}