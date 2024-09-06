package com.example.xo_game;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {

    MediaPlayer mp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction()=="START"){
            if(mp==null) mp=MediaPlayer.create(this,R.raw.login_or_register);
            if(!mp.isPlaying()){ //not playing in that time
                mp.start();
                mp.setLooping(true);
            }

        }else if(intent.getAction()=="STOP"){
            mp.stop();
            mp.release();
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
