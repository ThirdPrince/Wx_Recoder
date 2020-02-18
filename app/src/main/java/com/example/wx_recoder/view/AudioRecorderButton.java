package com.example.wx_recoder.view;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.wx_recoder.AudioManager;
import com.example.wx_recoder.DialogManager;
import com.example.wx_recoder.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

/**
 * 自定义 btn  录音
 */
public class AudioRecorderButton extends AppCompatButton implements AudioManager.AudioStateListener {

    private static final String TAG = "AudioRecorderButton";

    private static final int DISTANCE_Y_CANCEL = 50;

    private static final int STATE_NORMAL = 1;

    private static final int STATE_RECORDING = 2;

    private static final int STATE_CANCEL = 3;

    private boolean isRecord;
    private int mCurrentState = STATE_NORMAL ;

    private DialogManager dialogManager ;

    private AudioManager audioManager ;

    private float mTime ;
    // 是否触发longClick
    private boolean mReady ;


    private static final int MSG_AUDIO_PREPARE = 0X110;

    private static final int MSG_VOICE_CHANGED = 0X111;

    private static final int MSG_DIALOG_DISMISS = 0X112;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case MSG_AUDIO_PREPARE:
                    dialogManager.showRecordingDialog();
                    isRecord = true ;

                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:

                    dialogManager.updateVoiceLevel(audioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DISMISS:
                    dialogManager.dismissDialog();
                    break;
            }
        }
    };

    /**
     * 获得音量大小的线程
     */
    Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {

            while(isRecord)
            {
                try {
                    Thread.sleep(100);
                    mTime +=0.1f;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(MSG_VOICE_CHANGED);
            }
        }
    };


    public AudioRecorderButton(Context context) {
        this(context,null);
        Log.e(TAG,"   this(context,null);");
    }

    public AudioRecorderButton(final Context context, AttributeSet attrs) {
        this(context, attrs,0);
        Log.e(TAG,"   this(context, attrs,0);");

    }

    public AudioRecorderButton(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Log.e(TAG," super(context, attrs, defStyleAttr);");
        String audioPath = context.getExternalFilesDir("recorder_audios").getAbsolutePath();
        audioManager = AudioManager.getInstance(audioPath);
        audioManager.setAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mReady = true ;
                dialogManager = new DialogManager(context);
                audioManager.prepareAudio();
                return true;
            }
        });
    }
    @Override
    public void wellPrepare() {

        handler.sendEmptyMessage(MSG_AUDIO_PREPARE);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int)event.getY();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                isRecord = true ;
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:


                if(isRecord)
                {
                    if(wantToCancel(x,y))
                    {
                        changeState(STATE_CANCEL);
                    }else
                    {
                        changeState(STATE_RECORDING);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                if(!mReady)
                {
                    reset();
                    return  super.onTouchEvent(event);
                }
                if(!isRecord)
                {
                    dialogManager.tooShort();
                }
                if(mCurrentState == STATE_RECORDING)
                {
                    //release
                    // callbackToAct
                   // changeState(STATE_RECORDING);
                    onAudioFinishRecorderListener.onFinish(mTime,audioManager.getCurrentFilePath());

                }else if(mCurrentState == STATE_CANCEL)
                {
                    //changeState(STATE_CANCEL);
                    audioManager.cancel();
                }
                reset();
                break;
        }
      return  super.onTouchEvent(event);
    }

    private boolean wantToCancel(int x, int y) {
        Log.e(TAG,"x="+x+"::y="+y + "getWidth= "+getWidth()+"::getHeight="+getHeight());
        if(x<0 || x >getWidth())
        {
            return  true ;
        }
        if(y < -DISTANCE_Y_CANCEL || y >getHeight()+DISTANCE_Y_CANCEL)
        {

            return true ;
        }
        return false;
    }

    private void changeState(int state) {

        if(mCurrentState != state)
        {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
                   setBackgroundResource(R.drawable.btn_recorder_normal);
                   setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_recording);
                    if(isRecord)
                    {
                        // Dialog recording()
                    }
                    break;
                case STATE_CANCEL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_cancel);
                    dialogManager.wantToCancel();
                    break;
            }
        }
    }

    /**
     * reset
     */
    private void reset()
    {

        isRecord = false ;
        mReady = false ;
        mTime = 0.0f;
        changeState(STATE_NORMAL);
        if(audioManager != null) {
            audioManager.release();
        }
        dialogManager.dismissDialog();
    }



    /**
     * 录音完成的回调
     */
    public interface OnAudioFinishRecorderListener
    {
        void onFinish(float sec,String filePath);
    }

    public OnAudioFinishRecorderListener onAudioFinishRecorderListener;

    public void setOnAudioFinishRecorderListener(OnAudioFinishRecorderListener onAudioFinishRecorderListener) {
        this.onAudioFinishRecorderListener = onAudioFinishRecorderListener;
    }
}
