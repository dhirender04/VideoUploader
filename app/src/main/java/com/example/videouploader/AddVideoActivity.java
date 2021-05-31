package com.example.videouploader;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddVideoActivity extends AppCompatActivity {

    //Action bar
    private ActionBar actionBar;
    //UI Views
    private EditText titleEt;
    private VideoView videoView;
    private Button uploadVideoButton;
    private FloatingActionButton picVideoFloat;
    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;


    private String[] cameraPermission;
    private Uri videoUri ; //uri of picked video
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        //init ActionBar
        actionBar = getSupportActionBar();
        //title
        actionBar.setTitle("Add New Video");
        //add back button
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //init UI
        titleEt = findViewById(R.id.titleEt);
        videoView = findViewById(R.id.videoView);
        uploadVideoButton = findViewById(R.id.uploadVideoButton);
        picVideoFloat = findViewById(R.id.picVideoFloat);

        //Camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


    }



    //Handle Click Upload Video
    public void uploadVideoButton(View view){
        videoPickDialog();

    }
    //Handle Click Pick Video from camera/ gallery
    public void picVideoFloat(View view){
        videoPickDialog();
    }

    private void videoPickDialog() {
        //options to display dialog
        String[] options = {"Camera", "Gallery"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            //Camera Click
                            if (!checkCameraPermission()){
                                //camera permission not allow , request it
                                requestCameraPermission();
                            }else {
                                //permission Already allow , take picture
                                videoPickCamera();
                            }
                        }else if (i == 1){
                            //Gallery Click
                            videoPickGallery();
                        }
                    }
                });

    }
    private void requestCameraPermission(){
        //Request Camera Permission
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);

    }
    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
   }

   private void videoPickGallery(){
        //pic video from gallery
       Intent intent = new Intent();
       intent.setType("video/*");
       intent.setAction(intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Selected Videos"), VIDEO_PICK_GALLERY_CODE);
   }

   private void videoPickCamera(){
       //pic video from Camera - intent
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       startActivityForResult(intent, VIDEO_PICK_GALLERY_CODE);
   }
   private void setVideoToVideoView(){
       MediaController mediaController = new MediaController(this);
       mediaController.setAnchorView(videoView);

       //set controller to video view
       videoView.setMediaController(mediaController);
       //set video Url
       videoView.requestFocus();
       videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
           @Override
           public void onPrepared(MediaPlayer mediaPlayer) {
               videoView.pause();
           }
       });
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    //check permission allow or not
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        //both permission is allow
                        videoPickCamera();
                    }else {
                        //both or one of this denied
                        Toast.makeText(this, "Camera and Storage permission are required", Toast.LENGTH_SHORT).show();
                    }

                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //called after picking video from camera/ gallery
        if (resultCode == RESULT_OK){
            if (requestCode == VIDEO_PICK_GALLERY_CODE){
                videoUri = data.getData();
                //show picker video in Video View
                setVideoToVideoView();
            }else if (requestCode == VIDEO_PICK_CAMERA_CODE){
                videoUri = data.getData();
                //show picker video in Video View
                setVideoToVideoView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();// Go to previous activity on cliking back button on action bar
        return super.onSupportNavigateUp();

    }

//    //intent just trying 
//    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
//            , new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    startActivityForResult(intent, VIDEO_PICK_GALLERY_CODE);
//                    Intent intent = result.getData();
//                }
//            });
}