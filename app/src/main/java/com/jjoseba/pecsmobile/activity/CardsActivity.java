package com.jjoseba.pecsmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.app.PECSMobile;
import com.jjoseba.pecsmobile.fragment.CardsPageFragment;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.CardsGridListener;
import com.jjoseba.pecsmobile.ui.cards.CardPECS;
import com.jjoseba.pecsmobile.ui.dialog.EditCardDialog;
import com.jjoseba.pecsmobile.ui.dialog.HiddenInputDialog;
import com.jjoseba.pecsmobile.ui.displaymode.DisplayModeFactory;
import com.jjoseba.pecsmobile.ui.displaymode.DisplayModeStrategy;
import com.jjoseba.pecsmobile.ui.viewpager.EnableableViewPager;
import com.jjoseba.pecsmobile.ui.viewpager.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CardsActivity extends BaseActivity implements TextToSpeech.OnInitListener, CardsGridListener, ViewPager.OnPageChangeListener, DisplayModeStrategy.ResetListener {

    protected ArrayList<Card> navigationCards = new ArrayList<>();
    private EnableableViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private HashMap<Card, CardsPageFragment> cardPages = new HashMap<>();
    private int mLastPage;

    private boolean newCardButton = PECSMobile.DEFAULT_SHOW_NEWCARD_BUTTON;
    private boolean tempCardButton = PECSMobile.DEFAULT_SHOW_TEMPTEXT_BUTTON;
    private DisplayModeStrategy displayStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_cards);

        navigationCards.add(new CardPECS());
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(this.getSupportFragmentManager());
        ZoomOutPageTransformer mPageTransformer = new ZoomOutPageTransformer();

        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(false, mPageTransformer);
        mPager.setPagingEnabled(false);
        mPager.setOnPageChangeListener(this);
        mLastPage = 0;

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
        if (mPager.getCurrentItem() == 0) {
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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
        Intent i = new Intent(this, NewCardActivity.class);
        i.putExtra(NewCardActivity.EXTRA_PARENT_CARD, clicked);
        Log.d("NewCard", clicked==null?"null":(""+clicked.getParentID()));

        startActivityForResult(i, NewCardActivity.REQUEST);
    }

    @Override
    public void onTempCardButton() {

        HiddenInputDialog dialog = new HiddenInputDialog();
        dialog.setOnTextListener(cardLabel -> displayStrategy.onNewTempTextCard(cardLabel));
        dialog.show(this.getFragmentManager(), "Hidden");
    }

    @Override
    public void onCardLongClick(final Card cardPressed) {
        final EditCardDialog dialog = new EditCardDialog(this, cardPressed);
        dialog.show();

        dialog.setOnDismissListener(d -> {
            if (dialog.hasDataChanged()){
                Card currentCard = navigationCards.get(mLastPage);
                CardsPageFragment currentPage = cardPages.get(currentCard);
                currentPage.notifyCardChanged(cardPressed, dialog.isCardDeleted());
            }
        });
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

    public void onNewCard(Card card) {
        Card currentCard = navigationCards.get(mLastPage);
        CardsPageFragment currentPage = cardPages.get(currentCard);
        if (currentPage != null){
            currentPage.addCard(card);
        }
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

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Card card = navigationCards.get(position);
            CardsPageFragment page = cardPages.get(card);
            if (page == null){
                page = CardsPageFragment.newInstance(card, newCardButton, tempCardButton);
                cardPages.put(card, page);
            }
            return page;
        }

        @Override
        public int getItemPosition(@NonNull Object object){
            return POSITION_NONE;
        }

        public void removeFragment(Card cardToRemove){
            CardsPageFragment page = cardPages.get(cardToRemove);
            cardPages.remove(cardToRemove);
            if (page != null){
                mFragmentManager.beginTransaction().remove(page).commit();
            }

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

        if(resultCode == Activity.RESULT_OK){
            if (requestCode == NewCardActivity.REQUEST){
                Card newCard = (Card) intent.getSerializableExtra(NewCardActivity.NEW_CARD_RESULT);
                onNewCard(newCard);
            }
        }

    }
}
