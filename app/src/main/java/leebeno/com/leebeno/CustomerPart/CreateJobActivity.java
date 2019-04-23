package leebeno.com.leebeno.CustomerPart;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.AddSkillAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.MySpinner;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Signup;

import static leebeno.com.leebeno.Api.RequestCode.AUTOPLACE_CODE;
import static leebeno.com.leebeno.Api.RequestCode.CODE_createJob;
import static leebeno.com.leebeno.Services.Utility.getDateToUtc;
import static leebeno.com.leebeno.Services.Utility.setDatePickerHidePreviousET;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class CreateJobActivity extends AppCompatActivity implements View.OnClickListener, WebCompleteTask {


    public String strJobHours;
    @Bind(R.id.etSkillsLabour)
    MySpinner etSkillsLabour;
    List arrayListSkill;
    @Bind(R.id.recyclerSkillJob)
    RecyclerView recyclerSkillJob;
    List<String> listSelectSkill;
    ArrayList<String> listSelectSkillId;
    AddSkillAdapter addSkillAdapter;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.etJobTitle)
    EditText etJobTitle;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.etJobHour)
    EditText etJobHour;
    @Bind(R.id.rangeSeekbar)
    CrystalRangeSeekbar crystalRangeSeekbar;
    @Bind(R.id.etMinValue)
    EditText etMinValue;
    @Bind(R.id.etMaxValue)
    EditText etMaxValue;
    @Bind(R.id.layoutRangeBar)
    LinearLayout layoutRangeBar;
    @Bind(R.id.radioGroupPayment)
    RadioGroup radioGroupPayment;
    @Bind(R.id.etPayPerHour)
    EditText etPayPerHour;
    @Bind(R.id.etFixAmount)
    EditText etFixAmount;
    @Bind(R.id.etStartDate)
    EditText etStartDate;
    @Bind(R.id.etNoLabour)
    EditText etNoLabour;
    @Bind(R.id.etYourLocation)
    EditText etYourLocation;
    @Bind(R.id.etStreetCity)
    EditText etStreetCity;
    double latitude, longitude;
    String paymentTypeString;
    List<String> listSkillsId = new ArrayList<>();
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        listSelectSkill = new ArrayList<>();
        listSelectSkillId = new ArrayList<>();
        ButterKnife.bind(this);
        recyclerSkillJob.setHasFixedSize(true);
        recyclerSkillJob.setNestedScrollingEnabled(true);
        recyclerSkillJob.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        backSignUp.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etYourLocation.setOnClickListener(this);

        paymentTypeString = getResources().getString(R.string.pay_per_hour);

