package com.example.wx_recoder;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

/**
 * 管理 录音dialog
 */
public class DialogManager {

    private static final String TAG = "DialogManager";

    private AlertDialog mDialog ;

    private ImageView mIcon ;

    private ImageView  mVoice;

    private TextView mTips ;

    private Context mContext ;

    public DialogManager(Context context)
    {
        this.mContext = context ;
    }

    public void showRecordingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.Translucent_NoTitle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_recorder, null);
        mIcon = view.findViewById(R.id.recorder_dialog_icon);
        mVoice = view.findViewById(R.id.recorder_dialog_volume);
        mTips = view.findViewById(R.id.dialog_tips);
        builder.setView(view);
        builder.setCancelable(true);
        mDialog = builder.create();
        mDialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.show();
        Log.e(TAG,"showRecordingDialog");

    }

    public void recording()
    {
        if(mDialog != null && mDialog.isShowing())
        {


        }
    }
    public void wantToCancel()
    {
        if(mDialog != null && mDialog.isShowing())
        {
            mIcon.setImageResource(R.drawable.cancel);
            //mDialog.dismiss();
            mVoice.setVisibility(View.GONE);
          //  mDialog.dismiss();
        }
    }
    public void dismissDialog()
    {
        if(mDialog != null && mDialog.isShowing())
        {
            mDialog.dismiss();
            mDialog = null;

        }
    }

    public void tooShort()
    {
        if(mDialog != null && mDialog.isShowing())
        {
            mVoice.setVisibility(View.GONE);
            mIcon.setImageResource(R.drawable.voice_to_short);
            mTips.setText(R.string.str_recorder_too_short);
            Log.e(TAG,"tooShort");
        }
    }

    public void updateVoiceLevel(int i) {
        //Log.e(TAG,"updateVoiceLevel=="+i);
        //mIcon.setImageResource(R.drawable.volume_img_bg);
        if(mDialog != null && mDialog.isShowing()) {
            mVoice.setImageLevel(i);
        }
    }
}
