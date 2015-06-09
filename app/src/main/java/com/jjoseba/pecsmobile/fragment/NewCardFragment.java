package com.jjoseba.pecsmobile.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.ui.ImageDialog;
import com.jjoseba.pecsmobile.ui.NewCardListener;
import com.jjoseba.pecsmobile.util.FileUtils;
import com.jjoseba.pecsmobile.util.ImageUtils;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.squareup.picasso.Picasso;

public class NewCardFragment extends Fragment {

    private static float EXTRA_TRANSLATION = 300f;
    private static long ANIM_DURATION = 800;
    private static String PREVIOUS_COLOR = "previousColor";
    public static final int REQUEST_IMAGE = 1;
    public static final int REQUEST_CAMERA = 2;

    private ColorPicker picker;
    private View colorPickerContainer;
    private View cardFrame;
    private View colorBucket;
    private ImageView cardImage;
    private int previousColor = CardPECS.DEFAULT_COLOR;

    private TextView cardTitleTextView;
    private TextView cardTextImage;
    private Switch switchCategory;
    private Switch switchDisabled;

    private boolean disableOkButton = false;
    private NewCardListener listener;
    private int parentCard;

    private boolean textAsImage = false;

    private String cardImagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_new_card, container, false);

        cardFrame = view.findViewById(R.id.card_frame);
        colorBucket = view.findViewById(R.id.colorBucket);
        cardTitleTextView = (TextView) view.findViewById(R.id.et_title);
        cardTitleTextView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) cardTextImage.setText(s);
            }
        });
        cardTextImage = (TextView) view.findViewById(R.id.card_imageText);
        switchCategory = (Switch) view.findViewById(R.id.sw_category);
        switchDisabled = (Switch) view.findViewById(R.id.sw_disabled);
        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disableOkButton) {
                    //the colorPicker is visible, so we select the current color
                    selectColor();
                } else {
                    if (validateForm()) {
                        CardPECS newCard = new CardPECS();
                        newCard.setCardColor(String.format("#%06X", (0xFFFFFF & previousColor)));
                        newCard.animateOnAppear = true;
                        newCard.setLabel(cardTitleTextView.getText().toString());
                        newCard.setAsCategory(switchCategory.isChecked());
                        newCard.setDisabled(switchDisabled.isChecked());
                        if (cardImagePath != null){
                            newCard.setImageFilename(FileUtils.copyFileToInternal(cardImagePath));
                        }
                        else if (textAsImage){
                            cardTextImage.setTextColor(previousColor);
                            newCard.setImageFilename(ImageUtils.saveViewImage(cardTextImage));
                            cardTextImage.setTextColor(0x000000);
                        }
                        DBHelper db = new DBHelper(NewCardFragment.this.getActivity());
                        db.addCard(parentCard, newCard);

                        if (listener != null) {
                            listener.onNewCard(newCard);
                        }
                    }

                }
            }
        });

        cardImage = (ImageView) view.findViewById(R.id.card_image);
        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageDialog dialog = new ImageDialog(NewCardFragment.this);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface d) {
                        if (dialog.isTextForImage()){
                            setTextForImage();
                        }
                    }
                });

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
            public void onClick(View v) { selectColor(); }
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

    private boolean validateForm() {
        String label = cardTitleTextView.getText().toString();
        if (label.trim().length() == 0){
            String errMessage = "Hay que introducir un texto a la tarjeta!";
            RelativeSizeSpan scaleStyle = new RelativeSizeSpan (1.8f);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errMessage);
            ssbuilder.setSpan(scaleStyle, 0, errMessage.length(), 0);

            cardTitleTextView.requestFocus();
            cardTitleTextView.setError(ssbuilder);
            return false;
        }
        else return true;

    }

    private void selectColor(){
        int selectedColor = picker.getColor();
        picker.setOldCenterColor(selectedColor);
        hideColorPicker();
        changeColor(selectedColor, true);
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

     @Override
     public void onActivityResult(int requestCode, int resultCode,
                                  Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
         if(resultCode == Activity.RESULT_OK){
             switch(requestCode) {

                 case REQUEST_IMAGE:
                     Uri selectedImage = imageReturnedIntent.getData();
                     String[] filePathColumn = {MediaStore.Images.Media.DATA};
                     Picasso.with(this.getActivity()).load(selectedImage).into(cardImage);
                     cardImagePath = FileUtils.getPath(this.getActivity(), selectedImage);
                     hideTextForImage();

                     break;

                 case REQUEST_CAMERA:
                     Bundle extras = imageReturnedIntent.getExtras();
                     Bitmap imageBitmap = (Bitmap) extras.get("data");
                     cardImage.setImageBitmap(imageBitmap);
                     hideTextForImage();

                     break;
             }
         }

    }

    private void setTextForImage(){
        textAsImage = true;
        cardTextImage.setVisibility(View.VISIBLE);
        cardTextImage.setAllCaps(true);
        cardTextImage.setText(cardTitleTextView.getText());
    }

    private void hideTextForImage(){
        cardTextImage.setVisibility(View.GONE);
        textAsImage = false;

    }

    public void resetForm(CardPECS clicked) {
        changeColor(clicked.getCardColor(), false);

        picker.setColor(clicked.getCardColor());
        cardTitleTextView.setText("");
        switchCategory.setChecked(false);
        switchDisabled.setChecked(false);
        cardImage.setImageDrawable(null);
        this.parentCard = clicked.getCardId();
    }
}
