package com.hongwenli.winnieweather.music;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.music.adapter.MusicAdapter;
import com.hongwenli.winnieweather.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    SeekBar seekBar;
    ImageButton buttonPlayOrPause;
    ImageButton buttonBack;
    ImageButton buttonForward;
    ImageButton buttonNext;
    ImageButton buttonPre;
    MediaPlayer mediaPlayer = new MediaPlayer();
    TextView textView;
    SeekBar seekBarVolume;
    boolean flag = true;
    String[] array = {"1.mp3", "2.mp3", "3.mp3", "4.mp3", "5.mp3", "6.mp3"};
    int currentPlay = 0;
    MusicAdapter musicAdapter;
    ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.stop();
        mediaPlayer.reset();
        currentPlay=0;
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "Music" + File.separator + array[currentPlay];
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new MyTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            play();
        }else{
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(100);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarVolume.setMax(100);
        seekBarVolume.setProgress(50);
        buttonBack = findViewById(R.id.imageButtonBack);
        buttonForward = findViewById(R.id.imageButtonForward);
        buttonPlayOrPause = findViewById(R.id.imageButtonPlayOrPause);
        buttonPre = findViewById(R.id.imageButtonPre);
        buttonNext = findViewById(R.id.imageButtonNext);
        textView = findViewById(R.id.textView);
        mediaPlayer.setVolume(0.5f, 0.5f);
        if(mediaPlayer.isPlaying()){
            buttonPlayOrPause.setImageResource(android.R.drawable.ic_media_pause);
        }else if(!mediaPlayer.isPlaying()){
            buttonPlayOrPause.setImageResource(android.R.drawable.ic_media_play);
        }
        buttonForward.setOnClickListener(v -> {
            if(mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.seekTo((mediaPlayer.getCurrentPosition() + 10000) > mediaPlayer.getDuration()
                        ? mediaPlayer.getCurrentPosition() : (mediaPlayer.getCurrentPosition() + 10000));
            }
        });
        buttonBack.setOnClickListener(v -> {
            if(mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.seekTo((mediaPlayer.getCurrentPosition() - 10000) < 0
                        ? 0 : (mediaPlayer.getCurrentPosition() - 10000));
            }
        });
        buttonNext.setOnClickListener(v -> {
            if(currentPlay < array.length-1){
                mediaPlayer.stop();
                mediaPlayer.reset();
                currentPlay+=1;
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + "Music" + File.separator + array[currentPlay];
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonPre.setOnClickListener(v -> {
            if(currentPlay>0){
                mediaPlayer.stop();
                mediaPlayer.reset();
                currentPlay-=1;
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + "Music" + File.separator + array[currentPlay];
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null){
                    mediaPlayer.setVolume(seekBarVolume.getProgress() / 100.0f,
                            seekBarVolume.getProgress() / 100.0f);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        buttonPlayOrPause.setOnClickListener(v -> {
            if(mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                buttonPlayOrPause.setImageResource(android.R.drawable.ic_media_play);
            }else if(mediaPlayer != null && mediaPlayer.isPlaying() == false){
                mediaPlayer.start();
                buttonPlayOrPause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && mediaPlayer.isPlaying() && fromUser){
                    mediaPlayer.seekTo((int)(mediaPlayer.getDuration() * progress / 100));
                }
                if(progress==100){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    if(currentPlay!=5)currentPlay+=1;
                    else currentPlay=0;
                    try {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                File.separator + "Music" + File.separator + array[currentPlay];
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        listView = findViewById(R.id.list_view);
        List<String > musicList = Arrays.asList(array);
        musicAdapter = new MusicAdapter(this,musicList);
        listView.setAdapter(musicAdapter);

        listView.setOnItemClickListener(this);
        new MyTask().execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            play();
        }
    }

    void play(){
        try{
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "Music" + File.separator + array[currentPlay];
            Log.i("PATH", path);
//            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            currentPlay = position;
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "Music" + File.separator + array[currentPlay];
//            Log.i("PATH", path);
//            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyTask extends AsyncTask<Object, Integer, Object> {
        @Override
        protected Object doInBackground(Object... objects) {
            while(flag){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(1);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(mediaPlayer == null || mediaPlayer.isPlaying() == false) {
                ToastUtils.showShortToast(MusicActivity.this,"音乐播放器压根没开");
            }
            int duration = mediaPlayer.getDuration();
            Log.i("DUR", duration + "");
            int current = mediaPlayer.getCurrentPosition();
            Log.i("CURR", current + "");
            seekBar.setProgress(Math.round((current + 0.0f) / duration * 100));
            Log.i("PRO", Math.round(current / duration * 100) + "");
            String str = "文件名：" + array[currentPlay];
            str += "  总时长：" + (duration / 1000 / 60) + "分" + (duration / 1000 % 60) + "秒";
            str += "  当前进度：" + (current / 1000 / 60) + "分" + (current / 1000 % 60) + "秒";
            textView.setText(str);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}