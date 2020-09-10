package com.jjoseba.pecsmobile.ui.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageDialog extends FABAnimatedDialog{

    private Context ctx;

    public static String IMAGE_TEXT = "text";
    public static String IMAGE_PICKER = "picker";
    public static String IMAGE_CAMERA = "camera";

    private String result;

    public ImageDialog(Context context){
        super(context);
        ctx = context;
    }

    @Override
    public void show(){
        setContentView(R.layout.dialog_image);

        FloatingActionButton galleryBtn =  this.findViewById(R.id.gallery_button);
        FloatingActionButton cameraBtn = this.findViewById(R.id.camera_button);
        FloatingActionButton textBtn = this.findViewById(R.id.text_button);
        animatableButtons = new ArrayList<>(Arrays.asList(galleryBtn, cameraBtn, textBtn));

        galleryBtn.setOnClickListener(v -> {
            result = IMAGE_PICKER;
            ImageDialog.this.dismiss();
        });

        cameraBtn.setOnClickListener(v -> {
            result = IMAGE_CAMERA;
            ImageDialog.this.dismiss();
        });

        textBtn.setOnClickListener(v -> {
            result = IMAGE_TEXT;
            ImageDialog.this.dismiss();
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        boolean cameraEnabled = prefs.getBoolean(PrefsActivity.CREATE_CAMERA, true);
        boolean textcardEnabled = prefs.getBoolean(PrefsActivity.CREATE_TEXTCARD, true);
        if (!cameraEnabled && !textcardEnabled){
            //if we only have the image option, launch it directly
            super.show();
            result = IMAGE_PICKER;
            dismiss();
        }
        else{
            super.show();
            if (!textcardEnabled) { textBtn.hide(); }
            if (!cameraEnabled){ cameraBtn.hide(); }
        }
    }

    public boolean resultIsCamera(){ return TextUtils.equals(result, IMAGE_CAMERA); }
    public boolean resultIsGalleryPicker(){ return TextUtils.equals(result, IMAGE_PICKER); }
    public boolean resultIsTextForImage(){ return TextUtils.equals(result, IMAGE_TEXT); }
}
