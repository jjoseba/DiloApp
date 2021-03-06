package com.jjoseba.pecsmobile.activity;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.cards.CardPECS;
import com.jjoseba.pecsmobile.ui.dialog.ImageDialog;
import com.jjoseba.pecsmobile.util.FileUtils;
import com.jjoseba.pecsmobile.util.ImageUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class CardFormActivity extends BaseActivity {

    private static final float EXTRA_TRANSLATION = 300f;
    private static final long ANIM_DURATION = 800;
    public static final int REQUEST_IMAGE = 1;
    public static final int REQUEST_CAMERA = 2;
    public static final int REQUEST = 3;

    public static final int RESULT_UPDATED = 33;
    public static final int RESULT_NEW = 44;

    public static final String CARD_RESULT = "new_card";
    public static final String EXTRA_PARENT_CARD = "parent_card";
    public static final String EXTRA_NEW_CARD = "is_new";
    public static final String EXTRA_CARD = "card_edit";

    @BindView(R.id.picker) ColorPicker picker;
    @BindView(R.id.pickerContainer) View colorPickerContainer;
    @BindView(R.id.card_frame) View cardFrame;
    @BindView(R.id.colorBucket) CardView colorBucket;
    @BindView(R.id.card_image) ImageView cardImage;
    @BindView(R.id.cancelButton) Button titleButton;

    @BindView(R.id.et_title) EditText cardTitleTextView;
    @BindView(R.id.card_imageText) TextView cardTextImage;
    @BindView(R.id.sw_category) Switch switchCategory;
    @BindView(R.id.sw_disabled) Switch switchDisabled;

    private int previousColor = Card.DEFAULT_COLOR;
    private boolean disableOkButton = false;

    private Card currentCard;
    private boolean isNewCard = true;
    private int parentCardId;

    private boolean textAsImage = false;

    private String cardImagePath;
    private ImageDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_form_card);
        ButterKnife.bind(this);

        //Cancelamos la propagación del pulsado cuando colorPickerContainer es visible
        colorPickerContainer.setOnTouchListener((view, motionEvent) -> (colorPickerContainer.getVisibility() == View.VISIBLE));

        Bundle b = getIntent().getExtras();
        currentCard = (Card) b.getSerializable((EXTRA_CARD));
        isNewCard = b.getBoolean(EXTRA_NEW_CARD, false);

        if (isNewCard){
            Card parentCard = (Card) b.getSerializable(EXTRA_PARENT_CARD);
            parentCardId = parentCard.getCardId();
            initializeViews(null, parentCard.getCardColor());
        }
        else{
            titleButton.setText(R.string.edit_card_title);
            parentCardId = currentCard.getParentID();
            initializeViews(currentCard, currentCard.getCardColor());
        }
    }

    private void initializeViews(Card card, int cardColor){
        cardTitleTextView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    String text = s.toString();
                    cardTextImage.setText(text.toUpperCase());
                    if(!text.equals(text.toUpperCase()))
                    {
                        text=text.toUpperCase();
                        cardTitleTextView.setText(text);
                        cardTitleTextView.setSelection(text.length());
                    }
                }
            }
        });

        if (card != null){
            File imageFile = new File(card.getImagePath());
            Picasso.get().load(imageFile).placeholder(R.drawable.empty).error(R.drawable.empty).into(cardImage);
            cardTitleTextView.setText(card.getLabel());
            switchDisabled.setChecked(card.isDisabled());

            if (card.isCategory()){
                switchCategory.setChecked(card.isCategory());
                if (DBHelper.getInstance(this).getCards(card.getCardId()).size() > 0){
                    // If card is a non-empty category, we disable the category value change
                    switchCategory.setEnabled(false);
                }
            }
        }

        SaturationBar saturationBar = colorPickerContainer.findViewById(R.id.saturationbar);
        ValueBar valueBar = colorPickerContainer.findViewById(R.id.valuebar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        changeColor(cardColor, false);
        picker.setOldCenterColor(cardColor);
        picker.setColor(cardColor);

        if (cardColor == Card.DEFAULT_COLOR){
            saturationBar.setSaturation(0.75f);
            valueBar.setValue(0.85f);
        }
    }


    @OnClick(R.id.saveButton)
    void saveCard(){
        if (disableOkButton) {
            //the colorPicker is visible, so we select the current color
            selectColor();
        } else {
            if (validateForm()) {
                CardPECS newCardValues = new CardPECS();
                newCardValues.setCardColor(String.format("#%06X", (0xFFFFFF & previousColor)));
                newCardValues.setLabel(cardTitleTextView.getText().toString());
                newCardValues.setAsCategory(switchCategory.isChecked());
                newCardValues.setDisabled(switchDisabled.isChecked());
                newCardValues.setParentID(parentCardId);

                if (textAsImage){
                    cardTextImage.setTextColor(previousColor);
                    newCardValues.setImageFilename(ImageUtils.saveViewImage(cardTextImage));
                    cardTextImage.setTextColor(0x000000);
                }
                else if (cardImagePath != null){
                    newCardValues.setImageFilename(FileUtils.copyFileToInternal(cardImagePath));
                }

                Intent returnIntent = new Intent();
                DBHelper db = DBHelper.getInstance(this);
                if (isNewCard){
                    newCardValues.animateOnAppear = true;
                    db.addCard(parentCardId, newCardValues);
                    setResult(RESULT_NEW, returnIntent);
                }
                else{
                    newCardValues.setCardId(currentCard.getCardId());
                    if (newCardValues.getImageFilename() != null){
                        FileUtils.deleteImage(currentCard.getImageFilename());
                    }
                    else{
                        newCardValues.setImageFilename(currentCard.getImageFilename());
                    }
                    db.updateCard(newCardValues);
                    setResult(RESULT_UPDATED, returnIntent);
                }
                returnIntent.putExtra(CARD_RESULT, newCardValues);
                finish();
            }
        }
    }

    @OnClick(R.id.cancelButton)
    @Override
    public void onBackPressed(){
        if (isColorPickerVisible()){
            hideColorPicker();
        }
        else{
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    @OnClick(R.id.card_image)
    void changeCardImage() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener(){
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        dialog = new ImageDialog(CardFormActivity.this);
                        dialog.show();
                        dialog.setOnDismissListener(d -> {
                            if (dialog== null){
                                return;
                            }
                            else if (dialog.resultIsTextForImage()){
                                setTextForImage();
                            }
                            else if (dialog.resultIsCamera()){
                                startCamera();
                            }
                            else if (dialog.resultIsGalleryPicker()){
                                startImagePicker();
                            }
                        });
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        showPermissionSnackbar();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        showPermissionSnackbar();
                    }
                }).check();
    }

    private void showPermissionSnackbar(){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.permissionsRationale, Snackbar.LENGTH_LONG);
        snackbar.setAction("Configuración", v -> {

            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + CardFormActivity.this.getPackageName()));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CardFormActivity.this.startActivity(myAppSettings);
        });
        Log.d("Permissions", "Showing snackbar!");
        snackbar.show();
    }

    private void startImagePicker(){
        boolean success = startSamsungPicker();
        if (success){
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE);
        }
        else {
            //TODO: should log this...
            Toast.makeText(this, R.string.unableResolveImageRequest, Toast.LENGTH_LONG).show();
            success = startDocumentPicker();

            if (success){
                return;
            }
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona imagen");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
            startActivityForResult(chooserIntent, REQUEST_IMAGE);
        }

    }

    private boolean startSamsungPicker(){
        Log.d("Intent", "Trying to launch Samsung file picker...");
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        intent.putExtra("CONTENT_TYPE", "image/*");

        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivityForResult(intent, REQUEST_IMAGE);
            return true;
        }
        else{
            Log.d("Intent",  "No activity found.");
            return false;
        }

    }

    private boolean startDocumentPicker(){
        Log.d("Intent", "Trying to launch brute file picker...");
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivityForResult(intent, REQUEST_IMAGE);
            return true;
        }
        else{
            Log.d("Intent", "No activity found. ");
            return false;
        }

    }

    private void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getTempImageURI(this));
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
        else {
            //TODO: should log this...
            Toast.makeText(this, R.string.unableResolveCameraRequest, Toast.LENGTH_LONG).show();
        }
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

    @OnClick(R.id.select_color_btn)
    void selectColor(){
        int selectedColor = picker.getColor();
        picker.setOldCenterColor(selectedColor);
        hideColorPicker();
        changeColor(selectedColor, true);
    }

    @OnClick(R.id.pickColorButton)
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


    protected void changeColor(int colorTo, boolean animate){
        int colorFrom = previousColor;
        if (animate){
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.addUpdateListener(animator -> {
                int animColor = (Integer)animator.getAnimatedValue();
                cardFrame.setBackgroundColor(animColor);
                colorBucket.setCardBackgroundColor(animColor);
                cardTextImage.setTextColor(animColor);
            });
            colorAnimation.setDuration(ANIM_DURATION).start();
        }
        else{
            cardFrame.setBackgroundColor(colorTo);
            colorBucket.setCardBackgroundColor(colorTo);
            cardTextImage.setTextColor(colorTo);
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

            if (dialog != null){
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                dialog = null;
            }

            switch(requestCode) {

                case Crop.REQUEST_CROP:
                    notifySuccessfulCrop();
                    break;

                case REQUEST_IMAGE:
                    Uri selectedImage = imageReturnedIntent.getData();
                    Picasso.get().load(selectedImage).into(cardImage);
                    hideTextForImage();
                    cardImagePath = FileUtils.copyFileTemp(this, selectedImage);
                    Uri cardImageUri = Uri.fromFile(new File(cardImagePath));

                    Crop.of(cardImageUri, FileUtils.getTempImageURI(this))
                            .asSquare()
                            .withMaxSize(600, 600)
                            .start(this);
                    break;

                case REQUEST_CAMERA:
                    Uri tempUri = FileUtils.getTempImageURI(this);
                    cardImagePath = FileUtils.copyFileTemp(this, tempUri);
                    Picasso.get().load(tempUri).memoryPolicy(MemoryPolicy.NO_CACHE).into(cardImage);

                    hideTextForImage();
                    Crop.of(tempUri, tempUri)
                            .asSquare()
                            .withMaxSize(300, 300)
                            .start(this);
                    break;

            }
        }

    }

    private void setTextForImage(){
        textAsImage = true;
        cardTextImage.setVisibility(View.VISIBLE);
        cardTextImage.setAllCaps(true);
        cardTextImage.setTextColor(picker.getOldCenterColor());
        cardTextImage.setText(cardTitleTextView.getText());
        cardImage.setImageDrawable(null);
    }

    private void hideTextForImage(){
        cardTextImage.setVisibility(View.GONE);
        textAsImage = false;
    }


    public void notifySuccessfulCrop(){
        Log.d("Crop", "Loading:" + cardImagePath);
        cardImagePath = FileUtils.copyFileTemp(this, FileUtils.getTempImageURI(this));
        File image = new File(cardImagePath);
        Log.d("Crop", image.exists()?"Exists!":"noooooo");
        Picasso.get().load(image).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.empty).into(cardImage);
    }
}
