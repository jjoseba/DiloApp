package com.jjoseba.pecsmobile.activity;

import com.jjoseba.pecsmobile.fragment.CardsPage;
import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.ui.EnableableViewPager;
import com.jjoseba.pecsmobile.ui.GridItemClickedListener;
import com.jjoseba.pecsmobile.ui.ZoomOutPageTransformer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CardsActivity extends FragmentActivity implements GridItemClickedListener, ViewPager.OnPageChangeListener {

    private static int NUM_PAGES = 2;

    private EnableableViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ZoomOutPageTransformer mPageTransformer;
    private int mLastPage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);

        mPager = (EnableableViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(this.getSupportFragmentManager());
        mPageTransformer = new ZoomOutPageTransformer();

        mPager.setAdapter(mPagerAdapter);
        //mPager.setPageTransformer(false, mPageTransformer);
        mPager.setPagingEnabled(false);
        mPager.setOnPageChangeListener(this);
        mLastPage = 0;

    }

    @Override
    public void onBackPressed() {
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
    public void onClick(int position) {

        mPageTransformer.setAnimateFromRight((position % 2 != 0));

        int target = mPager.getCurrentItem() + 1;
        NUM_PAGES = target + 1;
        mPagerAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(target, true);
        mPager.setPagingEnabled(true);
    }

    @Override
    public void onPageSelected(int page) {
        int currentPage = mPager.getCurrentItem();
        if ( currentPage == 0){
            mPager.setPagingEnabled(false);
        }
        else if (currentPage < mLastPage){
            // swiping back --> remove last page
            NUM_PAGES--;
            mPagerAdapter.notifyDataSetChanged();
        }
        Log.d("pager", "" + NUM_PAGES);
        mLastPage = currentPage;
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }


    @Override
    public void onPageScrollStateChanged(int i) {

    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter  {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            CardsPage page = new CardsPage(position);
            page.setOnClickListener(CardsActivity.this);
            return page;
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}
