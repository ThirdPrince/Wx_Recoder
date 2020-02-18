package com.example.wx_recoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.example.wx_recoder.view.AudioRecorderButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AudioRecorderButton audio_btn ;

    private RecyclerView recyclerView ;

    private RecorderAdapter recorderAdapter ;

    private List<Recorder> list ;

    private   View anim ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }
    private void  initView()
    {
        audio_btn = findViewById(R.id.audio_btn);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        recorderAdapter = new RecorderAdapter(list,this);
        recyclerView.setAdapter(recorderAdapter);
    }

    private void initEvent()
    {
        audio_btn.setOnAudioFinishRecorderListener(new AudioRecorderButton.OnAudioFinishRecorderListener() {
            @Override
            public void onFinish(float sec, String filePath) {

                Recorder recorder = new Recorder(sec,filePath);
                list.add(recorder);
                recorderAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(list.size()-1);
            }
        });
        recorderAdapter.setOnVoicePlayListener(new RecorderAdapter.OnVoicePlayListener() {
            @Override
            public void play(View view ,Recorder recorder, int position) {

                // 播放动画
                if(anim != null)
                {
                    anim.setBackgroundResource(R.drawable.adj);
                    anim = null;

                }

                 anim = view.findViewById(R.id.id_recorder_anim) ;
                anim.setBackgroundResource(R.drawable.voice_anim);
                final AnimationDrawable animationDrawable = (AnimationDrawable)anim.getBackground();
                animationDrawable.start();

                // 播放音频

                MediaPlayManager.play(recorder.filePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        animationDrawable.stop();

                        animationDrawable.stop();
                    }
                });
            }
        });
    }

}
