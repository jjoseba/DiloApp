package com.jjoseba.pecsmobile.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.app.PECSMobile;
import com.jjoseba.pecsmobile.fragment.CardsPage;
import com.jjoseba.pecsmobile.fragment.NewCardFragment;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.CardsGridListener;
import com.jjoseba.pecsmobile.ui.NewCardListener;
import com.jjoseba.pecsmobile.ui.cards.CardPECS;
import com.jjoseba.pecsmobile.ui.dialog.EditCardDialog;
import com.jjoseba.pecsmobile.ui.dialog.HiddenInputDialog;
import com.jjoseba.pecsmobile.ui.displaymode.DisplayModeFactory;
import com.jjoseba.pecsmobile.ui.displaymode.DisplayModeStrategy;
import com.jjoseba.pecsmobile.ui.viewpager.EnableableViewPager;
import com.jjoseba.pecsmobile.ui.viewpager.ZoomOutPageTransformer;
import com.soundcloud.android.crop.Crop;

import java.util.ArrayList;
import java.util.HashMap;

public class CardsActivity extends BaseActivity implements TextToSpeech.OnInitListener, CardsGridListener, NewCardListener, ViewPager.OnPageChangeListener, DisplayModeStrategy.ResetListener {

    private static final boolean FADE_IN = true;
    private static final boolean FADE_OUT = false;

    protected ArrayList<Card> navigationCards = new ArrayList<>();
    private EnableableViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private HashMap<Card, CardsPage> cardPages = new HashMap<>();
    private int mLastPage;

    private NewCardFragment newCardFragment;
    private View newCardContainer;
    private boolean newCardIsHiding = false;

