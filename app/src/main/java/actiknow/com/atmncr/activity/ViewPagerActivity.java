package actiknow.com.atmncr.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.pixelcan.inkpageindicator.InkPageIndicator;

import actiknow.com.atmncr.R;
import actiknow.com.atmncr.adapter.MyPagerAdapter;
import actiknow.com.atmncr.adapter.SmartFragmentStatePagerAdapter;


public class ViewPagerActivity extends AppCompatActivity {

    
    ViewPager vpPager;
    InkPageIndicator inkPageIndicator;
    // ...
    private SmartFragmentStatePagerAdapter adapterViewPager;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_pager);
        initView ();
        initListener ();
        initAdapter ();

        adapterViewPager = new MyPagerAdapter (getSupportFragmentManager ());
        vpPager.setAdapter (adapterViewPager);

        inkPageIndicator.setViewPager (vpPager);

        vpPager.setClipToPadding (false);
        vpPager.setPageMargin (10);
    }

    private void initAdapter () {
    }

    private void initView () {
        vpPager = (ViewPager) findViewById (R.id.vpPager);
        inkPageIndicator = (InkPageIndicator) findViewById (R.id.indicator);
    }

    private void initListener () {
        // Attach the page change listener inside the activity
        vpPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener () {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected (int position) {
                //     Toast.makeText (ViewPagerActivity.this,
                //             "Selected page position: " + position, Toast.LENGTH_SHORT).show ();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged (int state) {
                // Code goes here
            }
        });
    }

    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
