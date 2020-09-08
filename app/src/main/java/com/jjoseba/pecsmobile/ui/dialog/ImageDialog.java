package com.jjoseba.pecsmobile.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;
import com.jjoseba.pecsmobile.fragment.NewCardFragment;
import com.jjoseba.pecsmobile.util.FileUtils;

public class ImageDialog extends Dialog{

    private Fragment parentFragment;
    private boolean textForImage = false;

    public ImageDialog(Context context){
        super(context);
    }

    public ImageDialog(Fragment f){
        super(f.getActivity());
        parentFragment = f;
    }

    @Override
    public void show(){

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            this.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        }
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setContentView(R.layout.image_dialog);

        this.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog.this.dismiss();
            }
        });

        View galleryBtn =  this.findViewById(R.id.gallery_button);
        View cameraBtn = this.findViewById(R.id.camera_button);
        View textBtn = this.findViewById(R.id.text_button);

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImagePicker();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });

        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textForImage = true;
                ImageDialog.this.dismiss();
            }
        });

        Animation appearButton1 = AnimationUtils.loadAnimation(parentFragment.getActivity(), R.anim.button_appear);
        Animation appearButton2 = AnimationUtils.loadAnimation(parentFragment.getActivity(), R.anim.button_appear);
        Animation appearButton3 = AnimationUtils.loadAnimation(parentFragment.getActivity(), R.anim.button_appear);
        appearButton2.setStartOffset(150);
        appearButton3.setStartOffset(300);
        galleryBtn.startAnimation(appearButton1);
        cameraBtn.startAnimation(appearButton2);
        textBtn.startAnimation(appearButton3);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        boolean cameraEnabled = prefs.getBoolean(PrefsActivity.CREATE_CAMERA, true);
        boolean textcardEnabled = prefs.getBoolean(PrefsActivity.CREATE_TEXTCARD, true);
        if (!cameraEnabled && !textcardEnabled){
            //if we only have the image option, launch it directly
            super.show();
            startImagePicker();
        }
        else{
            cameraBtn.setVisibility(cameraEnabled ? View.VISIBLE : View.GONE);
            textBtn.setVisibility(textcardEnabled ? View.VISIBLE : View.GONE);
            super.show();
        }

    }

    private void startImagePicker(){

        boolean success = startSamsungPicker();
        if (success){
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(parentFragment.getActivity().getPackageManager()) != null){
            parentFragment.getActivity().startActivityForResult(intent, NewCardFragment.REQUEST_IMAGE);
        }
        else {
            //TODO: should log this...
            Toast.makeText(getContext(), R.string.unableResolveImageRequest, Toast.LENGTH_LONG).show();
            success = startDocumentPicker();

            if (success){
                return;
            }
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona imagen");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
            parentFragment.getActivity().startActivityForResult(chooserIntent, NewCardFragment.REQUEST_IMAGE);
        }

    }

    private boolean startSamsungPicker(){
        Log.d("Intent", "Trying to launch Samsung file picker...");
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        intent.putExtra("CONTENT_TYPE", "image/*");

        if (parentFragment.getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            parentFragment.getActivity().startActivityForResult(intent, NewCardFragment.REQUEST_IMAGE);
            return true;
        }
        else{
            Log.d("Intent",  "No activity found.");
            return false;
        }

    }

    private boolean startDocumentPicker(){
        Log.d("Intent", "Trying to launch brute file picker...");
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        if (parentFragment.getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            parentFragment.getActivity().startActivityForResult(intent, NewCardFragment.REQUEST_IMAGE);
            return true;
        }
        else{
            Log.d("Intent", "No activity found. ");
            return false;
        }

    }

    private void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(parentFragment.getActivity().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getTempImageURI(this.getContext()));
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            parentFragment.startActivityForResult(takePictureIntent, NewCardFragment.REQUEST_CAMERA);
            ImageDialog.this.dismiss();
        }
        else {
            //TODO: should log this...
            Toast.makeText(getContext(), R.string.unableResolveCameraRequest, Toast.LENGTH_LONG).show();
        }
    }

    public boolean isTextForImage(){ return textForImage; }
}