/*
        etJobHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                strJobHours= etJobHour.getText().toString();
                etJobHour.setText(strJobHours + "  days");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
*/

        radioGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int getRadioId = radioGroupPayment.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(getRadioId);
                String getRadioText = radioButton.getText().toString();
                if (getRadioText.equals(getResources().getString(R.string.pay_per_hour))) {
                    layoutRangeBar.setVisibility(View.GONE);
                    etPayPerHour.setVisibility(View.VISIBLE);
                    etFixAmount.setVisibility(View.GONE);
                    paymentTypeString = getResources().getString(R.string.pay_per_hour);
                }
                if (getRadioText.equals(getResources().getString(R.string.job_pay_range))) {
                    layoutRangeBar.setVisibility(View.VISIBLE);
                    etPayPerHour.setVisibility(View.GONE);
                    etFixAmount.setVisibility(View.GONE);
                    paymentTypeString = getResources().getString(R.string.job_pay_range);
                }
                if (getRadioText.equals(getResources().getString(R.string.fixed_amount))) {
                    layoutRangeBar.setVisibility(View.GONE);
                    etPayPerHour.setVisibility(View.GONE);
                    etFixAmount.setVisibility(View.VISIBLE);
                    paymentTypeString = getResources().getString(R.string.fixed_amount);
                }

            }
        });

        crystalRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                etMinValue.setText("$    " + String.valueOf(minValue));
                etMaxValue.setText("$    " + String.valueOf(maxValue));
            }
        });


        getAllSkills();

        etSkillsLabour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String selectedItem = adapterView.getItemAtPosition(position).toString();
                String selectedItemS = etSkillsLabour.getSelectedItem().toString();
                if (selectedItemS.equals(getResources().getString(R.string.select_skills))) {
                    // do your stuff
                } else {
                    if (listSelectSkill.size() == 0) {
                        listSelectSkill.add(selectedItemS);
                        listSelectSkillId.add(listSkillsId.get(position));
                        addSkillAdapter = new AddSkillAdapter(listSelectSkillId, listSelectSkill, CreateJobActivity.this, recyclerSkillJob, etSkillsLabour);
                        recyclerSkillJob.setAdapter(addSkillAdapter);
                        recyclerSkillJob.setVisibility(View.VISIBLE);
                    } else {

                        if (listSelectSkill.contains(selectedItemS)) {
                            if (Signup.cl == false) {
                                showToast(CreateJobActivity.this, getResources().getString(R.string.select_already_select_skills));

                            }

                            //showToast(CreateJobActivity.this, getResources().getString(R.string.select_already_select_skills));

                        } else {

                            listSelectSkill.add(selectedItemS);
                            listSelectSkillId.add(listSkillsId.get(position));
                            addSkillAdapter = new AddSkillAdapter(listSelectSkillId, listSelectSkill, CreateJobActivity.this, recyclerSkillJob, etSkillsLabour);
                            recyclerSkillJob.setAdapter(addSkillAdapter);
                            recyclerSkillJob.setVisibility(View.VISIBLE);

                        }
                        Signup.cl = false;
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getAllSkills() {
        HashMap hashMap = new HashMap();
        new WebTask(CreateJobActivity.this, WebUrls.BASE_URL + WebUrls.getAllSkills_api
                , hashMap, CreateJobActivity.this, RequestCode.CODE_getskills, 0);

    }

    public void findIndexes(String longerStr, String word) {
       /* String searchableString = "don’t be evil.being evil is bad";
        String keyword = "be";*/

        int index = longerStr.indexOf(word);
        if (index >= 0) {
            Log.e("Yess", "" + index);
            String[] splited = etJobHour.getText().toString().split("\\s+");
            strJobHours = splited[0];
        } else {
            strJobHours = etJobHour.getText().toString();
            if (!strJobHours.equals("")) {
                etJobHour.setText(strJobHours + " days");
            }
            Log.e("No", "ghg " + strJobHours);
        }
      /*  while (index >=0){
            System.out.println("Index : "+index);
            index = longerStr.indexOf(word, index+word.length());
            Log.e("dfmbdffbd",""+index);
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOPLACE_CODE) {
            if (data != null) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                if (place != null) {
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    etYourLocation.setText(place.getAddress());
                }

            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view == etYourLocation) {
            findIndexes(etJobHour.getText().toString(), "days");
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .build(this);
                startActivityForResult(intent, AUTOPLACE_CODE);
            } catch (Exception e) {
                Log.e("fgbryhyhy", "" + e);
            }
        }
        if (view == etStartDate) {

            findIndexes(etJobHour.getText().toString(), "days");

            setDatePickerHidePreviousET(CreateJobActivity.this, etStartDate);
            //Log.e("fgbryhyhyffgdr", "" + getTimeToUtc("1/12/2018"));

        }
        if (view == backSignUp) {
            finish();
        }
        if (view == btnSubmit) {
            if (etJobTitle.getText().toString().trim().isEmpty()) {
                etJobTitle.setError(getResources().getString(R.string.errorJobTitle));
                etJobTitle.requestFocus();
            } else if (etDescription.getText().toString().trim().isEmpty()) {
                etDescription.setError(getResources().getString(R.string.errorDescription));
                etDescription.requestFocus();
            } else if (listSelectSkill.size() == 0) {
                showToast(CreateJobActivity.this, getResources().getString(R.string.errorSkills));
            } else if (etJobHour.getText().toString().trim().isEmpty()) {
                etJobHour.setError(getResources().getString(R.string.errorJobDays));
                etJobHour.requestFocus();
            } else if (etStartDate.getText().toString().trim().isEmpty()) {
                etStartDate.setError(getResources().getString(R.string.errorStartDate));
                etStartDate.requestFocus();
                showToast(CreateJobActivity.this, getResources().getString(R.string.errorStartDate));
            } else if (etNoLabour.getText().toString().trim().isEmpty()) {
                etNoLabour.setError(getResources().getString(R.string.errorNoLabours));
                etNoLabour.requestFocus();
            }else if (etNoLabour.getText().toString().compareTo("0")==0) {
                etNoLabour.setError(getResources().getString(R.string.errorNoLaboursValid));
                etNoLabour.requestFocus();
            } else if (etYourLocation.getText().toString().trim().isEmpty()) {
                etYourLocation.setError(getResources().getString(R.string.errorLocation));
                etYourLocation.requestFocus();
                showToast(CreateJobActivity.this, getResources().getString(R.string.errorAddress));
            } else if (etStreetCity.getText().toString().trim().isEmpty()) {
                etStreetCity.setError(getResources().getString(R.string.errorStreet));
                etStreetCity.requestFocus();
            } else {
                try {
                    final Dialog dialog = new Dialog(CreateJobActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.show_msg_trans);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                    final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                    dialog.show();
                    btnSubmit.setText(getResources().getString(R.string.accept));
                    btnCancel.setText(getResources().getString(R.string.reject));

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                          /*  Intent intent = new Intent();
                            intent.putExtra("title", etJobTitle.getText().toString());
                            intent.putExtra("description", etDescription.getText().toString());
                            intent.putExtra("hour", etJobHour.getText().toString());
                            setResult(99, intent);
                            finish();*/
                          /*  double  amtMin = 0, amtMax = 0;
                            try {
                                amtMin = Double.parseDouble(etMinValue.getText().toString().trim().replaceAll("\\$", "").trim());
                                amtMax = Double.parseDouble(etMaxValue.getText().toString().trim().replaceAll("\\$", "").trim());
                            } catch (Exception e) {
                                Log.e("rdgerff", "" + e);
                            }*/

                            if (paymentTypeString.compareTo(getResources().getString(R.string.pay_per_hour)) == 0 && etPayPerHour.getText().toString().trim().isEmpty()) {
                                etPayPerHour.setError(getResources().getString(R.string.errorPPH));
                                etPayPerHour.requestFocus();

                            } else if (paymentTypeString.compareTo(getResources().getString(R.string.fixed_amount)) == 0 && etFixAmount.getText().toString().trim().isEmpty()) {
                                etFixAmount.setError(getResources().getString(R.string.enter_fix_amount_for_job_biding));
                                etFixAmount.requestFocus();
                            } /*else  if (paymentTypeString.compareTo(getResources().getString(R.string.job_pay_range)) == 0 && amtMin>amtMax) {
                              // showToast(CreateJobActivity.this,);

                            }*/ else {

                                createJob();

                            }


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

        }

    }

    private void createJob() {
        String getTimeVal = getOtherFormate(etStartDate.getText().toString().trim());

       /* SimpleDateFormat outputFormatt = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        String formattedDatee = "";
           Date myDate = null;
            try {
                myDate = outputFormatt.parse(etStartDate.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            outputFormatt.setTimeZone(TimeZone.getDefault());// Set device time zone
        String getTimeVal;
        getTimeVal = outputFormatt.format(myDate);*/


            double jobHours = 0, noOfLab = 0, amounFix = 0, amounPph = 0, amtMin = 0, amtMax = 0;
        String minValueStr = "", maxValueStr = "";
        minValueStr = etMinValue.getText().toString().trim().replaceAll("\\$", "");
        maxValueStr = etMaxValue.getText().toString().trim().replaceAll("\\$", "");
        minValueStr = minValueStr.trim();
        maxValueStr = maxValueStr.trim();
        try {
            jobHours = Double.parseDouble(strJobHours);
            noOfLab = Double.parseDouble(etNoLabour.getText().toString().trim());

        } catch (NumberFormatException e) {
            Log.e("rdgerff", "" + e);
        }
        Log.e("fgbrbreredffb", minValueStr + " " + maxValueStr + " " + amtMin + " " + amtMax);

        JSONObject subObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        JSONObject latlong = new JSONObject();
        try {
            latlong.put("lat", latitude);
            latlong.put("lng", longitude);
            subObject.put("value", "" + etYourLocation.getText().toString().trim());
            subObject.put("location", latlong);
            subObject.put("street", "" + etStreetCity.getText().toString().trim());
            //jsonObject.put("address", subObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray jsonArray = new JSONArray();


        for (int i = 0; i < listSelectSkillId.size(); i++) {
            jsonArray.put(listSelectSkillId.get(i));
        }

        JSONObject objectNew = new JSONObject();

        try {
            objectNew.put("skillIds", jsonArray);
            objectNew.put("jobTitle", "" + etJobTitle.getText().toString().trim());
            objectNew.put("jobHours", jobHours);
            objectNew.put("startDate", getTimeVal);
            objectNew.put("noOfLabours", noOfLab);
            objectNew.put("address", subObject);
            objectNew.put("description", etDescription.getText().toString().trim());

            if (paymentTypeString.compareTo(getResources().getString(R.string.pay_per_hour)) == 0) {
                try {
                    amounPph = Double.parseDouble(etPayPerHour.getText().toString().trim());
                } catch (Exception e) {
                    Log.e("rdgerff", "" + e);
                }

                objectNew.put("paymentType", getResources().getString(R.string.pph));
                objectNew.put("amount", amounPph);
            }
            if (paymentTypeString.compareTo(getResources().getString(R.string.job_pay_range)) == 0) {
                try {
                    amtMin = Double.parseDouble(etMinValue.getText().toString().trim().replaceAll("\\$", "").trim());
                    amtMax = Double.parseDouble(etMaxValue.getText().toString().trim().replaceAll("\\$", "").trim());
                } catch (Exception e) {
                    Log.e("rdgerff", "" + e);
                }

                objectNew.put("paymentType", getResources().getString(R.string.range));
                objectNew.put("min", amtMin);
                objectNew.put("max", amtMax);
            }

            if (paymentTypeString.compareTo(getResources().getString(R.string.fixed_amount)) == 0) {
                try {
                    amounFix = Double.parseDouble(etFixAmount.getText().toString().trim());
                } catch (Exception e) {
                    Log.e("rdgerff", "" + e);
                }

                objectNew.put("paymentType", getResources().getString(R.string.fix));
                objectNew.put("amount", amounFix);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("gdrgrgyuiywg", "" + objectNew);

        new WebTask(CreateJobActivity.this, WebUrls.BASE_URL + WebUrls.getCreateJob
                , objectNew, CreateJobActivity.this, CODE_createJob, 1);
    }



    public static String getOtherFormate(String utcDate) {
          SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        SimpleDateFormat outputFormatt = new SimpleDateFormat("d/M/yyyy", Locale.ENGLISH);
        String formattedDatee = "";
        try {
//            TimeZone utcZone = TimeZone.getTimeZone("GMT");
            inputFormat.setTimeZone(TimeZone.getDefault());// Set UTC time zone
            //outputFormatt.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = outputFormatt.parse(utcDate);
            outputFormatt.setTimeZone(TimeZone.getDefault());// Set device time zone
            String strDate = "";
            strDate = outputFormatt.format(myDate);
            Log.e("fdvdvdffdfgreg", "" + strDate);
            Date datee = null;
            try {
                datee = outputFormatt.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = inputFormat.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sgerthrthg", "" + e);
        }
        return formattedDatee;
    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_getskills) {
                Log.d("Createjob_res", response);

                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONArray jsonArray = jsonObjSuccess.getJSONArray("data");
                //JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                Signup.listSkillsName = new ArrayList<>();

                Signup.listSkillsName.add(0, getResources().getString(R.string.select_skills));
                listSkillsId.add(0, "0");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Signup.listSkillsName.add(jsonArray.getJSONObject(i).optString("name"));
                    listSkillsId.add(jsonArray.getJSONObject(i).optString("id"));
                    // Log.e("vdregrehdhrty",jsonArray.getJSONObject(i).optString("name")+"  "+listSkillsName.get(i));
                }


                //arrayListSkill = new ArrayList<>(Arrays.asList(listSkillsName));
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateJobActivity.this, R.layout.design_spinner, R.id.textSpinner, Signup.listSkillsName) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(R.id.textSpinner);
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextSize(15);
                            // tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                            tv.setPadding(15, 15, 15, 15);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                            tv.setPadding(15, 15, 15, 15);
                            //tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                etSkillsLabour.setAdapter(spinnerArrayAdapter);
            }
            if (taskcode == CODE_createJob) {
                Log.e("responseSS", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                    showToast(CreateJobActivity.this, "" + jsonMsg.getString("replyMessage"));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
    }
}