    private boolean newCardButton = PECSMobile.DEFAULT_SHOW_NEWCARD_BUTTON;
    private boolean tempCardButton = PECSMobile.DEFAULT_SHOW_TEMPTEXT_BUTTON;
    private DisplayModeStrategy displayStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_cards);

        navigationCards.add(new CardPECS());
        mPager = (EnableableViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(this.getSupportFragmentManager());
        ZoomOutPageTransformer mPageTransformer = new ZoomOutPageTransformer();

        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(false, mPageTransformer);
        mPager.setPagingEnabled(false);
        mPager.setOnPageChangeListener(this);
        mLastPage = 0;

        newCardContainer = findViewById(R.id.newCardContainer);
        newCardFragment = (NewCardFragment) getSupportFragmentManager().findFragmentById(R.id.new_card_fragment);
        newCardFragment.setNewCardListener(this);

        fetchPreferences();
        displayStrategy = new DisplayModeFactory(prefs).getCurrentDisplayMode();
        displayStrategy.initialize(this, navigationCards);
        displayStrategy.setResetListener(this);
    }

    @Override
    protected void onResume(){

        super.onResume();
        fetchPreferences();
        displayStrategy.onResume(this, navigationCards);
    }

    //Function to reload preferences values in this class
    private void fetchPreferences(){
        tempCardButton = prefs.getBoolean(PrefsActivity.SHOW_TEMPTEXT_CARD, PECSMobile.DEFAULT_SHOW_TEMPTEXT_BUTTON);
        newCardButton = prefs.getBoolean(PrefsActivity.SHOW_ADD_CARD, PECSMobile.DEFAULT_SHOW_NEWCARD_BUTTON);
    }

    @Override
    public void onBackPressed() {
        if (newCardContainer.getVisibility() == View.VISIBLE){
            if (newCardFragment.isColorPickerVisible()){
                newCardFragment.hideColorPicker();
            }
            else if (!newCardIsHiding){
                animateCardContainer(FADE_OUT);
            }
        }
        else if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
            System.exit(0);
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putSerializable("cards", navigationCards);
        //outState.putSerializable("fragments", cardPages);
        outState.putInt("currentPage", mLastPage);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        mLastPage = savedInstanceState.getInt("currentPage", mLastPage);
        ArrayList<Card> cards = (ArrayList<Card>) savedInstanceState.getSerializable("cards");
        if (cards != null && cards.size() > 0){
            navigationCards = cards;
        }
    }

    @Override
    public void onCardSelected(Card clicked) {

        if (clicked.isCategory()){
            int target = mPager.getCurrentItem() + 1;
            navigationCards.add(clicked);
            mPagerAdapter.notifyDataSetChanged();
            mPager.setCurrentItem(target, true);
            mPager.setPagingEnabled(true);
        }
        displayStrategy.onCardSelected(this, clicked);
    }

    @Override
    public void onAddCardButton(Card clicked){
        animateCardContainer(FADE_IN);
        newCardFragment.resetForm(clicked);
    }

    @Override
    public void onTempCardButton() {

        HiddenInputDialog dialog = new HiddenInputDialog();
        dialog.setOnTextListener(new HiddenInputDialog.InputListener() {
            @Override
            public void onText(String cardLabel) {
                displayStrategy.onNewTempTextCard(cardLabel);
            }
        });
        dialog.show(this.getFragmentManager(), "Hidden");
    }

    @Override
    public void onCardLongClick(final Card cardPressed) {
        final EditCardDialog dialog = new EditCardDialog(this, cardPressed);
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface d) {
                if (dialog.hasDataChanged()){
                    Card currentCard = navigationCards.get(mLastPage);
                    CardsPage currentPage = cardPages.get(currentCard);
                    currentPage.notifyCardChanged(cardPressed, dialog.isCardDeleted());
                }
            }
        });
    }

    private void animateCardContainer(final boolean show) {

        //if the card is already hiding, don't launch the animation again
        if (!show && newCardIsHiding) return;

        newCardContainer.setVisibility(View.VISIBLE);
        Animation fadeAnimation = new AlphaAnimation(show?0:1, show?1:0);
        fadeAnimation.setInterpolator(new DecelerateInterpolator()); //add this
        fadeAnimation.setDuration(600);
        fadeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (!show){
                    newCardContainer.setVisibility(View.GONE);
                    newCardIsHiding = false;
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(newCardContainer.getWindowToken(), 0);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        if (!show) newCardIsHiding = true;
        newCardContainer.startAnimation(fadeAnimation);
    }

    @Override
    public void onPageSelected(int page) {
        int currentPage = mPager.getCurrentItem();

        if (currentPage < mLastPage){
            // swiping back --> remove pages
            for (int i=mLastPage;i>currentPage;i--){
                Card lastPageCard = navigationCards.get(i);
                mPagerAdapter.removeFragment(lastPageCard);
                navigationCards.remove(i);
            }
            mPagerAdapter.notifyDataSetChanged();

            if ( currentPage == 0){
                mPager.setPagingEnabled(false);
            }
        }
        mLastPage = currentPage;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {  }

    @Override
    public void onPageScrollStateChanged(int i) {  }

    @Override
    public void onNewCard(Card card) {
        animateCardContainer(FADE_OUT);
        Card currentCard = navigationCards.get(mLastPage);
        CardsPage currentPage = cardPages.get(currentCard);
        currentPage.addCard(card);
    }

    @Override
    public void onCancel() {
        animateCardContainer(FADE_OUT);
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void resetCards() {
        mPager.setCurrentItem(0);
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        private final FragmentManager mFragmentManager;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            Card card = navigationCards.get(position);
            CardsPage page = cardPages.get(card);
            if (page == null){
                page = CardsPage.newInstance(card, newCardButton, tempCardButton);
                cardPages.put(card, page);
            }
            return page;
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        public void removeFragment(Card cardToRemove){
            CardsPage page = cardPages.get(cardToRemove);
            cardPages.remove(cardToRemove);
            mFragmentManager.beginTransaction().remove(page).commit();

        }

        @Override
        public void notifyDataSetChanged(){
            super.notifyDataSetChanged();
            displayStrategy.onSelectedCardsChanged();
        }

        @Override
        public int getCount() {
            return navigationCards.size();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        displayStrategy.onActivityResult(this, requestCode, resultCode);
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            newCardFragment.notifySuccessfulCrop();
        }
    }
}
