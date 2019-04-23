package leebeno.com.leebeno.CustomerPart.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.CustomerPart.Fragments.CategoryFragments.CurrentFragment;
import leebeno.com.leebeno.CustomerPart.Fragments.CategoryFragments.OldFragment;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.R;


public class CategoryCustomerFragment extends Fragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private TabLayout tabLayout;
    private ViewPager pagerBookingCategory;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_category_customer, container, false);
        ButterKnife.bind(this,v);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayoutCategory);


        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerBookingCategory = (ViewPager) v.findViewById(R.id.pagerBookingCategory);
        PagerAdpterr adapter = new PagerAdpterr(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        pagerBookingCategory.setAdapter(adapter);
//        pagerBookingCategory.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(pagerBookingCategory);

        return v;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pagerBookingCategory.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        pagerBookingCategory.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    public class PagerAdpterr extends FragmentStatePagerAdapter

    {
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.ongoing)),
                String.valueOf(getText(R.string.completed))};
        int tabCount;

        public PagerAdpterr(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    CurrentFragment tab1 = new CurrentFragment();
                    return tab1;
                case 1:
                    OldFragment tab2 = new OldFragment();
                    return tab2;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;

        }

    }
}
