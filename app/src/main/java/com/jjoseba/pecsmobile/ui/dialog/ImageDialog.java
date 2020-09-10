package com.jjoseba.pecsmobile.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;

public class ImageDialog extends Dialog{

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_dialog);
        Window window = this.getWindow();
        if (window != null){
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        findViewById(R.id.cancelButton).setOnClickListener(v -> ImageDialog.this.dismiss());

        FloatingActionButton galleryBtn =  this.findViewById(R.id.gallery_button);
        FloatingActionButton cameraBtn = this.findViewById(R.id.camera_button);
        FloatingActionButton textBtn = this.findViewById(R.id.text_button);

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

        Animation appearButton1 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
        Animation appearButton2 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
        Animation appearButton3 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
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
