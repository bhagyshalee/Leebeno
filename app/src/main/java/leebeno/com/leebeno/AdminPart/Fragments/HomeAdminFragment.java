package leebeno.com.leebeno.AdminPart.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.Adapters.FeedJobAdapter;
import leebeno.com.leebeno.AdminPart.HomeAdminActivity;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;

import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

import static leebeno.com.leebeno.AdminPart.HomeAdminActivity.dateSearchAdmin;
import static leebeno.com.leebeno.AdminPart.HomeAdminActivity.searchViewAdmin;
import static leebeno.com.leebeno.LabourPart.HomeLabourActivity.dateSearchLabour;
import static leebeno.com.leebeno.LabourPart.HomeLabourActivity.searchViewLabour;
import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;

public class HomeAdminFragment extends Fragment implements WebCompleteTask {

    static int valueGett = 0;
    public ArrayList<String> mAllDataHomeAdmin = new ArrayList<String>();
    public RecyclerView recyclerJobsAdmin;
    public List<String> listJobItemHomeAdmin;
    public FeedJobAdapter createNewJobAdapter;
    List<JobGetterSetter> getPostedJobs;
    TextView serviceNotMsg;
    CardView homeCard;
    SwipeRefreshLayout setOnRefreshListener;


    public static HomeAdminFragment mInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_admin, container, false);
        ButterKnife.bind(this, v);
        getPostedJobs = new ArrayList<>();
        mInstance = this;

        recyclerJobsAdmin = (RecyclerView) v.findViewById(R.id.recyclerJobsAdmin);
        serviceNotMsg = (TextView) v.findViewById(R.id.serviceNotMsg);
        homeCard = (CardView) v.findViewById(R.id.homeCard);

        createNewJobAdapter = new FeedJobAdapter(getPostedJobs, getActivity());
        recyclerJobsAdmin.setAdapter(createNewJobAdapter);
        recyclerJobsAdmin.setHasFixedSize(true);
        recyclerJobsAdmin.setNestedScrollingEnabled(true);
        Log.e("loooginn", SharedPrefManager.getAccessToken(getActivity(), Constrants.AccessToken) + "   " + SharedPrefManager.getUserID(getActivity(), Constrants.UserID));
        recyclerJobsAdmin.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        setOnRefreshListener = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setOnRefreshListener.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dateSearchAdmin!=null)
                        {
                            dateSearchAdmin.setText(null);
                            dateSearchAdmin.setHint(getResources().getString(R.string.date));
                        }
                        if (dateSearchLabour!=null)
                        {
                            dateSearchLabour.setText(null);
                            dateSearchLabour.setHint(getResources().getString(R.string.date));
                        }
                        if (searchViewLabour!=null)
                        {
                            searchViewLabour.setText(null);
                            searchViewLabour.setHint(getResources().getString(R.string.search));
                        }
                        if (searchViewAdmin!=null)
                        {
                            searchViewAdmin.setText(null);
                            searchViewAdmin.setHint(getResources().getString(R.string.search));
                        }
                        setOnRefreshListener.setRefreshing(false);
