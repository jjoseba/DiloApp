package com.jjoseba.pecsmobile.ui.dialog;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;
import com.jjoseba.pecsmobile.fragment.NewCardFragment;
import com.jjoseba.pecsmobile.util.FileUtils;

public class ImageDialog extends Dialog{

    private Fragment parentFragment;
    private boolean cardChanged = false;
    private boolean textForImage = false;

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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(parentFragment.getActivity().getPackageManager()) != null){
            parentFragment.startActivityForResult(intent, NewCardFragment.REQUEST_IMAGE);
            ImageDialog.this.dismiss();
        }
        else {
            //TODO: should log this...
        }
    }

    private void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(parentFragment.getActivity().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getTempImageURI());
            parentFragment.startActivityForResult(takePictureIntent, NewCardFragment.REQUEST_CAMERA);
            ImageDialog.this.dismiss();
        }
        else {
            //TODO: should log this...
        }
    }

    public boolean hasDataChanged(){
        return cardChanged;
    }
    public boolean isTextForImage(){ return textForImage; }
}
