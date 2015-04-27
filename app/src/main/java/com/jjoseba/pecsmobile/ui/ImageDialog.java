package com.jjoseba.pecsmobile.ui;

import android.app.Activity;
import android.app.Dialog;
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

    private Activity ctx;
    private boolean cardChanged = false;

    public ImageDialog(Activity context){
        super(context);
        ctx = context;
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                ctx.startActivityForResult(intent, NewCardFragment.REQUEST_CODE);
                ImageDialog.this.dismiss();
            }
        });

        Animation appearButton1 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
        Animation appearButton2 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
        appearButton2.setStartOffset(200);
        deleteBtn.startAnimation(appearButton1);
        editBtn.startAnimation(appearButton2);

        super.show();
    }

    public boolean hasDataChanged(){
        return cardChanged;
    }
}
