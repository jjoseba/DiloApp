package com.jjoseba.pecsmobile.activity;

import com.jjoseba.pecsmobile.fragment.CardsPage;
import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.fragment.NewCardFragment;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.ui.EnableableViewPager;
import com.jjoseba.pecsmobile.ui.GridItemClickedListener;
import com.jjoseba.pecsmobile.ui.ZoomOutPageTransformer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CardsActivity extends FragmentActivity implements GridItemClickedListener, ViewPager.OnPageChangeListener {

    private static final boolean FADE_IN = true;
    private static final boolean FADE_OUT = false;

    private EnableableViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ZoomOutPageTransformer mPageTransformer;
    private HashMap<CardPECS, CardsPage> cardPages = new HashMap<CardPECS, CardsPage>();
    private int mLastPage;
    private NewCardFragment newCardFragment;
    private View newCardContainer;
    private boolean newCardIsHiding = false;

    protected ArrayList<CardPECS> navigationCards = new ArrayList<CardPECS>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);

        //Initial card -- change this in the future
        navigationCards.add(new CardPECS());

        mPager = (EnableableViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(this.getSupportFragmentManager());
        mPageTransformer = new ZoomOutPageTransformer();

        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(false, mPageTransformer);
        mPager.setPagingEnabled(false);
        mPager.setOnPageChangeListener(this);
        mLastPage = 0;

        newCardContainer = findViewById(R.id.newCardContainer);
        newCardFragment = (NewCardFragment) getSupportFragmentManager().findFragmentById(R.id.new_card_fragment);
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
            return;
        }
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();

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
        ArrayList<CardPECS> cards = (ArrayList<CardPECS>) savedInstanceState.getSerializable("cards");
        if (cards != null && cards.size() > 0){
            navigationCards = cards;
        }
    }



    @Override
    public void onClick(CardPECS clicked, boolean addChildCard) {

        //mPageTransformer.setAnimateFromRight((position % 2 != 0));
        if (addChildCard){
            animateCardContainer(FADE_IN);
            newCardFragment.resetForm(clicked);
            /*Intent i = new Intent(this, NewCardActivity.class);
            i.putExtra("cardColor", clicked.getCardColor());
            startActivity(i);*/
        }
        else{
            if (clicked.isCategory()){
                int target = mPager.getCurrentItem() + 1;
                navigationCards.add(clicked);
                mPagerAdapter.notifyDataSetChanged();
                mPager.setCurrentItem(target, true);
                mPager.setPagingEnabled(true);
            }
            else{

            }

        }
    }

    private void animateCardContainer(final boolean show) {
        Log.d("MAIN", "showContainer " + show);
        newCardContainer.setVisibility(View.VISIBLE);
        Animation fadeAnimation = new AlphaAnimation(show?0:1, show?1:0);
        fadeAnimation.setInterpolator(new DecelerateInterpolator()); //add this
        fadeAnimation.setDuration(700);
        fadeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (!show){
                    newCardContainer.setVisibility(View.GONE);
                    newCardIsHiding = false;
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
            // swiping back --> remove last page
            CardPECS lastPageCard = navigationCards.get(mLastPage);
            mPagerAdapter.removeFragment(lastPageCard);
            navigationCards.remove(mLastPage);
            mPagerAdapter.notifyDataSetChanged();
            if ( currentPage == 0){
                mPager.setPagingEnabled(false);
            }
        }
        Log.d("pager", "profund" + navigationCards.size());
        mLastPage = currentPage;
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }


    @Override
    public void onPageScrollStateChanged(int i) {

    }


    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        private final FragmentManager mFragmentManager;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            CardPECS card = navigationCards.get(position);
            CardsPage page = cardPages.get(card);
            if (page == null){
                page = CardsPage.newInstance(card);
                cardPages.put(card, page);
            }
            return page;
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;

        }

        public void removeFragment(CardPECS cardToRemove){
            CardsPage page = cardPages.get(cardToRemove);
            cardPages.remove(cardToRemove);
            mFragmentManager.beginTransaction().remove(page).commit();
        }

        @Override
        public int getCount() {
            return navigationCards.size();
        }

    }


}
