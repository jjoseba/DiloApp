package com.jjoseba.pecsmobile.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.util.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import at.markushi.ui.CircleButton;

public class EditCardDialog extends Dialog{

    private Context ctx;
    private CardPECS card;
    private boolean cardChanged = false;
    private boolean cardDeleted = false;

    public EditCardDialog(Context context, CardPECS card){
        super(context);
        ctx = context;
        this.card = card;
    }

    @Override
    public void show(){

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setContentView(R.layout.card_dialog);

        this.findViewById(R.id.card_frame).setBackgroundColor(card.getCardColor());

        File imageFile = new File(card.getImagePath());
        Picasso.with(ctx).load(imageFile).placeholder(R.drawable.empty).error(R.drawable.empty).into((ImageView) findViewById(R.id.card_image));
        ((TextView) this.findViewById(R.id.cardLabel)).setText(card.getLabel());
        this.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardChanged = false;
                EditCardDialog.this.dismiss();
            }
        });

        View deleteBtn =  this.findViewById(R.id.delete_button);
        View editBtn = this.findViewById(R.id.edit_button);
        CircleButton disableBtn = (CircleButton) this.findViewById(R.id.disable_button);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(ctx);
                if (card.getImagePath() != null){
                    FileUtils.deleteImage(card.getImageFilename());
                }
                db.deleteCard(card);
                cardChanged = true;
                cardDeleted = true;
                EditCardDialog.this.dismiss();
            }
        });

        disableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(ctx);
                card.setDisabled(!card.isDisabled());
                db.updateCard(card);
                cardChanged = true;
                EditCardDialog.this.dismiss();
            }
        });
        if (card.isDisabled()){
            disableBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_lock_open));
        }


        Animation appearButton1 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
        Animation appearButton2 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
        Animation appearButton3 = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
        appearButton2.setStartOffset(150);
        appearButton3.setStartOffset(300);

        editBtn.startAnimation(appearButton1);
        disableBtn.startAnimation(appearButton2);
        deleteBtn.startAnimation(appearButton3);
        super.show();
    }

    public boolean hasDataChanged(){
        return cardChanged;
    }

    public boolean isCardDeleted(){ return cardDeleted; }
}
