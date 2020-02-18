package com.example.wx_recoder;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;

class MediaPlayManager {

   private static MediaPlayer mediaPlayer ;

    private static boolean isPause ;

    public static  void play(String path , MediaPlayer.OnCompletionListener onCompletionListener)
    {
        if(mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();

        }else
        {
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void pause()
    {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            isPause = true ;
        }
    }

    public void resume()
    {
        if(mediaPlayer != null && isPause)
        {
            mediaPlayer.start();
            isPause = false ;
        }
    }

    public void release()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
