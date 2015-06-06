package com.jjoseba.pecsmobile.ui;

import android.app.Activity;
import android.app.Dialog;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.fragment.NewCardFragment;
import com.jjoseba.pecsmobile.model.CardPECS;

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
        this.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setContentView(R.layout.image_dialog);

        this.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog.this.dismiss();
            }
        });

        View deleteBtn =  this.findViewById(R.id.gallery_button);
        View editBtn = this.findViewById(R.id.edit_button);
        View textBtn = this.findViewById(R.id.text_button);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                parentFragment.startActivityForResult(intent, NewCardFragment.REQUEST_IMAGE);
                ImageDialog.this.dismiss();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(parentFragment.getActivity().getPackageManager()) != null) {
                    parentFragment.startActivityForResult(takePictureIntent, NewCardFragment.REQUEST_CAMERA);
                    ImageDialog.this.dismiss();
                }
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
        deleteBtn.startAnimation(appearButton1);
        editBtn.startAnimation(appearButton2);
        textBtn.startAnimation(appearButton3);

        super.show();
    }

    public boolean hasDataChanged(){
        return cardChanged;
    }
    public boolean isTextForImage(){ return textForImage; }
}
