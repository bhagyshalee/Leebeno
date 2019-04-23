package leebeno.com.leebeno.CustomerPart.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.CustomerPart.Adapter.CreateNewJobAdapter;
import leebeno.com.leebeno.CustomerPart.CreateJobActivity;

import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;


public class HomeCustomerFragment extends Fragment implements View.OnClickListener, WebCompleteTask {

    @Bind(R.id.recyclerJobs)
    RecyclerView recyclerJobs;
    @Bind(R.id.fabIcon)
    FloatingActionButton fabIcon;
    public CreateNewJobAdapter createNewJobAdapter;
    List<JobGetterSetter> getPostedJobs;
    @Bind(R.id.serviceNotMsg)
    TextView serviceNotMsg;
    @Bind(R.id.homeCard)
    CardView homeCard;
    static int valueGett=0;
    SwipeRefreshLayout setOnRefreshListener;
    public static HomeCustomerFragment mInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_customer, container, false);
        ButterKnife.bind(this, v);


        getPostedJobs = new ArrayList<>();
        mInstance = this;
        recyclerJobs.setHasFixedSize(true);
        fabIcon.setOnClickListener(this);
        recyclerJobs.setNestedScrollingEnabled(true);
        recyclerJobs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        serviceNotMsg.setVisibility(View.VISIBLE);
        createNewJobAdapter = new CreateNewJobAdapter(getPostedJobs, getActivity());
        recyclerJobs.setAdapter(createNewJobAdapter);

        setOnRefreshListener = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setOnRefreshListener.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CustomerGetAllJob("", "", 0.0, 0.0, 0.0, null, null);
                        setOnRefreshListener.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        CustomerGetAllJob("", "", 0.0, 0.0, 0.0, null, null);


        return v;
    }

    public static HomeCustomerFragment getInstance(){
        return mInstance;
    }

    public void CustomerGetAllJob(String searching_text, String paymentType, double amount, double minAmt, double maxAmt, JSONArray jobSkill, JSONObject loc){
        HashMap hashMap = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getCustomerAllJob+ "sDate=" + searching_text + "&" + "paymentType=" +
                paymentType + "&" + "amount=" + amount + "&" + "minAmt=" + minAmt + "&" + "maxAmt=" + maxAmt + "&" + "jobSkill=" + jobSkill
                + "&" + "loc=" + loc, hashMap,HomeCustomerFragment.this, RequestCode.CODE_GetCustomerAllJobs, 0);
    }


    @Override
    public void onResume() {
        super.onResume();
        CustomerGetAllJob("", "", 0.0, 0.0, 0.0, null, null);

    }

    @Override
    public void onClick(View view) {
       /* if (view==humburgerIcon)
        {
            drawer.openDrawer(Gravity.LEFT);
        }*/
        if (view == fabIcon) {

            Intent intent = new Intent(getActivity(), CreateJobActivity.class);
            startActivity(intent);

        }
    }


    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_GetCustomerAllJobs) {
                getPostedJobs.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONArray jsonArrayData = jsonObjSuccess.getJSONArray("data");
                //JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");


                if (jsonArrayData!=null && jsonArrayData.length()>0){
                    Log.e("jookokokok", "" + jsonObjSuccess);
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JobGetterSetter jobGetterSetter = new JobGetterSetter();
                        if (jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.postedStatus))==0) {
                            jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getString("jobTitle"));
                            jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("description"));
                            jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getString("startDate"));
                            jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getInt("bidCount"));
                            jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));
                            jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
                            // jobGetterSetter.setBidders(jsonArrayData.getJSONObject(i).getJSONArray("bidders"));

                            jobGetterSetter.setPaymentType(jsonArrayData.getJSONObject(i).getString("paymentType"));
                            if (jsonArrayData.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                jobGetterSetter.setMin(jsonArrayData.getJSONObject(i).getInt("min"));
                                jobGetterSetter.setMax(jsonArrayData.getJSONObject(i).getInt("max"));
                            } else {
                                jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("amount"));
                            }
                            valueGett=1;
                            getPostedJobs.add(jobGetterSetter);
                            serviceNotMsg.setVisibility(View.GONE);
                            homeCard.setVisibility(View.GONE);
                        }else  if (valueGett==0){
                            serviceNotMsg.setVisibility(View.VISIBLE);
                            homeCard.setVisibility(View.VISIBLE);
                        }
                    }
                }else  {
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
