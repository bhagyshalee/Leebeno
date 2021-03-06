package leebeno.com.leebeno.CustomerPart.Fragments.CategoryFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.CustomerPart.Adapter.OngoingJobAdapter;
import leebeno.com.leebeno.CustomerPart.Fragments.HomeCustomerFragment;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.AdminPart.HomeAdminActivity.dateSearchAdmin;
import static leebeno.com.leebeno.CustomerPart.HomeCustomerActivity.getUtcToLocal;


public class CurrentFragment extends Fragment implements WebCompleteTask {

    @Bind(R.id.recyclerCurrentJob)
    RecyclerView recyclerCurrentJob;
    OngoingJobAdapter createNewJobAdapter;
    @Bind(R.id.editSearchOngoing)
    EditText editSearchOngoing;
    @Bind(R.id.dateSearch)
    TextView dateSearch;
    @Bind(R.id.textNotAvailable)
    TextView textNotAvailable;
    static int valueGett=0;

    List<JobGetterSetter> getPostedJobs;
    SwipeRefreshLayout setOnRefreshListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_current, container, false);
        ButterKnife.bind(this, v);
        getPostedJobs = new ArrayList<>();


        recyclerCurrentJob.setHasFixedSize(true);
        recyclerCurrentJob.setNestedScrollingEnabled(true);
        recyclerCurrentJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerCurrentJob.addItemDecoration(itemDecor);



//         dateSearch.setVisibility(View.VISIBLE);
//            editSearchOngoing.setVisibility(View.VISIBLE);

        dateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePick(getActivity(),dateSearch);
            }
        });
        createNewJobAdapter = new OngoingJobAdapter(getPostedJobs, getActivity());
        recyclerCurrentJob.setAdapter(createNewJobAdapter);

        setOnRefreshListener = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dateSearch.setText(null);
                dateSearch.setHint(getResources().getString(R.string.date));
                setOnRefreshListener.setRefreshing(true);
//                dateSearch.setText(messages[new java.util.Random().nextInt(messages.length)]);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setOnRefreshListener.setRefreshing(false);
                        getCustomerOngoing("");


                    }
                }, 2000);
            }
        });

        getCustomerOngoing("");
//            HashMap hashMap = new HashMap();
//            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getCustomerAllJob
//                    , hashMap, CurrentFragment.this, RequestCode.CODE_GetCustomerAllJobs, 0);


//        Log.e("vsdbjksbdv",providerId+"");


//            dateSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setDatePick(getActivity(), dateSearch);
//            }
//        });
        editSearchOngoing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = editSearchOngoing.getText().toString().toLowerCase(Locale.getDefault());
                createNewJobAdapter.getFilter().filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return v;
    }

    public void setDatePick(Context context, final TextView et) {
        int mYear;
        int mMonth;
        int mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
//                        et.setText((monthOfYear + 1) + "/" +dayOfMonth  +  "/" + year);
                        String deliveryDate = (monthOfYear + 1) + "-" + (dayOfMonth) + "-" + year;
                        et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                        HomeCustomerFragment.getInstance().CustomerGetAllJob(getUtcToLocal(deliveryDate),"",0.0,0.0,0.0,null,null);

                        getCustomerOngoing(getUtcToLocal(deliveryDate));
                    }

                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et.setText("");
                et.setHint("Date");
                getCustomerOngoing("");
            }
        });
        datePickerDialog.show();
    }

//    public static String getUtcToLocal(String date){
//        String readReminderdate=null;
//        try{
//            DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//            df.setTimeZone(TimeZone.getTimeZone("UTC"));
//            DateFormat target_date = new SimpleDateFormat("MMM/dd/yyyy", Locale.ENGLISH);
//            Calendar cal = Calendar.getInstance();
//            TimeZone tz = cal.getTimeZone();
//            target_date.setTimeZone(tz);
//            Date startDate = df.parse(date);
//            readReminderdate = target_date.format(startDate);
//        }catch (Exception e){
//
//        }
//        return readReminderdate;
//    }

    public void getCustomerOngoing(String searching){
        HashMap hashMap = new HashMap();
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.getCustomerAllJob+"sDate="+searching
                    , hashMap, CurrentFragment.this, RequestCode.CODE_GetCustomerAllJobs, 3);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCustomerOngoing("");
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


                if (jsonArrayData != null && jsonArrayData.length() > 0) {
                    Log.e("NormalFragment", "" + jsonObjSuccess);
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JobGetterSetter jobGetterSetter = new JobGetterSetter();
                        if (jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.ongoingStatus)) == 0 ||
                                jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.amountpendingStatus))==0 ||
                                        jsonArrayData.getJSONObject(i).getString("status").compareTo(getResources().getString(R.string.satisfiedStatus)) == 0) {
                            jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getString("jobTitle"));
                            jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("description"));
                            jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getString("startDate"));
                            jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getInt("bidCount"));
                            jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));
                            jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
                            jobGetterSetter.setStatus(jsonArrayData.getJSONObject(i).getString("status"));
                             // jobGetterSetter.setBidders(jsonArrayData.getJSONObject(i).getJSONArray("bidders"));

                           /* jobGetterSetter.setPaymentType(jsonArrayData.getJSONObject(i).getString("paymentType"));
                            if (jsonArrayData.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                                jobGetterSetter.setMin(jsonArrayData.getJSONObject(i).getInt("min"));
                                jobGetterSetter.setMax(jsonArrayData.getJSONObject(i).getInt("max"));
                            } else {
                                jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("amount"));
                            }*/
                            jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("finalAmount"));

                            if (jsonArrayData.optJSONObject(i).optJSONObject("bid")!=null) {
                                jobGetterSetter.setBidAmount(jsonArrayData.optJSONObject(i).optJSONObject("bid").optInt("cost"));
                            }

                            if (jsonArrayData.optJSONObject(i).optJSONObject("applier")!=null){
                                jobGetterSetter.setApplierName(jsonArrayData.optJSONObject(i).optJSONObject("applier").optString("fullName"));
                            }

                            valueGett=1;
                            getPostedJobs.add(jobGetterSetter);
                            textNotAvailable.setVisibility(View.GONE);

                        } else if (valueGett==0){
                            textNotAvailable.setVisibility(View.VISIBLE);
                        }

                    }
                } else if (valueGett==0){
                    textNotAvailable.setVisibility(View.VISIBLE);
                }
                createNewJobAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }
}
