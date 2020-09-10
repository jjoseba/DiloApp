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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
                result = IMAGE_PICKER;
                ImageDialog.this.dismiss();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = IMAGE_CAMERA;
                ImageDialog.this.dismiss();
            }
        });

        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = IMAGE_TEXT;
                ImageDialog.this.dismiss();
            }
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
            cameraBtn.setVisibility(cameraEnabled ? View.VISIBLE : View.GONE);
            textBtn.setVisibility(textcardEnabled ? View.VISIBLE : View.GONE);
            super.show();
        }

    }

    public boolean isCamera(){ return TextUtils.equals(result, IMAGE_CAMERA); }
    public boolean isGalleryPicker(){ return TextUtils.equals(result, IMAGE_PICKER); }
    public boolean isTextForImage(){ return TextUtils.equals(result, IMAGE_TEXT); }
}
