package com.itbooth.mobility.ludodice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    Uri uri;
    ImageButton refreshIV, closeIV, shareIV;
    TextView lastScoreTV, countTV;
    LinearLayout scoreWrapper;
    ArrayList<String> scoreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoView = (VideoView)findViewById(R.id.dice_vv);
        refreshIV = (ImageButton)findViewById(R.id.refreshIV);
        shareIV = (ImageButton)findViewById(R.id.shareIV);
        closeIV = (ImageButton) findViewById(R.id.closeIV);
        lastScoreTV = findViewById(R.id.lastScoreTV);
        countTV = findViewById(R.id.countTV);
        scoreWrapper = findViewById(R.id.scoreWrapper);
        scoreWrapper.setVisibility(View.INVISIBLE);

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

        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.dice_1));
        videoView.seekTo( 1 ); // thumbnail
        updateDiceCountHistory();
    }

    public void updateDiceCountHistory() {
        String count = Utils.getDiceCount(this);
        if(count.equals("")){
            countTV.setText("0");
        } else {
            countTV.setText(count);
        }
    }

    public void refreshDice() {
        int dot = Utils.generateRandomNumber();
        switch (dot){
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

        updateScore(dot+"");
    }

    public void updateScore(String point) {
        videoView.setVideoURI(uri);
        videoView.start();

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
            }
        }, 4000);
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