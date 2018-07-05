package com.nomadapp.splash.ui.activity.standard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nomadapp.splash.R;
import com.nomadapp.splash.utils.sysmsgs.loadingdialog.BoxedLoadingDialog;
import com.nomadapp.splash.utils.sysmsgs.toastmessages.ToastMessages;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ContactUsActivity extends AppCompatActivity {

    private TextView mContactUsUsername, mContactUsEmail, mMessageConEdit;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private String currentUsername, currentEmail;

    private Bitmap socialBitmapGal;
    private TextView mPhotoUploadCUTextView;

    private ToastMessages toastMessages = new ToastMessages();
    private BoxedLoadingDialog boxedLoadingDialog = new BoxedLoadingDialog(ContactUsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //Navigate back to parent activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //--------------------------------

        mContactUsUsername = findViewById(R.id.contactUsUsername);
        mContactUsEmail = findViewById(R.id.contactUsEmail);
        mMessageConEdit = findViewById(R.id.messageConEdit);
        mPhotoUploadCUTextView = findViewById(R.id.photoUploadCUTextView);

        getUserData();

    }

    public void getUserData(){
        if (currentUser != null){
            currentUsername = currentUser.getUsername();
            currentEmail = currentUser.getEmail();
            mContactUsUsername.setText(currentUsername);
            mContactUsEmail.setText(currentEmail);
        }
    }

    public void sendMessage(View view){
        if (currentUser != null){
            if (!mMessageConEdit.getText().toString().isEmpty()){
                boxedLoadingDialog.showLoadingDialog();
                String lockedMessage = mMessageConEdit.getText().toString();
                ParseObject message = new ParseObject("Messages");
                message.put("username", currentUsername);
                message.put("userEmail", currentEmail);
                message.put("message", lockedMessage);
                if (!mPhotoUploadCUTextView.getText().toString().isEmpty()){
                    message.put("file", compressedMessageFile());
                }
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            toastMessages.productionMessage(getApplicationContext(),
                                    getResources().getString(R.string.act_contactUs_messageSend)
                                    ,1);
                            finish();
                        }else{
                            toastMessages.productionMessage(getApplicationContext(),
                                    getResources().getString(R.string.act_contactUs_messageNotSend)
                                    ,1);
                            boxedLoadingDialog.hideLoadingDialog();
                        }
                    }
                });
            }else{
                toastMessages.productionMessage(getApplicationContext(),
                        getResources().getString(R.string.act_contactUs_pleaseWriteAMsg)
                        ,1);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void photoUpload(View v){
        isStoragePermissionGranted();
        if (isStoragePermissionGranted()){
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                Uri targetUriID = data.getData();
                if (targetUriID != null) {
                    String onlyFileNameID = targetUriID.toString().substring(targetUriID.getPath().lastIndexOf(File.separator) + 1);
                    String onlyFileNameID2 = onlyFileNameID.substring(onlyFileNameID.lastIndexOf("/") + 1);
                    try {
                        socialBitmapGal = MediaStore.Images.Media.getBitmap(this.getContentResolver(), targetUriID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("photoUpID", targetUriID.toString());
                    Log.i("photoUp name onlyID", onlyFileNameID);
                    Log.i("uriNamephotoID", onlyFileNameID2);
                    String filePrefix = "message_file_" + onlyFileNameID2;
                    mPhotoUploadCUTextView.setText(filePrefix);
                }
            }
        }
    }

    public ParseFile compressedMessageFile(){
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        socialBitmapGal.compress(Bitmap.CompressFormat.JPEG, 20, stream1);
        byte[] bytes1 = stream1.toByteArray();//READY//
        return new ParseFile("messageFile.png", bytes1);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission. READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("permission","Permission is granted, press again");
                return true;
            } else {

                Log.i("permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest
                        .permission. READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.i("permission","Permission is granted, press again");
            return true;
        }
    }
}
