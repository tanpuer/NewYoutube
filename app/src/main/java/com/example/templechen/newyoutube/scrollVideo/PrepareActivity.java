package com.example.templechen.newyoutube.scrollVideo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.templechen.newyoutube.R;
import com.example.templechen.newyoutube.grafika.RecordFBOActivity;

public class PrepareActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CAPTURE_PERM = 1234;

    private Button goToPlayVideoBtn;
    private Button goToRecordedVideosBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);
        goToPlayVideoBtn = findViewById(R.id.go_to_play_btn);
        goToRecordedVideosBtn = findViewById(R.id.go_to_gallery_btn);
        goToPlayVideoBtn.setOnClickListener(this);
        goToRecordedVideosBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestWritePermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_PERM: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "need write permission to record!", Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                break;
        }
    }

    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAPTURE_PERM);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_to_play_btn: {
//                Intent intent = new Intent(this, ScrollVideoActivity.class);
                Intent intent = new Intent(this, RecordFBOActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.go_to_gallery_btn : {
                Intent intent = new Intent(this, VideoGalleryActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}
