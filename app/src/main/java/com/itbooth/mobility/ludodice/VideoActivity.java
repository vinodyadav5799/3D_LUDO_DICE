package com.itbooth.mobility.ludodice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    Uri uri;
    ImageButton refreshIV, closeIV, shareIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoView = (VideoView)findViewById(R.id.dice_vv);
        refreshIV = (ImageButton)findViewById(R.id.refreshIV);
        shareIV = (ImageButton)findViewById(R.id.shareIV);
        closeIV = (ImageButton) findViewById(R.id.closeIV);
        refreshDice();

        refreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDice();
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDice();
            }
        });

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });
    }

    public void refreshDice(){
        int dot = Utils.generateRandomNumber();
        switch (dot){
            case 1:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_1);
                break;
            case 2:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_2);
                break;
            case 3:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_3);
                break;
            case 4:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_4);
                break;
            case 5:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_5);
                break;
            case 6:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_6);
                break;
            default:
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_1);
                break;
        }

        /*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);
        */
        uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_1);
        videoView.setVideoURI(uri);
        videoView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoActivity.this, "Tap or shake to refresh the dice.", Toast.LENGTH_SHORT).show();
            }
        }, 4000);
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