//                        Log.e("jksjkfdbsfb",""+dateSearchLabour);
                        getAllJob("", "", 0.0, 0.0, 0.0, null, null);
                    }
                }, 2000);
            }
        });

        getAllJob("", "", 0.0, 0.0, 0.0, null, null);
        return v;
    }

    public static HomeAdminFragment getmInstance() {
        return mInstance;
    }

    public void getNewrMeJobs(double lat, double lng) {
        JSONObject locationobj = null;
        showProgress(getActivity());
        if (lat != 0 && lng != 0) {

            try {

                locationobj = new JSONObject();
                locationobj.put("lat", lat);
                locationobj.put("lng", lng);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }
        }


        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JsonObject> getPostData = apiInterface.getNearMeJobs(SharedPrefManager.getAccessToken(getActivity(), Constrants.AccessToken), SharedPrefManager.getLangId(getActivity()
                , Constrants.UserLang), locationobj);
        getPostData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        close();
                        getPostedJobs.clear();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONArray jsonArrayData = jsonObjSuccess.getJSONArray("data");
                        Log.e("HomeFragmentOut", "" + jsonObject);
                        if (jsonArrayData != null && jsonArrayData.length() > 0) {

                            Log.e("HomeFragment", "" + jsonObject);
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                JobGetterSetter jobGetterSetter = new JobGetterSetter();
                                if (jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.postedStatus)) == 0) {

                                    JSONArray jsonArrayBidders = jsonArrayData.getJSONObject(i).getJSONArray("bidders");
                                    jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getString("jobTitle"));
                                    jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("description"));
                                    jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getString("startDate"));
                                    jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getInt("bidCount"));
                                    jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));
                                    jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
                                    if (jsonArrayData.optJSONObject(i).optJSONObject("myBid") != null) {
                                        jobGetterSetter.setBidStatus(jsonArrayData.optJSONObject(i).optJSONObject("myBid").optString("bidStatus"));
                                    }


                                    for (int j = 0; j < jsonArrayBidders.length(); j++) {
                                        jobGetterSetter.setBidders(jsonArrayBidders.optString(j));
                                    }


                                    jobGetterSetter.setPaymentType(jsonArrayData.getJSONObject(i).getString("paymentType"));
                                    if (jsonArrayData.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                        jobGetterSetter.setMin(jsonArrayData.getJSONObject(i).getInt("min"));
                                        jobGetterSetter.setMax(jsonArrayData.getJSONObject(i).getInt("max"));
                                    } else {
                                        jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("amount"));
                                    }
                                    valueGett = 1;
                                    getPostedJobs.add(jobGetterSetter);
                                    serviceNotMsg.setVisibility(View.GONE);
                                    homeCard.setVisibility(View.GONE);
                                } else if (valueGett == 0) {
                                    serviceNotMsg.setVisibility(View.VISIBLE);
                                    homeCard.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            serviceNotMsg.setVisibility(View.VISIBLE);
                            homeCard.setVisibility(View.VISIBLE);
                        }
                        createNewJobAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
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

    public void getAllJob(String searching_text, String paymentType, double amount, double minAmt, double maxAmt, JSONArray jobSkill, JSONObject loc) {
        HashMap hashMap = new HashMap();
        Log.e("getAdminGGGG", "" + searching_text + " " + paymentType + " " + amount + " " + minAmt + " " + maxAmt + " " + jobSkill + " " + loc);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getLabourAllJob + "sDate=" + searching_text + "&" + "paymentType=" +
                paymentType + "&" + "amount=" + amount + "&" + "minAmt=" + minAmt + "&" + "maxAmt=" + maxAmt + "&" + "jobSkill=" + jobSkill
                + "&" + "loc=" + loc, hashMap, HomeAdminFragment.this, RequestCode.CODE_GetLabourAllJobs, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllJob("", "", 0.0, 0.0, 0.0, null, null);

    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_GetLabourAllJobs) {
//                close();
              /*  Activity activity = getActivity();
                if(activity != null) {
               */
                getPostedJobs.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONArray jsonArrayData = jsonObjSuccess.getJSONArray("data");
                Log.e("HomeFragmentOut", "" + jsonObject);
                if (jsonArrayData != null && jsonArrayData.length() > 0) {

                    Log.e("HomeFragment", "" + jsonObject);
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JobGetterSetter jobGetterSetter = new JobGetterSetter();
                        if (jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.postedStatus)) == 0) {

                            JSONArray jsonArrayBidders = jsonArrayData.getJSONObject(i).getJSONArray("bidders");

                            jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getString("jobTitle"));


                            jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("description"));
                            jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getString("startDate"));
                            jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getInt("bidCount"));
                            jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));
                            jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
                            if (jsonArrayData.optJSONObject(i).optJSONObject("myBid") != null) {
                                jobGetterSetter.setBidStatus(jsonArrayData.optJSONObject(i).optJSONObject("myBid").optString("bidStatus"));
                            }


                            for (int j = 0; j < jsonArrayBidders.length(); j++) {
                                jobGetterSetter.setBidders(jsonArrayBidders.optString(j));
                            }


                            jobGetterSetter.setPaymentType(jsonArrayData.getJSONObject(i).getString("paymentType"));
                            if (jsonArrayData.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                jobGetterSetter.setMin(jsonArrayData.getJSONObject(i).getInt("min"));
                                jobGetterSetter.setMax(jsonArrayData.getJSONObject(i).getInt("max"));
                            } else {
                                jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("amount"));
                            }
                            valueGett = 1;
                            getPostedJobs.add(jobGetterSetter);
                            serviceNotMsg.setVisibility(View.GONE);
                            homeCard.setVisibility(View.GONE);
                        } else if (valueGett == 0) {
                            serviceNotMsg.setVisibility(View.VISIBLE);
                            homeCard.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    serviceNotMsg.setVisibility(View.VISIBLE);
                    homeCard.setVisibility(View.VISIBLE);
                }
                createNewJobAdapter.notifyDataSetChanged();
//                }

            }
        } catch (Exception e) {
            Log.e("exceptionGetmyBid", "" + e);
        }

    }
}
