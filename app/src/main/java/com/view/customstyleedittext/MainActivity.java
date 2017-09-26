package com.view.customstyleedittext;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imgBack,imgFlash;
    private Camera m_Camera = Camera.open();
    private Camera.Parameters mParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgFlash = (ImageView) findViewById(R.id.img_flash);
        imgFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
                    Toast.makeText(MainActivity.this, "你的手机没有闪光灯!", Toast.LENGTH_LONG).show();

                if (mParameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    m_Camera.setParameters(mParameters);
                    imgFlash.setImageResource(R.drawable.ic_flash_open);
                } else if (mParameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    m_Camera.setParameters(mParameters);
                    imgFlash.setImageResource(R.drawable.ic_flash_opened);
                }
            }
        });
        mParameters = m_Camera.getParameters();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_Camera.release();
    }
}
