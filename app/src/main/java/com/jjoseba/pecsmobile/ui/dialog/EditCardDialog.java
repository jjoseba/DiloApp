package com.jjoseba.pecsmobile.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.util.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class EditCardDialog extends FABAnimatedDialog{

    private Context ctx;
    private Card card;
    private boolean cardChanged = false;
    private boolean cardDeleted = false;

    public EditCardDialog(Context context, Card card){
        super(context);
        ctx = context;
        this.card = card;
    }

    @Override
    public void show(){
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

        FloatingActionButton deleteBtn =  this.findViewById(R.id.delete_button);
        FloatingActionButton disableBtn = this.findViewById(R.id.disable_button);
        animatableButtons = new ArrayList<>(Arrays.asList(deleteBtn, disableBtn));

        deleteBtn.setOnClickListener(v -> {
            DBHelper db = DBHelper.getInstance(ctx);
            if (card.getImagePath() != null){
                FileUtils.deleteImage(card.getImageFilename());
            }
            db.deleteCard(card);
            cardChanged = true;
            cardDeleted = true;
            EditCardDialog.this.dismiss();
        });

        disableBtn.setOnClickListener(v -> {
            DBHelper db = DBHelper.getInstance(ctx);
            card.setDisabled(!card.isDisabled());
            db.updateCard(card);
            cardChanged = true;
            EditCardDialog.this.dismiss();
        });
        if (card.isDisabled()){
            disableBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_lock_open));
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        if (!prefs.getBoolean(PrefsActivity.SHOW_DISABLE_CARD, true)){
            disableBtn.hide();
        }
        super.show();
    }

    public boolean hasDataChanged(){
        return cardChanged;
    }

    public boolean isCardDeleted(){ return cardDeleted; }
}
