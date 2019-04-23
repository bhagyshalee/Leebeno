package leebeno.com.leebeno.LabourPart.Fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import leebeno.com.leebeno.R;


public class MessageLabourFragment extends Fragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener{

    private TabLayout tabLayoutMessage;
    private ViewPager pagerBookingMessage;
    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_message_labour, container, false);
        ButterKnife.bind(this,v);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tabLayoutMessage = (TabLayout) v.findViewById(R.id.tabLayoutMessage);


        tabLayoutMessage.addTab(tabLayoutMessage.newTab());
        tabLayoutMessage.addTab(tabLayoutMessage.newTab());
        tabLayoutMessage.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerBookingMessage = (ViewPager) v.findViewById(R.id.pagerBookingMessage);
        PagerAdpterr adapter = new PagerAdpterr(getActivity().getSupportFragmentManager(), tabLayoutMessage.getTabCount());
        pagerBookingMessage.setAdapter(adapter);
        pagerBookingMessage.setOffscreenPageLimit(2);
        tabLayoutMessage.setOnTabSelectedListener(this);
        tabLayoutMessage.setupWithViewPager(pagerBookingMessage);

        return v;
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pagerBookingMessage.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        pagerBookingMessage.setCurrentItem(tab.getPosition());
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
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.customer_text)),
                String.valueOf(getText(R.string.group))};
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
                    CustomerMessageFragment tab1 = new CustomerMessageFragment();
                    return tab1;
                case 1:
                    GroupMessageFragment tab2 = new GroupMessageFragment();
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
