package com.itbooth.mobility.ludodice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbooth.mobility.ludodice.adapter.DiceHistoryAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    //Player videoView;
    ImageButton refreshIV, closeIV, shareIV, settingIV, musicSettingIV, vibrationSettingIV;
    TextView lastScoreTV;
    LinearLayout scoreWrapper;
    RecyclerView diceHistoryRV;
    Vibrator vibrator;
    int delay = 3000;
    int miniDelay = 500;
    Uri uri;
    ArrayList<String> scoreList = new ArrayList<>();
    MediaPlayer.OnPreparedListener PreparedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = (VideoView)findViewById(R.id.dice_vv);
        MediaController mc = new MediaController(this);
        //mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        //videoView.setMediaController(mc);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    boolean isEnabled = Utils.getMusicSetting(VideoActivity.this);
                    if(isEnabled){
                       unMute(mp);
                    } else {
                        mute(mp);
                    }
                } catch (Exception ex) {
                    Log.e("error", ex.getMessage());
                }
            }
        });
        videoView.requestFocus();

        //videoView = findViewById(R.id.dice_vv);
        refreshIV = (ImageButton)findViewById(R.id.refreshIV);
        shareIV = (ImageButton)findViewById(R.id.shareIV);
        shareIV.setVisibility(View.GONE);
        closeIV = (ImageButton) findViewById(R.id.closeIV);
        settingIV = (ImageButton) findViewById(R.id.settingIV);
        settingIV.setVisibility(View.GONE);
        lastScoreTV = findViewById(R.id.lastScoreTV);
        scoreWrapper = findViewById(R.id.scoreWrapper);
        scoreWrapper.setVisibility(View.INVISIBLE);
        musicSettingIV = findViewById(R.id.musicSettingIV);
        vibrationSettingIV = findViewById(R.id.vibrationSettingIV);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        diceHistoryRV = (RecyclerView) findViewById(R.id.diceHistoryRV);
        diceHistoryRV.setLayoutManager(layoutManager);

        refreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDice(v);
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDice(v);
            }
        });

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        musicSettingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean isEnabled = Utils.getMusicSetting(VideoActivity.this);
                    if(isEnabled){
                        Utils.saveMusicSetting(VideoActivity.this, false);
                        musicSettingIV.setBackgroundResource(R.drawable.ic_music_off);
                    } else {
                        Utils.saveMusicSetting(VideoActivity.this, true);
                        musicSettingIV.setBackgroundResource(R.drawable.ic_music_on);
                    }
                } catch (Exception ex) {
                    Log.e("error", ex.getMessage());
                }
            }
        });

        vibrationSettingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = Utils.getVibrationSetting(VideoActivity.this);
                if(isEnabled){
                    Utils.saveVibrationSetting(VideoActivity.this, false);
                    vibrationSettingIV.setBackgroundResource(R.drawable.ic_vibration_off);
                    stopVibrate(v);
                } else {
                    Utils.saveVibrationSetting(VideoActivity.this, true);
                    vibrationSettingIV.setBackgroundResource(R.drawable.ic_vibration_on);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(miniDelay, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(miniDelay);
                    }
                }
            }
        });

        settingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VideoActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        shareIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

        // Vibrator Icon
        boolean isEnabled = Utils.getVibrationSetting(VideoActivity.this);
        if(isEnabled){
            vibrationSettingIV.setBackgroundResource(R.drawable.ic_vibration_on);
        } else {
            vibrationSettingIV.setBackgroundResource(R.drawable.ic_vibration_off);
        }

        // Music Icon
        boolean isMusicEnabled = Utils.getMusicSetting(VideoActivity.this);
        if(isMusicEnabled){
            musicSettingIV.setBackgroundResource(R.drawable.ic_music_on);
        } else {
            musicSettingIV.setBackgroundResource(R.drawable.ic_music_off);
        }

        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.role_1));
    }

    public void mute(MediaPlayer mediaPlayer) {
        this.setVolume(mediaPlayer, 0);
    }

    public void unMute(MediaPlayer mediaPlayer) {
        this.setVolume(mediaPlayer, 100);
    }

    private void setVolume(MediaPlayer mediaPlayer,int amount) {
        final int max = 100;
        final double numerator = max - amount > 0 ? Math.log(max - amount) : 0;
        final float volume = (float) (1 - (numerator / Math.log(max)));
        mediaPlayer.setVolume(volume, volume);
    }

    public void startVibrate(View v) {
        boolean isEnabled = Utils.getVibrationSetting(VideoActivity.this);
        if(isEnabled){
            //long pattern[] = { 0, 100, 200, 300, 400 };
            //vibrator.vibrate(pattern, 0);

            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(delay, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(delay);
            }
        }
    }

    public void stopVibrate(View v) {
        vibrator.cancel();
    }

    public void initializeView() {
        videoView.seekTo( 1 ); // thumbnail
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeView();
    }

    public void updateDiceCountHistory() {
        String count = Utils.getDiceCount(this);
        diceHistoryRV.setAdapter(new DiceHistoryAdapter(this, scoreList));
        diceHistoryRV.scrollToPosition(scoreList.size()-1);
    }

    public void refreshDice(View v) {
        int dot = Utils.generateRandomNumber();
        switch (dot){
            case 2:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.role_2);
                break;
            case 3:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.role_3);
                break;
            case 4:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.role_4);
                break;
            case 5:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.role_5);
                break;
            case 6:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.role_6);
                break;
            default:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.role_1);
                break;
        }

        updateScore(dot+"",v);
    }

    public void updateScore(String point, View v) {
        videoView.setVideoURI(uri);
        videoView.start();
        startVibrate(v);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(scoreList.size()>0){
                    scoreWrapper.setVisibility(View.VISIBLE);
                    lastScoreTV.setText(scoreList.get(scoreList.size()-1));

                    if(scoreList.get(scoreList.size()-1).equals("6")){
                        lastScoreTV.setTextColor(getResources().getColor(R.color.light_blue));
                    } else {
                        lastScoreTV.setTextColor(getResources().getColor(R.color.yellow));
                    }
                }
                scoreList.add(point);
                Utils.saveDiceCount(getApplicationContext(), scoreList.size()+"");
                updateDiceCountHistory();

                if(scoreList.size()==1){
                    showCustomToast("Tap to shake the dice.");
                }
                stopVibrate(v);
            }
        }, delay);
    }

    public void showCustomToast(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_launcher);
        image.setVisibility(View.GONE);
        TextView text1 = (TextView) layout.findViewById(R.id.text1);
        text1.setText(message);
        Toast firstToast = new Toast(getApplicationContext());
        //firstToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        firstToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        firstToast.setDuration(Toast.LENGTH_LONG);
        firstToast.setView(layout);
        firstToast.show();

        /*
        Toast secondToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        // getView() method returns the default view of the initialized Toast.
        View toastView = secondToast.getView();

        // TextView can be accessed from default View of the Toast.
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(25);
        toastMessage.setTextColor(getResources().getColor(R.color.yellow));

        // With setCompoundDrawablesWithIntrinsicBounds() method drawable image can be aligned under the Toast View.
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher,0,0,0);
        toastMessage.setGravity(Gravity.CENTER_VERTICAL);
        toastView.setBackgroundColor(getResources().getColor(R.color.organge));
        //toastView.setBackgroundResource(R.drawable.bg_with_circular_border);
        toastView.setMinimumWidth(650);
        secondToast.show();
        */
    }

    private void takeScreenshot() {
        Date now = new Date();
        //android.text.format.DateFormat.format("hh:mm:ss", now);
        SimpleDateFormat formatter5=new SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss");
        String currentDate = formatter5.format(now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + currentDate + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        Uri apkURI = FileProvider.getUriForFile(
                this,
                getApplicationContext()
                        .getPackageName() + ".provider", imageFile);
        intent.setDataAndType(apkURI, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

}