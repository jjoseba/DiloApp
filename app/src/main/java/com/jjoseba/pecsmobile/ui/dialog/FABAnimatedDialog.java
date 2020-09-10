package com.jjoseba.pecsmobile.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.jjoseba.pecsmobile.R;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class FABAnimatedDialog extends Dialog {

    protected Context ctx;
    protected List<View> animatableButtons;

    private static int ANIMATION_DELAY = 150;

    FABAnimatedDialog(@NonNull Context context) {
        super(context);
        this.ctx = context;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        super.setContentView(layoutResID);

        Window window = this.getWindow();
        if (window != null){
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        Button cancelButton = findViewById(R.id.cancelButton);
        if (cancelButton != null){
            cancelButton.setOnClickListener(v -> dismiss());
        }
    }

    @Override
    public void show(){
        super.show();
        if (animatableButtons != null){
            for (int i=0; i < animatableButtons.size(); i++){
                if (animatableButtons.get(i).getVisibility() == View.VISIBLE){
                    Animation appearAnim = AnimationUtils.loadAnimation(ctx, R.anim.button_appear);
                    appearAnim.setStartOffset(ANIMATION_DELAY * i);
                    animatableButtons.get(i).startAnimation(appearAnim);
                }
            }
        }
    }
}
