package com.jjoseba.pecsmobile.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.ui.NewCardListener;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import org.w3c.dom.Text;

public class NewCardFragment extends Fragment {

    private static float EXTRA_TRANSLATION = 300f;
    private static long ANIM_DURATION = 800;
    private static String PREVIOUS_COLOR = "previousColor";

    private ColorPicker picker;
    private View colorPickerContainer;
    private View cardFrame;
    private View colorBucket;
    private int previousColor = CardPECS.DEFAULT_COLOR;

    private TextView cardTitleTextView;
    private Switch switchCategory;
    private Button saveButton;

    private boolean disableOkButton = false;
    private NewCardListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_new_card, container, false);

        /*ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(cardColor));
        }*/

        cardFrame = view.findViewById(R.id.card_frame);
        colorBucket = view.findViewById(R.id.colorBucket);
        cardTitleTextView = (TextView) view.findViewById(R.id.et_title);
        switchCategory = (Switch) view.findViewById(R.id.sw_category);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardPECS newCard = new CardPECS();
                newCard.setCardColor(String.format("#%06X", (0xFFFFFF & previousColor)));
                newCard.animateOnAppear = true;
                newCard.setLabel(cardTitleTextView.getText().toString());
                newCard.setAsCategory(switchCategory.isChecked());
                if (listener != null){ listener.onNewCard(newCard); }
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){ listener.onCancel(); }
            }
        });

        colorPickerContainer = view.findViewById(R.id.pickerContainer);
        colorPickerContainer.setOnTouchListener(new View.OnTouchListener() {
            //Cancelamos la propagación del pulsado cuando colorPickerContainer es visible
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return (colorPickerContainer.getVisibility() == View.VISIBLE);
            }
        });

        picker = (ColorPicker) colorPickerContainer.findViewById(R.id.picker);
        picker.addSaturationBar((SaturationBar) colorPickerContainer.findViewById(R.id.saturationbar) );
        picker.addValueBar((ValueBar) colorPickerContainer.findViewById(R.id.valuebar));

        Button selectColorBtn = (Button) colorPickerContainer.findViewById(R.id.select_color_btn);
        selectColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedColor = picker.getColor();
                picker.setOldCenterColor(selectedColor);
                hideColorPicker();
                changeColor(selectedColor, true);
            }
        });

        ImageButton pickColor = (ImageButton) view.findViewById(R.id.pickColorButton);
        pickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPicker();
            }
        });

        return view;
    }

    public void showColorPicker(){
        toggleColorPicker(true);
        disableOkButton = true;
    }

    public void hideColorPicker(){
        toggleColorPicker(false);
        disableOkButton = false;
    }

    public boolean isColorPickerVisible(){
        return colorPickerContainer.getVisibility() == View.VISIBLE;
    }

    public void setNewCardListener(NewCardListener newCardListener){
        listener = newCardListener;
    }

    protected void changeColor(int colorTo, boolean animate){
        int colorFrom = previousColor;
        if (animate){
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    cardFrame.setBackgroundColor((Integer)animator.getAnimatedValue());
                    colorBucket.setBackgroundColor((Integer)animator.getAnimatedValue());
                }
            });
            colorAnimation.setDuration(ANIM_DURATION).start();
        }
        else{
            cardFrame.setBackgroundColor(colorTo);
            colorBucket.setBackgroundColor(colorTo);
        }


        previousColor = colorTo;
    }
    protected void toggleColorPicker(boolean show){

        float translation = - colorPickerContainer.getMeasuredHeight() - EXTRA_TRANSLATION;
        float initY  =  show ? translation : 0f;
        float finalY = !show ? translation : 0f;

        TranslateAnimation anim = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, initY,
                TranslateAnimation.ABSOLUTE, finalY);
        anim.setDuration(ANIM_DURATION);
        anim.setFillAfter(false);

        if (show) { colorPickerContainer.setVisibility(View.VISIBLE); }
        colorPickerContainer.startAnimation(anim);
        if (!show) { colorPickerContainer.setVisibility(View.INVISIBLE); }

    }

    public void resetForm(CardPECS clicked) {
        changeColor(clicked.getCardColor(), false);
        picker.setColor(clicked.getCardColor());
        cardTitleTextView.setText("");
        switchCategory.setChecked(false);
    }
}
