package com.jjoseba.pecsmobile.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.CardPECS;

public class EditCardDialog extends Dialog{

    private Context ctx;
    private CardPECS card;
    private boolean cardChanged = false;

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
        ((TextView) this.findViewById(R.id.cardLabel)).setText(card.getLabel());
        this.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCardDialog.this.dismiss();
            }
        });

        View deleteBtn =  this.findViewById(R.id.delete_button);
        View editBtn = this.findViewById(R.id.edit_button);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(ctx);
                db.deleteCard(card);
                cardChanged = true;
                EditCardDialog.this.dismiss();
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
