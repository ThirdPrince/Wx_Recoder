package com.example.wx_recoder;

import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 录音管理
 */
public class AudioManager {

    private static final String TAG = "AudioManager";

    private MediaRecorder mediaRecorder ;

    private  String mDir ;

    private String mCurrentFilePath;

    private boolean isPrepare ;


    private AudioManager(String mDir)
    {

        this.mDir = mDir ;
    }
    private static AudioManager mInstance  ;

    public interface AudioStateListener
    {
        void wellPrepare();
    }
    public AudioStateListener audioStateListener ;

    /**
     * 回调准备完毕
     * @param audioStateListener
     */
    public void setAudioStateListener(AudioStateListener audioStateListener) {
        this.audioStateListener = audioStateListener;
    }

    public static AudioManager getInstance(String mdir)
    {
        if(mInstance == null)
        {
            synchronized (AudioManager.class)
            {
                if(mInstance == null)
                {
                    mInstance = new AudioManager(mdir);
                }
            }

        }
        return mInstance ;
    }

    public void prepareAudio()
    {
     File dir = new File(mDir);
     if(!dir.exists())
     {
         dir.mkdirs();
     }
       // mCurrentFilePath = dir.getAbsolutePath();
     String fileName = generateFileName();

     File targetFile = new File(dir,fileName);
        mCurrentFilePath = targetFile.getAbsolutePath();
     mediaRecorder = new MediaRecorder();
     mediaRecorder.setOutputFile(targetFile.getAbsoluteFile());
     // 设置MediaRecorder 音频源为麦克风
     mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
     // 设置音频的格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        // 设置音频的编码amr
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isPrepare = true ;
            if(audioStateListener != null)
            {
                audioStateListener.wellPrepare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成文件的名称
     *
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString()+".amr";
    }

    public int getVoiceLevel(int maxLevel)
    {
        if(isPrepare)
        {

            if(mediaRecorder != null) {

               // int maxVoice =  mediaRecorder.getMaxAmplitude();

               // Log.e(TAG,"rate=="+mediaRecorder.getMaxAmplitude() / 3000 );
                double ratio = (double)mediaRecorder.getMaxAmplitude() /1;
                double db = 0;// 分贝
                if (ratio > 1)
                    db = 20 * Math.log10(ratio);
                Log.d(TAG,"分贝值："+db);

               // Log.e(TAG,"maxLevel=="+(maxLevel * (int)db) /90  );
                return   (int)db;

            }
        }
        return 1;

    }

    public void release()
    {
        if(mediaRecorder!= null)
        {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void cancel()
    {
        File file = new File(mCurrentFilePath);
        file.delete();
        release();
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }
}
