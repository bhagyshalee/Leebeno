package leebeno.com.leebeno.AdminPart.Fragments;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.Adapters.MyBidedJobAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.AdminPart.HomeAdminActivity.dateSearchAdmin;
import static leebeno.com.leebeno.AdminPart.HomeAdminActivity.searchViewAdmin;
import static leebeno.com.leebeno.LabourPart.HomeLabourActivity.dateSearchLabour;
import static leebeno.com.leebeno.LabourPart.HomeLabourActivity.searchViewLabour;


public class BidAdminFragment extends Fragment implements WebCompleteTask {

    static int valueGett = 0;
    static ArrayList<String> mAllDataHomeAdmin = new ArrayList<String>();
    public RecyclerView recyclerJobsAdmin;
    public MyBidedJobAdapter createNewJobAdapter;
    List<JobGetterSetter> getPostedJobs;
    @Bind(R.id.homeCard)
    CardView homeCard;
    @Bind(R.id.serviceNotMsg)
    TextView serviceNotMsg;
    SwipeRefreshLayout setOnRefreshListener;

    public static BidAdminFragment mInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bid_admin, container, false);

        mInstance = this;
        ButterKnife.bind(this, v);
        getPostedJobs = new ArrayList<>();


        recyclerJobsAdmin = (RecyclerView) v.findViewById(R.id.recyclerJobsAdmin);
        recyclerJobsAdmin.setHasFixedSize(true);
        recyclerJobsAdmin.setNestedScrollingEnabled(true);
        recyclerJobsAdmin.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        createNewJobAdapter = new MyBidedJobAdapter(getPostedJobs, getActivity());
        recyclerJobsAdmin.setAdapter(createNewJobAdapter);

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
                        getMyJob("");
                    }
                }, 2000);
            }
        });

        getMyJob("");
        Log.e("BidFragmmm", "" + SharedPrefManager.getInstance(getActivity()).getAccessToken()+"  "+SharedPrefManager.getUserID(getActivity(), Constrants.UserID));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyJob("");
    }

    public static BidAdminFragment getInstance(){
        return mInstance;
    }

/*
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listJobItemAdminBid .clear();
     */
/*   listJobItemDB .clear();
        listJobItemAB .clear();
        listJobItemSB .clear();
        listJobItemTB .clear();
        listJobItemStaB .clear();*//*

        if (charText.length() == 0) {
            listJobItemAdminBid.addAll(mAllDataHomeAdmin);
        } else {
            for (String wp : mAllDataHomeAdmin) {
                if (wp.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    listJobItemAdminBid .add(wp);
                }
            }
        }
        createNewJobAdapter.notifyDataSetChanged();
    }
*/

    public void getMyJob(String searching_text){
        HashMap hashMap = new HashMap();
        Log.e("jkjkfdnjkfb",""+searching_text);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getLabourOwnJob+ "sDate=" + searching_text
                , hashMap, BidAdminFragment.this, RequestCode.CODE_GetLabourAllJobs, 3);
    }



    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_GetLabourAllJobs) {
                getPostedJobs.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONArray jsonArrayData = jsonObjSuccess.getJSONArray("data");
                //JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");


                if (jsonArrayData != null && jsonArrayData.length() > 0) {
                    Log.e("BidFragmentLLL", "" + jsonObjSuccess);
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JobGetterSetter jobGetterSetter = new JobGetterSetter();
                        if (jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.postedStatus)) != 0) {
                            jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getString("jobTitle"));
                            jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("description"));
                            jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getString("startDate"));
                            jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getInt("bidCount"));
                            jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));
                            jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
                            jobGetterSetter.setStatus(jsonArrayData.getJSONObject(i).getString("status"));
//                            jobGetterSetter.setApplierName(jsonArrayData.getJSONObject(i).getJSONObject("applier").getString("fullName"));
                            jobGetterSetter.setBidAmount(jsonArrayData.getJSONObject(i).getJSONObject("bid").
                                    getInt("cost"));
                            // jobGetterSetter.setBidders(jsonArrayData.getJSONObject(i).getJSONArray("bidders"));

                           /* jobGetterSetter.setPaymentType(jsonArrayData.getJSONObject(i).getString("paymentType"));
                            if (jsonArrayData.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                jobGetterSetter.setMin(jsonArrayData.getJSONObject(i).getInt("min"));
                                jobGetterSetter.setMax(jsonArrayData.getJSONObject(i).getInt("max"));
                            } else {
                                jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("amount"));
                            }*/
                            jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("finalAmount"));
                            valueGett = 1;
                            getPostedJobs.add(jobGetterSetter);
                            serviceNotMsg.setVisibility(View.GONE);
                            homeCard.setVisibility(View.GONE);
                        } else if (valueGett == 0) {
                            serviceNotMsg.setVisibility(View.VISIBLE);
                            homeCard.setVisibility(View.VISIBLE);
                        }
                    }
                } else  {
                    serviceNotMsg.setVisibility(View.VISIBLE);
                    homeCard.setVisibility(View.VISIBLE);
                }

                createNewJobAdapter.notifyDataSetChanged();


            }
        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }
}
