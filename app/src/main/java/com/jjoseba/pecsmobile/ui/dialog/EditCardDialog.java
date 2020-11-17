package com.jjoseba.pecsmobile.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.CardFormActivity;
import com.jjoseba.pecsmobile.activity.PrefsActivity;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.util.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCardDialog extends FABAnimatedDialog{

    private final Context ctx;
    private final Card card;
    private boolean cardChanged = false;
    private boolean cardDeleted = false;
    private boolean updateCard = false;

    public EditCardDialog(Context context, Card card){
        super(context);
        ctx = context;
        this.card = card;
    }

    @BindView(R.id.delete_button) FloatingActionButton deleteBtn;
    @BindView(R.id.disable_button) FloatingActionButton disableBtn;
    @BindView(R.id.edit_button) FloatingActionButton editBtn;

    @OnClick(R.id.delete_button)
    void deleteCard(){
        if (card.getImagePath() != null){
            FileUtils.deleteImage(card.getImageFilename());
        }
        DBHelper.getInstance(ctx).deleteCard(card);
        cardChanged = true;
        cardDeleted = true;
        EditCardDialog.this.dismiss();
    }

    @OnClick(R.id.disable_button)
    void toggleEnabledCard(){
        card.setDisabled(!card.isDisabled());
        DBHelper.getInstance(ctx).updateCard(card);
        cardChanged = true;
        EditCardDialog.this.dismiss();
    }

    @OnClick(R.id.edit_button)
    void editCard(){
        updateCard = true;
        dismiss();
    }

    @Override
    public void show(){
        this.setContentView(R.layout.dialog_card);
        ButterKnife.bind(this);

        this.findViewById(R.id.card_frame).setBackgroundColor(card.getCardColor());

        File imageFile = new File(card.getImagePath());
        Picasso.get().load(imageFile).placeholder(R.drawable.empty).error(R.drawable.empty).into((ImageView) findViewById(R.id.card_image));
        ((TextView) this.findViewById(R.id.cardLabel)).setText(card.getLabel());
        this.findViewById(R.id.cancelButton).setOnClickListener(v -> {
            cardChanged = false;
            EditCardDialog.this.dismiss();
        });
        animatableButtons = new ArrayList<>(Arrays.asList(editBtn, deleteBtn, disableBtn));

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

    public boolean shouldEditCard(){
        return updateCard;
    }

    public boolean isCardDeleted(){ return cardDeleted; }
}
