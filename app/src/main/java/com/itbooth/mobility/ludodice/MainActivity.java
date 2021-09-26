package com.itbooth.mobility.ludodice;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView randomTV;
    Button genBtn;
    int max = 6;
    int min = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomTV = findViewById(R.id.random_tv);
        genBtn = findViewById(R.id.gen_btn);

        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(intent);
            }
        });

        permissionCheck();
    }

    public void permissionCheck() {
        Dexter.withContext(this)
            .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(new MultiplePermissionsListener() {

        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
            if(report.isAnyPermissionPermanentlyDenied()){
                Toast.makeText(MainActivity.this, "some persmionss denied", Toast.LENGTH_SHORT).show();
                permissionCheck();
            }

            if(report.areAllPermissionsGranted()){
                Toast.makeText(MainActivity.this, "all persmionss granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "persmionss denied", Toast.LENGTH_SHORT).show();
            }
        }
        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}}).check();

    }

}