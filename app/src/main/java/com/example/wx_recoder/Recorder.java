package com.example.wx_recoder;

public class Recorder {

    float time ;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    String filePath ;

    public Recorder(float time,String filePath)
    {
          this.filePath = filePath;
          this.time = time ;
    }

}
