package com.example.wx_recoder;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecorderAdapter extends RecyclerView.Adapter<RecorderAdapter.RecorderViewHolder> {

    private List<Recorder> recorderList ;

    private int minWidth = 0;
    private int maxWidth = 0;
    interface OnVoicePlayListener
    {
        void play(View view ,Recorder recorder ,int position);
    }

    public OnVoicePlayListener onVoicePlayListener ;

    public void setOnVoicePlayListener(OnVoicePlayListener onVoicePlayListener) {
        this.onVoicePlayListener = onVoicePlayListener;
    }

    public RecorderAdapter(List<Recorder> list, Activity context)
    {
        recorderList = list ;
        WindowManager windowManager = context.getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        minWidth = (int)(dm.widthPixels*0.15f);
        maxWidth = (int)(dm.widthPixels*0.7f);
    }

    @NonNull
    @Override
    public RecorderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recorder_item, parent, false);
        RecorderViewHolder recorderViewHolder = new RecorderViewHolder(v);
        return recorderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecorderViewHolder holder, int position) {
        Recorder recorder = recorderList.get(position);
        holder.id_recorder_time.setText(Math.round(recorder.time)+"\"");
        ViewGroup.LayoutParams layoutParams = holder.ic_recorder_length.getLayoutParams();
        layoutParams.width = (int)(minWidth + (maxWidth /60f *recorder.time));
        holder.ic_recorder_length.setLayoutParams(layoutParams);

    }

    @Override
    public int getItemCount() {
        return recorderList.size();
    }

    class RecorderViewHolder extends RecyclerView.ViewHolder
    {

        private TextView id_recorder_time ;
        private FrameLayout ic_recorder_length ;
        public RecorderViewHolder(@NonNull final View itemView) {
            super(itemView);
            id_recorder_time = itemView.findViewById(R.id.id_recorder_time);
            ic_recorder_length = itemView.findViewById(R.id.ic_recorder_length);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(onVoicePlayListener !=null)
                    {
                        int pos = getAdapterPosition();
                        onVoicePlayListener.play(view,recorderList.get(pos),pos);
                    }
                }
            });
        }
    }
}
