package leebeno.com.leebeno;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.CreateJobActivity;
import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.Fragments.BankHistory;
import leebeno.com.leebeno.Fragments.PointsHistory;
import leebeno.com.leebeno.Fragments.WalletHistory;
import leebeno.com.leebeno.LabourPart.Fragments.CustomerMessageFragment;
import leebeno.com.leebeno.LabourPart.Fragments.GroupMessageFragment;
import leebeno.com.leebeno.LabourPart.Fragments.MessageLabourFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class Wallet extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.tabLayoutWallet)
    TabLayout tabLayoutWallet;
    @Bind(R.id.pagerBookingWallet)
    ViewPager pagerBookingWallet;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.layoutRewardPoints)
    LinearLayout layoutRewardPoints;
    @Bind(R.id.walletBalance)
    TextView walletBalance;
    @Bind(R.id.btnTransferToBank)
    Button btnTransferToBank;
    @Bind(R.id.btnTransferToWallet)
    Button btnTransferToWallet;
    @Bind(R.id.rechargeWallet)
    TextView rechargeWallet;
    @Bind(R.id.rewardPoint)
    TextView rewardPoint;
   static int walletAmountValue,rewardPointValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        tabLayoutWallet.addTab(tabLayoutWallet.newTab());
        tabLayoutWallet.addTab(tabLayoutWallet.newTab());
        tabLayoutWallet.addTab(tabLayoutWallet.newTab());
        //tabLayoutWallet.setTabGravity(TabLayout.GRAVITY_FILL);

        PagerAdpterr adapter = new PagerAdpterr(getSupportFragmentManager(), tabLayoutWallet.getTabCount());
        pagerBookingWallet.setAdapter(adapter);
        pagerBookingWallet.setOffscreenPageLimit(3);

        SpannableString content = new SpannableString(getResources().getString(R.string.recharge_wallet));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        rechargeWallet.setText(content);

        tabLayoutWallet.setupWithViewPager(pagerBookingWallet);
        tabLayoutWallet.setOnTabSelectedListener(this);
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnTransferToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(Wallet.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.design_reward_point);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                    final ImageView crossPopup = (ImageView) dialog.findViewById(R.id.crossPopup);
                    final TextView textMoney = (TextView) dialog.findViewById(R.id.textMoney);
                    final TextView titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
                    final TextView suggePoints = (TextView) dialog.findViewById(R.id.suggePoints);
                    final ImageView imagePointPeck = (ImageView) dialog.findViewById(R.id.imagePointPeck);

                    btnSubmit.setText(getResources().getString(R.string.transfer_to_wallet));
                    titlePopup.setText(getResources().getString(R.string.rewarded_points));
                    btnSubmit.setTextSize(getResources().getDimension(R.dimen._5sdp));
                    suggePoints.setTextSize(getResources().getDimension(R.dimen._4sdp));
                    //textMoney.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.points_peck, 0);
                    textMoney.setVisibility(View.VISIBLE);
                    suggePoints.setVisibility(View.VISIBLE);
                    imagePointPeck.setVisibility(View.VISIBLE);
                    textMoney.setText(""+rewardPointValue);

                    //textMoney.setGravity(View.FOCUS_LEFT);
                    titlePopup.setTypeface(titlePopup.getTypeface(), Typeface.BOLD);

                    dialog.show();

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            //showToast(Wallet.this,"Points successfully transferred");
                          // int totalValue= walletBalance.getText().toString();

                            showMsgMinimumBalance();
                        }
                    });
                    crossPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        getWalletAmount();

    }


    private void getWalletAmount() {
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        List<MultipartBody.Part> photos = new ArrayList<>();
        showProgress(Wallet.this);
/*

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jobId", jobId);
        jsonObject.addProperty("feedBack", feedback);
*/

        Call<JsonObject> getPostData = apiInterface.getWallet(SharedPrefManager.getAccessToken(Wallet.this, Constrants.AccessToken));
        getPostData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        close();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("fdvvbsjkdsv", jsonObject.toString() + "");
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
                        JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");
//                        showToast(Wallet.this, jsonObjectMsg.getString("replyMessage"));
                        walletAmountValue=jsonObjectData.getInt("walletAmt");
                        rewardPointValue=jsonObjectData.getInt("rewardPoint");
                        walletBalance.setText(""+walletAmountValue);
                        rewardPoint.setText(""+rewardPointValue);
//                        finish();

                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
                    }
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(Wallet.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("BidPlace", "" + t);
                close();
            }
        });
    }


    private void showMsgMinimumBalance() {
        try {
            final Dialog dialog = new Dialog(Wallet.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setContentView(R.layout.show_msg_trans);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            final TextView textMoney = (TextView) dialog.findViewById(R.id.textMoney);
            final TextView titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);


            btnSubmit.setText(getResources().getString(R.string.yes));
            btnCancel.setText(getResources().getString(R.string.ok));
            titlePopup.setText(getResources().getString(R.string.minimum_points_confirmation));
            textMoney.setText(getResources().getString(R.string.alert));
            titlePopup.setTextSize(getResources().getDimension(R.dimen._5sdp));
            textMoney.setTextSize(getResources().getDimension(R.dimen._7sdp));
            btnSubmit.setVisibility(View.GONE);
            textMoney.setVisibility(View.VISIBLE);
            textMoney.setTypeface(titlePopup.getTypeface(), Typeface.BOLD);
            dialog.show();

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    //showToast(Wallet.this,"Points successfully transferred");
                    // int totalValue= walletBalance.getText().toString();

                    //showMsgMinimumBalance();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      //  pagerBookingWallet.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
       // pagerBookingWallet.setCurrentItem(tab.getPosition());
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
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.walletHistory)),
                String.valueOf(getText(R.string.bankHistory)),String.valueOf(getText(R.string.rewardedHistory))};
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
                    WalletHistory tab1 = new WalletHistory();
                    return tab1;
                case 1:
                    BankHistory tab2 = new BankHistory();
                    return tab2;
                case 2:
                    PointsHistory tab3 = new PointsHistory();
                    return tab3;

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