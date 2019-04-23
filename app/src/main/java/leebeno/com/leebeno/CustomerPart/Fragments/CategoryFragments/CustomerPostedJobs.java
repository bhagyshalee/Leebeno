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
import leebeno.com.leebeno.AdminPart.Adapters.FeedJobAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.SpleshScreen;

import static leebeno.com.leebeno.BidPlace.providerId;
import static leebeno.com.leebeno.Services.Utility.setDatePickerIn;

public class CustomerPostedJobs extends Fragment implements WebCompleteTask {

    static int valueGett = 0;
    @Bind(R.id.recyclerCurrentJob)
    RecyclerView recyclerCurrentJob;

    FeedJobAdapter createNewJobAdapter;
    @Bind(R.id.textNotAvailable)
    TextView textNotAvailable;
    List<JobGetterSetter> getPostedJobs;
    List<String> jobSkills;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        ButterKnife.bind(this, v);


        getPostedJobs = new ArrayList<>();
        jobSkills = new ArrayList<>();


        recyclerCurrentJob.setHasFixedSize(true);
        recyclerCurrentJob.setNestedScrollingEnabled(true);
        recyclerCurrentJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerCurrentJob.addItemDecoration(itemDecor);


        HashMap hashMap = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + providerId,
                hashMap, CustomerPostedJobs.this, RequestCode.CODE_getProfileById, 3);


        Log.e("vsdbjksbdv", providerId + "  "+SharedPrefManager.getAccessToken(getActivity(), Constrants.AccessToken));



        return v;
    }


  /*  @Override
    public void onResume() {
        super.onResume();

        HashMap hashMap = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + providerId,
                hashMap, CustomerPostedJobs.this, RequestCode.CODE_getProfileById, 3);

    }*/

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_getProfileById) {
                getPostedJobs.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonArrayData = jsonObjSuccess.getJSONObject("data");

                JSONArray jsonjobs = jsonArrayData.getJSONArray("jobs");
                if (jsonjobs != null && jsonjobs.length() > 0) {

                    for (int i = 0; i < jsonjobs.length(); i++) {
                        JobGetterSetter jobGetterSetter = new JobGetterSetter();
                        for (int k = 0; k < jsonjobs.getJSONObject(i).getJSONArray("skillIds").length(); k++) {
                            jobSkills.add(jsonjobs.getJSONObject(i).getJSONArray("skillIds").optString(k));
                        }



                      /*  Set<String> set = shared.getStringSet("DATE_LIST", null);
                        arrPackage.addAll(set);
                        Log.d("retrivesharedPreferences",""+set);
*/
                        List<String> skillsLabour=new ArrayList<>();

                        skillsLabour=SharedPrefManager.getLabourSkills(getActivity(), Constrants.LabourSkill);
                        Log.e("JobSkillDD", ""+skillsLabour.size());
                        if (skillsLabour.contains(jobSkills.get(i))) {
                            if (SharedPrefManager.getUserStatus(getActivity(), Constrants.UserStatus).compareTo("labour") == 0)
                            {
                                if (jsonjobs.getJSONObject(i).getInt("noOfLabours")==1)
                                {
                                    Log.d("PostedJobsCustoemrLabo",""+jsonObject);
                                    if (jsonjobs.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.postedStatus)) == 0) {
                                        jobGetterSetter.setJobTitle(jsonjobs.getJSONObject(i).getString("jobTitle"));
                                        jobGetterSetter.setDescription(jsonjobs.getJSONObject(i).getString("description"));
                                        jobGetterSetter.setStartDate(jsonjobs.getJSONObject(i).getString("startDate"));
                                        jobGetterSetter.setBidCount(jsonjobs.getJSONObject(i).getInt("bidCount"));
                                        jobGetterSetter.setCreatedJob(jsonjobs.getJSONObject(i).getString("createdAt"));
                                        jobGetterSetter.setId(jsonjobs.getJSONObject(i).getString("id"));
                                        jobGetterSetter.setStatus(jsonjobs.getJSONObject(i).getString("status"));

                                        JSONArray jsonArrayBidders = jsonjobs.getJSONObject(i).getJSONArray("bidders");

                                        for (int j = 0; j < jsonArrayBidders.length(); j++) {
                                            jobGetterSetter.setBidders(jsonArrayBidders.optString(j));
                                        }

//                                jobGetterSetter.setApplierName(jsonjobs.getJSONObject(i).getJSONObject("applier").getString("fullName"));
                                        if (jsonjobs.optJSONObject(i).optJSONObject("myBid") != null) {
                                            jobGetterSetter.setBidStatus(jsonjobs.optJSONObject(i).optJSONObject("myBid").optString("bidStatus"));
                                        }
                                        jobGetterSetter.setPaymentType(jsonjobs.getJSONObject(i).getString("paymentType"));
                                        if (jsonjobs.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                            jobGetterSetter.setMin(jsonjobs.getJSONObject(i).getInt("min"));
                                            jobGetterSetter.setMax(jsonjobs.getJSONObject(i).getInt("max"));
                                        } else {
                                            jobGetterSetter.setAmount(jsonjobs.getJSONObject(i).getInt("amount"));
                                        }
                                        valueGett = 1;
                                        getPostedJobs.add(jobGetterSetter);
                                        textNotAvailable.setVisibility(View.GONE);
                                        createNewJobAdapter = new FeedJobAdapter(getPostedJobs, getActivity());
                                        recyclerCurrentJob.setAdapter(createNewJobAdapter);

                                    }

                                }else if (valueGett == 0) {
                                    textNotAvailable.setVisibility(View.VISIBLE);
                                }
                            }else
                            {
                                if (jsonjobs.getJSONObject(i).getInt("noOfLabours")!=1)
                                {

                                    Log.d("PostedJobsCustoemr",""+jsonObject);
                                    if (jsonjobs.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.postedStatus)) == 0) {
                                        jobGetterSetter.setJobTitle(jsonjobs.getJSONObject(i).getString("jobTitle"));
                                        jobGetterSetter.setDescription(jsonjobs.getJSONObject(i).getString("description"));
                                        jobGetterSetter.setStartDate(jsonjobs.getJSONObject(i).getString("startDate"));
                                        jobGetterSetter.setBidCount(jsonjobs.getJSONObject(i).getInt("bidCount"));
                                        jobGetterSetter.setCreatedJob(jsonjobs.getJSONObject(i).getString("createdAt"));
                                        jobGetterSetter.setId(jsonjobs.getJSONObject(i).getString("id"));
                                        jobGetterSetter.setStatus(jsonjobs.getJSONObject(i).getString("status"));

                                        JSONArray jsonArrayBidders = jsonjobs.getJSONObject(i).getJSONArray("bidders");

                                        for (int j = 0; j < jsonArrayBidders.length(); j++) {
                                            jobGetterSetter.setBidders(jsonArrayBidders.optString(j));
                                        }

//                                jobGetterSetter.setApplierName(jsonjobs.getJSONObject(i).getJSONObject("applier").getString("fullName"));
                                        if (jsonjobs.optJSONObject(i).optJSONObject("myBid") != null) {
                                            jobGetterSetter.setBidStatus(jsonjobs.optJSONObject(i).optJSONObject("myBid").optString("bidStatus"));
                                        }
                                        jobGetterSetter.setPaymentType(jsonjobs.getJSONObject(i).getString("paymentType"));
                                        if (jsonjobs.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                            jobGetterSetter.setMin(jsonjobs.getJSONObject(i).getInt("min"));
                                            jobGetterSetter.setMax(jsonjobs.getJSONObject(i).getInt("max"));
                                        } else {
                                            jobGetterSetter.setAmount(jsonjobs.getJSONObject(i).getInt("finalAmount"));
                                        }
                                        valueGett = 1;
                                        getPostedJobs.add(jobGetterSetter);
                                        textNotAvailable.setVisibility(View.GONE);
                                        createNewJobAdapter = new FeedJobAdapter(getPostedJobs, getActivity());
                                        recyclerCurrentJob.setAdapter(createNewJobAdapter);

                                    }

                                }else if (valueGett == 0) {
                                    textNotAvailable.setVisibility(View.VISIBLE);
                                }

                            }
                            Log.e("JobSkill", skillsLabour.toString());
                        } else if (valueGett == 0) {
                            textNotAvailable.setVisibility(View.VISIBLE);
                        }

                    }
                } else if (valueGett == 0) {
                    textNotAvailable.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            Log.e("customerPostedJobsEx", "" + e);
        }

    }
}

