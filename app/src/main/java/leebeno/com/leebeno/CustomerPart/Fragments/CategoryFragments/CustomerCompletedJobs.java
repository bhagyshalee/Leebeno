package leebeno.com.leebeno.CustomerPart.Fragments.CategoryFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import leebeno.com.leebeno.CustomerPart.Adapter.CreateOldJobAdapter;
import leebeno.com.leebeno.CustomerPart.Adapter.CustomerCompleteJobAdapter;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.BidPlace.providerId;
import static leebeno.com.leebeno.Services.Utility.setDatePickerIn;

public class CustomerCompletedJobs extends Fragment implements WebCompleteTask {

    static int valueGett = 0;
    CustomerCompleteJobAdapter createNewJobAdapter;
    @Bind(R.id.recyclerCurrentJob)
    RecyclerView recyclerCurrentJob;
    @Bind(R.id.textNotAvailable)
    TextView textNotAvailable;
    List<JobGetterSetter> getPostedJobs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        ButterKnife.bind(this, v);

        getPostedJobs = new ArrayList<>();
        recyclerCurrentJob.setHasFixedSize(true);
        recyclerCurrentJob.setNestedScrollingEnabled(true);
        recyclerCurrentJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerCurrentJob.addItemDecoration(itemDecor);


        HashMap hashMap = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + providerId,
                hashMap, CustomerCompletedJobs.this, RequestCode.CODE_getProfileById, 3);


        return v;
    }


 /*   @Override
    public void onResume() {
        super.onResume();

        HashMap hashMap = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + providerId,
                hashMap, CustomerCompletedJobs.this, RequestCode.CODE_getProfileById, 3);


    }*/

    @Override
    public void onComplete(String response, int taskcode) {
        try {

            if (taskcode == RequestCode.CODE_getProfileById) {
                getPostedJobs.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonArrayDatadata = jsonObjSuccess.getJSONObject("data");
                JSONArray jsonArrayData = jsonArrayDatadata.getJSONArray("jobs");
                if (jsonArrayData != null && jsonArrayData.length() > 0) {

                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        try
                        {
                            JobGetterSetter jobGetterSetter = new JobGetterSetter();
                            if (jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.completedStatus)) == 0) {
                                Log.e("CustomerCompletedJobs", "" + jsonObject);
                                jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getString("jobTitle"));
                                jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("description"));
                                jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getString("startDate"));
                                jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getInt("bidCount"));
                                jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));
                                jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
//                            jobGetterSetter.setApplierName(jsonArrayData.getJSONObject(i).getJSONObject("applier").getString("fullName"));
//                            jobGetterSetter.setBidAmount(jsonArrayData.getJSONObject(i).getJSONObject("bid").getInt("cost"));
                                jobGetterSetter.setCompletedDate(jsonArrayData.getJSONObject(i).getString("completedAt"));
                                jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("finalAmount"));

//                                jobGetterSetter.setPaymentType(jsonArrayData.getJSONObject(i).getString("paymentType"));
                               /* if (jsonArrayData.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                    jobGetterSetter.setMin(jsonArrayData.getJSONObject(i).getInt("min"));
                                    jobGetterSetter.setMax(jsonArrayData.getJSONObject(i).getInt("max"));
                                } else {
                                    jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("amount"));
                                }*/

                                valueGett=1;
                                getPostedJobs.add(jobGetterSetter);
                                textNotAvailable.setVisibility(View.GONE);
                                createNewJobAdapter = new CustomerCompleteJobAdapter(getPostedJobs, getActivity());
                                recyclerCurrentJob.setAdapter(createNewJobAdapter);

                            } else if (valueGett==0){
                                textNotAvailable.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Log.e("exceptionGet", "" + e);
                        }


                    }
                } else if (valueGett==0){
                    textNotAvailable.setVisibility(View.VISIBLE);
                }



            }
        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }
}
