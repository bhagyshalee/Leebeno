package leebeno.com.leebeno;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.CustomerPart.Fragments.CategoryFragments.CustomerCompletedJobs;
import leebeno.com.leebeno.CustomerPart.Fragments.CategoryFragments.CustomerPostedJobs;
import leebeno.com.leebeno.Adapters.ProfileReviewAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;

public class CustomerProfile extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, WebCompleteTask {

    @Bind(R.id.pagerBookingCustomer)
    ViewPager pagerBookingCustomer;
    @Bind(R.id.tabLayoutCustomer)
    TabLayout tabLayoutCustomer;
    @Bind(R.id.reviewRecycler)
    RecyclerView reviewRecycler;
    List<JobGetterSetter> reviewList;

    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.customerProfile)
    ImageView customerProfile;
    @Bind(R.id.customerName)
    TextView customerName;
    @Bind(R.id.customerRatingBar)
    RatingBar customerRatingBar;
    @Bind(R.id.addressValue)
    TextView addressValue;
    @Bind(R.id.titleReview)
    TextView titleReview;

    ProfileReviewAdapter profileReviewAdapter;

    String providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final String bidRequest = getIntent().getStringExtra("jobStatus");
        providerId = getIntent().getStringExtra("providerId");

        Log.e("dfhvdfvdfv", "" + providerId);
        reviewList = new ArrayList<>();
        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setNestedScrollingEnabled(true);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        tabLayoutCustomer.addTab(tabLayoutCustomer.newTab());
        tabLayoutCustomer.addTab(tabLayoutCustomer.newTab());
        tabLayoutCustomer.setTabGravity(TabLayout.GRAVITY_FILL);

        PagerAdpterr adapter = new PagerAdpterr(getSupportFragmentManager(), tabLayoutCustomer.getTabCount());
        pagerBookingCustomer.setAdapter(adapter);
        pagerBookingCustomer.setOffscreenPageLimit(2);
        tabLayoutCustomer.setOnTabSelectedListener(this);
        tabLayoutCustomer.setupWithViewPager(pagerBookingCustomer);


        HashMap hashMap = new HashMap();
        new WebTask(CustomerProfile.this, WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + providerId,
                hashMap, CustomerProfile.this, RequestCode.CODE_getProfileById, 3);

        Log.e("", providerId + " ");

        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

     /*   reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
        reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
        reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
        reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
*/


    }

    @Override
    public void onResume() {
        super.onResume();

        HashMap hashMap = new HashMap();
        new WebTask(CustomerProfile.this, WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + providerId,
                hashMap, CustomerProfile.this, RequestCode.CODE_getProfileById, 3);

    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_getProfileById) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");
                reviewList.clear();
//                JSONArray jsonArraySkill = jsonObjectData.getJSONArray("skill");
                customerName.setText(jsonObjectData.optString("fullName"));

                addressValue.setText(jsonObjectData.getJSONObject("address").optString("street") + " " + jsonObjectData.getJSONObject("address").optString("value"));


                Log.e("CustomerProJKJK", jsonObjSuccess.toString());

                Picasso.get()
                        .load(WebUrls.BASE_URL + jsonObjectData.optString("image"))
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image)
                        .into(customerProfile);
                JobGetterSetter jobGetterSetter = new JobGetterSetter();

                if (jsonObjectData.optJSONArray("ratings").length() != 0 && jsonObjectData.optJSONArray("ratings") != null) {

                    for (int i = 0; i < jsonObjectData.optJSONArray("ratings").length(); i++) {
                        if (jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("forUser").compareTo("customer") == 0) {
                            Log.e("reviewDetail", jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("comment")
                                    + "  " + jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("image")
                                    + "  " + jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("fullName"));
                            jobGetterSetter.setPofilePic(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("image"));
                            jobGetterSetter.setApplierName(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("fullName"));
                            jobGetterSetter.setJobRating(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("userRating"));
                            jobGetterSetter.setCreatedJob(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("createdAt"));
                            jobGetterSetter.setReviewComment(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("comment"));
                            customerRatingBar.setRating(jsonObjectData.optJSONObject("rating").optInt("avgRating"));
                            reviewList.add(jobGetterSetter);
                        }
                    }

                } else {
                    titleReview.setVisibility(View.GONE);
                }

                profileReviewAdapter = new ProfileReviewAdapter(reviewList, CustomerProfile.this);
                reviewRecycler.setAdapter(profileReviewAdapter);

             /*   Log.e("neerbbgbt", "" + jsonArraySkill.length());

                List<String> listSkill = new ArrayList<>();
                for (int i = 0; i < jsonArraySkill.length(); i++) {
                    //skillGet = TextUtils.join(", ", new String[]{jsonArraySkill.getJSONObject(i).optString("name")});

                    listSkill.add(jsonArraySkill.getJSONObject(i).optString("name"));
                }
*/





              /*  Log.e("dATA", "" + jsonObjSuccess.toString());
                Date d = null;
                try {
                    d = inputFormat.parse("" + jsonObjectData.optString("createdAt"));
                } catch (ParseException ex) {
                    Log.e("neerbbgbt", "" + ex);
                }

                providerPostedTime.setText("Posted " + getDateDifference(d));*/

            }
        } catch (Exception e) {
            Log.e("dfbmdbfdb", "" + e);
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    public class PagerAdpterr extends FragmentStatePagerAdapter {
        int tabCount;
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.posted)),
                String.valueOf(getText(R.string.completed))};

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
                    CustomerPostedJobs tab1 = new CustomerPostedJobs();
                    return tab1;
                case 1:
                    CustomerCompletedJobs tab2 = new CustomerCompletedJobs();
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
