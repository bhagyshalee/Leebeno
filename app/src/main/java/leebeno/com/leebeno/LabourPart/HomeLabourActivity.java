package leebeno.com.leebeno.LabourPart;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.JsonObject;
import com.googlecode.mp4parser.authoring.Edit;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
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
import de.hdodenhof.circleimageview.CircleImageView;
import leebeno.com.leebeno.AchieveBids;
import leebeno.com.leebeno.Adapters.AddSkillAdapter;
import leebeno.com.leebeno.AdminPart.Fragments.BidAdminFragment;
import leebeno.com.leebeno.AdminPart.Fragments.HomeAdminFragment;
import leebeno.com.leebeno.AdminPart.HomeAdminActivity;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.MySpinner;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.Fragments.SettingCustomerFragment;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.EscalatedJobs;
import leebeno.com.leebeno.LabourPart.Fragments.MessageLabourFragment;
import leebeno.com.leebeno.Login;
import leebeno.com.leebeno.NotificationActivity;
import leebeno.com.leebeno.PostedRatings;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.ReceivedRatings;
import leebeno.com.leebeno.Services.LocationTrack;
import leebeno.com.leebeno.SpleshScreen;
import leebeno.com.leebeno.Wallet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Api.RequestCode.AUTOPLACE_CODE;
import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.getDateToUtc;
import static leebeno.com.leebeno.Services.Utility.setDatePickerInEditView;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;
import static leebeno.com.leebeno.Signup.cl;


public class HomeLabourActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, WebCompleteTask {


    public static HomeLabourActivity mInstance;
    public static List<String> listSkillsName;
    public static String labourSkillHome;
    public DrawerLayout drawer;
    @Bind(R.id.humburgerIcon)
    ImageView humburgerIcon;
    @Bind(R.id.customerTabLayout)
    TabLayout customerTabLayout;
    @Bind(R.id.customerPager)
    ViewPager customerPager;
    ActionBarDrawerToggle toggle;

    @Bind(R.id.notificationIcon)
    ImageView notificationIcon;
    View v1, v2, v3, v4;
    ImageView view1, view2, view3, view4;
    @Bind(R.id.nav_view_admin)
    NavigationView nav_view_admin;
/*
    @Bind(R.id.dateSearch)
    TextView dateSearch;
*/
    TextView userName;
    CircleImageView navProfile;
    @Bind(R.id.filterImg)
    ImageView filterImg;
    @Bind(R.id.nearMe)
    ImageView nearMe;
    MySpinner etSkillsLabour;
    RecyclerView recyclerSkillJob;
    RadioGroup radioGroupPayment;
    EditText etPayPerHour, etYourLocation;
    EditText etFixAmount;
    LinearLayout layoutRangeBar;
    String paymentTypeString="";
    CrystalRangeSeekbar rangeSeekbar;
    EditText etMinValue, etMaxValue, etStartDate;
    List<String> listSelectSkill;
    List<String> listSkillsId = new ArrayList<>();
    ArrayList<String> listSelectedSkillsid;
    AddSkillAdapter addSkillAdapter;
    Button btnReset, btnApply;
    ImageView btnCancel;
    double latitude, longitude;
    Context context;
    LocationTrack gps;
    String jobId, requestId, groupId, walletId;
    public static TextView dateSearchLabour;
    public static EditText searchViewLabour;

    RadioButton radioPayPerH,radiojobPayRange,radioFixAmount;

    public static String getUtcToLocal(String date) {
        String readReminderdate = null;
        try {
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat target_date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            target_date.setTimeZone(tz);
            Date startDate = df.parse(date);
            readReminderdate = target_date.format(startDate);
        } catch (Exception e) {

        }
        return readReminderdate;
    }

    public static HomeLabourActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        ButterKnife.bind(this);
        // searchViewS.setQueryHint("Jo");
        mInstance = this;
        context = this;
        dateSearchLabour=(TextView)findViewById(R.id.dateSearch);
        searchViewLabour=(EditText) findViewById(R.id.searchViewS);
        listSkillsName = new ArrayList<>();
        labourSkillHome = "SkillLabour";
        listSelectSkill = new ArrayList<>();
        listSelectedSkillsid = new ArrayList<>();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
       /* toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
//        paymentTypeString = getResources().getString(R.string.pay_per_hour);
        jobId = getIntent().getStringExtra("jobId");
        requestId = getIntent().getStringExtra("requestId");
        groupId = getIntent().getStringExtra("groupId");
        walletId = getIntent().getStringExtra("walletId");

        nav_view_admin.setNavigationItemSelectedListener(this);
        View viewheader = nav_view_admin.getHeaderView(0);
        userName = (TextView) viewheader.findViewById(R.id.userName);
        navProfile = (CircleImageView) viewheader.findViewById(R.id.navProfile);

        userName.setText(SharedPrefManager.getUserName(HomeLabourActivity.this, Constrants.UserName));
        Picasso.get().load(SharedPrefManager.getUserPic(HomeLabourActivity.this, Constrants.UserPic))
                .placeholder(R.drawable.user).into(navProfile);
        customerTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        v1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1 = v1.findViewById(R.id.icon);
        view1.setImageResource(R.drawable.home_icon_click);
        customerTabLayout.addTab(customerTabLayout.newTab().setCustomView(view1));

        v2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2 = v2.findViewById(R.id.icon);
        view2.setImageResource(R.drawable.list_icon);
        customerTabLayout.addTab(customerTabLayout.newTab().setCustomView(view2));

        v3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3 = v3.findViewById(R.id.icon);
        view3.setImageResource(R.drawable.message_icon);
        customerTabLayout.addTab(customerTabLayout.newTab().setCustomView(view3));

        v4 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view4 = v4.findViewById(R.id.icon);
        view4.setImageResource(R.drawable.setting_icon);
        customerTabLayout.addTab(customerTabLayout.newTab().setCustomView(view4));

        customerPager.setOffscreenPageLimit(4);
        PagerAdpter adapter = new PagerAdpter(getSupportFragmentManager(), customerTabLayout.getTabCount());
        customerPager.setAdapter(adapter);
        customerTabLayout.setOnTabSelectedListener(this);
        customerTabLayout.setSelectedTabIndicatorHeight(0);

        Menu nav_Menu = nav_view_admin.getMenu();
        nav_Menu.findItem(R.id.navCreatedLabour).setVisible(false);

        GetSkillsMethod();


        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(HomeLabourActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeLabourActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 100);
            }

        }

        SharedPreferences.Editor editor = getSharedPreferences("CURRENT_TAB", MODE_PRIVATE).edit();
        editor.putString("tab_name", "home");
        editor.apply();

        dateSearchLabour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("CURRENT_TAB", MODE_PRIVATE);
                String restoredText = prefs.getString("tab_name", null);
                if (restoredText != null) {
//                    if (restoredText.equals("home")) {
                        setDatePick(HomeLabourActivity.this, dateSearchLabour,restoredText);
//                    }
                }

            }
        });
        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeLabourActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        nearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeLabourActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
//                    Toast.makeText(context,"You need have granted permission",Toast.LENGTH_SHORT).show();
                    gps = new LocationTrack(context, HomeLabourActivity.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.latitude;
                        double longitude = gps.longitude;
                        if (latitude != 0.0 && longitude != 0.0) {
                            showNearMeJobs(latitude, longitude);
                        }

//                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }
                }
            }
        });

        filterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(HomeLabourActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.filter_popup);
                    dialog.setCanceledOnTouchOutside(false);


                    dialog.show();

                    etSkillsLabour = (MySpinner) dialog.findViewById(R.id.etSkillsLabour);
                    recyclerSkillJob = (RecyclerView) dialog.findViewById(R.id.recyclerSkillJob);
                    radioGroupPayment = (RadioGroup) dialog.findViewById(R.id.radioGroupPayment);
                    layoutRangeBar = (LinearLayout) dialog.findViewById(R.id.layoutRangeBar);
                    etPayPerHour = (EditText) dialog.findViewById(R.id.etPayPerHour);
                    etFixAmount = (EditText) dialog.findViewById(R.id.etFixAmount);
                    etMaxValue = (EditText) dialog.findViewById(R.id.etMaxValue);
                    etMinValue = (EditText) dialog.findViewById(R.id.etMinValue);
                    etStartDate = (EditText) dialog.findViewById(R.id.etStartDate);
                    etYourLocation = (EditText) dialog.findViewById(R.id.etYourLocation);
                    btnReset = (Button) dialog.findViewById(R.id.btnReset);
                    btnApply = (Button) dialog.findViewById(R.id.btnApply);
                    btnCancel = (ImageView) dialog.findViewById(R.id.btnCancel);

                    radioPayPerH = (RadioButton) dialog.findViewById(R.id.radioPayPerH);
                    radiojobPayRange = (RadioButton) dialog.findViewById(R.id.radiojobPayRange);
                    radioFixAmount = (RadioButton) dialog.findViewById(R.id.radioFixAmount);
                    rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar);
                    recyclerSkillJob.setHasFixedSize(true);
                    recyclerSkillJob.setNestedScrollingEnabled(true);
                    recyclerSkillJob.setLayoutManager(new LinearLayoutManager(HomeLabourActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    etYourLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                        .build(HomeLabourActivity.this);
                                startActivityForResult(intent, AUTOPLACE_CODE);
                            } catch (Exception e) {
                                Log.e("fgbryhyhy", "" + e);
                            }
                        }
                    });

                    rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                        @Override
                        public void valueChanged(Number minValue, Number maxValue) {
                            etMinValue.setText("$ " + String.valueOf(minValue));
                            etMaxValue.setText("$ " + String.valueOf(maxValue));
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            etSkillsLabour.setSelection(0);
                            etPayPerHour.setText("");
                            etFixAmount.setText("");
                            etStartDate.setText("");
                            etYourLocation.setText("");
                            listSelectSkill.clear();
                            listSelectedSkillsid.clear();
                            addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listSelectSkill, HomeLabourActivity.this, recyclerSkillJob, etSkillsLabour);
                            recyclerSkillJob.setAdapter(addSkillAdapter);
                            recyclerSkillJob.setVisibility(View.GONE);

                        }
                    });
                    btnReset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radioPayPerH.isChecked())
                            {
                                radioPayPerH.setChecked(false);
                            }
                            if (radiojobPayRange.isChecked())
                            {
                                radiojobPayRange.setChecked(false);
                            }
                            if (radioFixAmount.isChecked())
                            {
                                radioFixAmount.setChecked(false);
                            }
                            etSkillsLabour.setSelection(0);
                            etPayPerHour.setText("");
                            etFixAmount.setText("");
                            etStartDate.setText("");
                            etYourLocation.setText("");
                            listSelectSkill.clear();
                            paymentTypeString="";
                            listSelectedSkillsid.clear();
                            addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listSelectSkill, HomeLabourActivity.this, recyclerSkillJob, etSkillsLabour);
                            recyclerSkillJob.setAdapter(addSkillAdapter);
                            recyclerSkillJob.setVisibility(View.GONE);

                        }
                    });

                    btnApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (paymentTypeString.compareTo(getResources().getString(R.string.pay_per_hour)) == 0 ) {
                                if (etPayPerHour.getText().toString().isEmpty()) {
                                    etPayPerHour.setError(getResources().getString(R.string.errorPPH));
                                    etPayPerHour.requestFocus();
                                }else  {
                                    dialog.dismiss();
                                    getFilteredJobs(etStartDate.getText().toString(), etPayPerHour.getText().toString(), "");

                                }

                            } else  if (paymentTypeString.compareTo(getResources().getString(R.string.fixed_amount)) == 0) {
                                if (etFixAmount.getText().toString().isEmpty()) {
                                    etFixAmount.setError(getResources().getString(R.string.enter_fix_amount));
                                    etFixAmount.requestFocus();
                                }else  {
                                    dialog.dismiss();
                                    getFilteredJobs(etStartDate.getText().toString(), "", etFixAmount.getText().toString());

                                }

                            } else if (!etPayPerHour.getText().toString().isEmpty() && paymentTypeString.compareTo("") == 0) {
                                showToast(HomeLabourActivity.this, getResources().getString(R.string.payment_type_error));
                            } else {
                                dialog.dismiss();
                                getFilteredJobs(etStartDate.getText().toString(), etPayPerHour.getText().toString(), etFixAmount.getText().toString());
                            }
//                            getFilteredJobs(etStartDate.getText().toString(), etPayPerHour.getText().toString(), etFixAmount.getText().toString());


                        }
                    });
                    etStartDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDatePickerInEditView(HomeLabourActivity.this, etStartDate);
                        }
                    });


                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(HomeLabourActivity.this, R.layout.design_spinner, R.id.textSpinner, listSkillsName) {

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

                    spinnerclick();

                    radioGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            int getRadioId = radioGroupPayment.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) dialog.findViewById(getRadioId);

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

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        });


        searchViewLabour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = searchViewLabour.getText().toString().trim().toLowerCase(Locale.getDefault());
                SharedPreferences prefs = getSharedPreferences("CURRENT_TAB", MODE_PRIVATE);
                String restoredText = prefs.getString("tab_name", null);
                if (restoredText != null) {
                    if (restoredText.equals("home")) {
                        Log.e("getSpace",searchViewLabour.getText().toString().length()+"");
                            HomeAdminFragment.getmInstance().createNewJobAdapter.getFilter().filter(text);


                    } else if (restoredText.equals("myJob")) {
//                        BidAdminFragment fragment = new BidAdminFragment();
//                        fragment.filter(text);
//                        if (searchViewLabour.getText().toString().compareTo("")!=0) {
                            BidAdminFragment.getInstance().createNewJobAdapter.getFilter().filter(text);
//                        }

                    } else if (restoredText.equals("message")) {

                    } else if (restoredText.equals("setting")) {

                    }
                }
                // filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (jobId != null) {
            getJobDetail(jobId);
        } else if (requestId != null || groupId != null) {
            Intent intent = new Intent(HomeLabourActivity.this, ConnectionsLabour.class);
            startActivity(intent);
        } else if (walletId != null) {
            Intent intent = new Intent(HomeLabourActivity.this, Wallet.class);
            startActivity(intent);
        }

        humburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(Gravity.LEFT);

            }
        });
    }


    private void showNearMeJobs(double latitude, double longitude) {
        HomeAdminFragment.getmInstance().getNewrMeJobs(latitude, longitude);

    }

    private void getFilteredJobs(String startDate, String etPayPerHour, String etFixAmount) {

        String getTimeVal = "", getPaymentType = null;
        if (!startDate.isEmpty()) {
            getTimeVal = getDateToUtc(startDate);
        }


        double jobHours = 0, noOfLab = 0, amounFix = 0, amounPph = 0, amtMin = 0, amtMax = 0;
        String minValueStr = "", maxValueStr = "";
        minValueStr = etMinValue.getText().toString().trim().replaceAll("\\$", "");
        maxValueStr = etMaxValue.getText().toString().trim().replaceAll("\\$", "");
        minValueStr = minValueStr.trim();
        maxValueStr = maxValueStr.trim();
//        amounPph= Double.parseDouble(etPayPerHour);

        JSONObject locationobj = null;
        if (latitude != 0 && longitude != 0) {

            try {
                locationobj = new JSONObject();
                locationobj.put("lat", latitude);
                locationobj.put("lng", longitude);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }
        }

        if (paymentTypeString.compareTo(getResources().getString(R.string.pay_per_hour)) == 0) {
            try {
                try {
                    amounPph = new Double(etPayPerHour);
                } catch (NumberFormatException e) {
                    amounPph = 0.0; // your default value
                }

//                amounPph = Double.parseDouble(etPayPerHour);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }

            getPaymentType = getResources().getString(R.string.pph);
        }

        if (paymentTypeString.compareTo(getResources().getString(R.string.job_pay_range)) == 0) {
            try {

                try {
                    amtMin = new Double(minValueStr);
                    amtMax = new Double(maxValueStr);
                } catch (NumberFormatException e) {
                    amtMin = 0.0; // your default value
                    amtMax = 0.0; // your default value
                }


               /* amtMin = Double.parseDouble(minValueStr);
                amtMax = Double.parseDouble(maxValueStr);*/
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }

            getPaymentType = getResources().getString(R.string.range);
        }

        if (paymentTypeString.compareTo(getResources().getString(R.string.fixed_amount)) == 0) {
            try {

                try {
                    amounPph = new Double(etFixAmount);
                } catch (NumberFormatException e) {
                    amounPph = 0.0; // your default value
                }

//                amounPph = Double.parseDouble(etFixAmount);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }

            getPaymentType = getResources().getString(R.string.fix);
        }

        if (etPayPerHour.isEmpty() && etFixAmount.isEmpty() && amtMin == 0.0 && amtMax == 0.0) {
//            paymentTypeString = "";
            getPaymentType = "";
        }


        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        showProgress(HomeLabourActivity.this);
        JSONArray ssk = null;

        if (listSelectedSkillsid.size() != 0) {
            ssk = new JSONArray();
            for (int i = 0; i < listSelectedSkillsid.size(); i++) {
                ssk.put(listSelectedSkillsid.get(i));
            }
        }



        String paymentTT = getPaymentType;

        Log.e("sdvksbdvs", SharedPrefManager.getAccessToken(HomeLabourActivity.this, Constrants.AccessToken) + "  " +
                SharedPrefManager.getLangId(HomeLabourActivity.this, Constrants.UserLang) + " " + getTimeVal + " " + getPaymentType +
                " " + amounPph + " " + amtMin + " " + amtMax + " " + ssk + " " + locationobj);

        HomeAdminFragment.getmInstance().getAllJob(getTimeVal, paymentTT, amounPph, amtMin, amtMax, ssk, locationobj);


        HomeAdminFragment homeAdminFragment = new HomeAdminFragment();
        listSelectedSkillsid.clear();
        latitude = 0;
        minValueStr = "";
        maxValueStr = "";
        etPayPerHour = "";
        etFixAmount = "";
        startDate = "";
        getPaymentType = "";
        longitude = 0;


        etSkillsLabour.setSelection(0);/*
        etPayPerHour.setText("");
        etFixAmount.setText("");*/
        etStartDate.setText("");
        etYourLocation.setText("");
        paymentTypeString="";
        listSelectSkill.clear();
        listSelectedSkillsid.clear();
        addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listSelectSkill, HomeLabourActivity.this, recyclerSkillJob, etSkillsLabour);
        recyclerSkillJob.setAdapter(addSkillAdapter);
        recyclerSkillJob.setVisibility(View.GONE);


     /*   String getTimeVal = "", getPaymentType = null;
        if (!startDate.isEmpty()) {
            getTimeVal = getDateToUtc(startDate);
        }


        double jobHours = 0, noOfLab = 0, amounFix = 0, amounPph = 0, amtMin = 0, amtMax = 0;
        String minValueStr = "", maxValueStr = "";
        minValueStr = etMinValue.getText().toString().trim().replaceAll("\\$", "");
        maxValueStr = etMaxValue.getText().toString().trim().replaceAll("\\$", "");
        minValueStr = minValueStr.trim();
        maxValueStr = maxValueStr.trim();

        JSONObject locationobj = null;
        if (latitude != 0 && longitude != 0) {

            try {

                locationobj = new JSONObject();
                locationobj.put("lat", latitude);
                locationobj.put("lng", longitude);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }
        }

        if (paymentTypeString.compareTo(getResources().getString(R.string.pay_per_hour)) == 0) {
            try {
                amounPph = Double.parseDouble(etPayPerHour);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }

            getPaymentType = getResources().getString(R.string.pph);
        }

        if (paymentTypeString.compareTo(getResources().getString(R.string.job_pay_range)) == 0) {
            try {
                amtMin = Double.parseDouble(etMinValue.getText().toString().trim().replaceAll("\\$", "").trim());
                amtMax = Double.parseDouble(etMaxValue.getText().toString().trim().replaceAll("\\$", "").trim());
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }

            getPaymentType = getResources().getString(R.string.range);
        }

        if (paymentTypeString.compareTo(getResources().getString(R.string.fixed_amount)) == 0) {
            try {
                amounPph = Double.parseDouble(etFixAmount);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }

            getPaymentType = getResources().getString(R.string.fix);
        }

        if (etPayPerHour.isEmpty() && etFixAmount.isEmpty() && amtMin == 0.0 && amtMax == 0.0) {
//            paymentTypeString = "";
            getPaymentType = "";
        }


        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        showProgress(HomeLabourActivity.this);
        JSONArray ssk = null;

        if (listSelectedSkillsid.size() != 0) {
            ssk = new JSONArray();
            for (int i = 0; i < listSelectedSkillsid.size(); i++) {
                ssk.put(listSelectedSkillsid.get(i));
            }
        }

        Log.e("sdvksbdvs", SharedPrefManager.getAccessToken(HomeLabourActivity.this, Constrants.AccessToken) + "  " +
                SharedPrefManager.getLangId(HomeLabourActivity.this, Constrants.UserLang) + " " + getTimeVal + " " + getPaymentType + " " + amounPph + " " + amtMin + " " + amtMax + " " + ssk + " " + locationobj);
        Call<JsonObject> getPostData = apiInterface.getFilteredJob(SharedPrefManager.getAccessToken(HomeLabourActivity.this, Constrants.AccessToken), SharedPrefManager.getLangId(HomeLabourActivity.this
                , Constrants.UserLang), getTimeVal, getPaymentType, amounPph, amtMin, amtMax, ssk, locationobj);
        getPostData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        close();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
                        JSONArray jsonObjectData = jsonObjSuccess.getJSONArray("data");
                        Log.e("FilterdData", jsonObjectData + "");

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
        });*/
    }


    private void logoutUser() {
        showProgress(HomeLabourActivity.this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jobId", jobId);
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JsonObject> call = apiInterface.logoutService(SharedPrefManager.getAccessToken(HomeLabourActivity.this, Constrants.AccessToken),SharedPrefManager.getLangId(HomeLabourActivity.this, Constrants.UserLang));
        Log.e("dfgdf", "" + jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                close();
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        JSONObject jsonObjError = jsonObject.getJSONObject("success");
//                        SharedPrefManager.getInstance(HomeLabourActivity.this).storeIsLoggedIn(false);
                        showToast(HomeLabourActivity.this, "" +jsonObjError.getJSONObject("msg").getString("replyMessage"));
//                        SharedPrefManager.setUserStatus(HomeLabourActivity.this, Constrants.UserStatus,"");

                        SharedPrefManager.getInstance(HomeLabourActivity.this).storeIsLoggedIn(false);
                        Intent intent = new Intent(HomeLabourActivity.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(HomeLabourActivity.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                close();
                Log.e("vvxvxvxv", t + "");
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
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

    public void setDatePick(Context context, final TextView et, final String currentTab) {
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
                        if (currentTab.compareTo("home")==0)
                        {
                            HomeAdminFragment.getmInstance().getAllJob(getUtcToLocal(deliveryDate), "", 0.0, 0.0, 0.0, null, null);
                        }
                        else if (currentTab.compareTo("myJob")==0)
                        {
                            BidAdminFragment.getInstance().getMyJob(getUtcToLocal(deliveryDate));
                        }
                    }

                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et.setText("");
                et.setHint("Date");
                HomeAdminFragment.getmInstance().getAllJob(et.getText().toString(), "", 0.0, 0.0, 0.0, null, null);
            }
        });
        datePickerDialog.show();
    }

    private void getJobDetail(String id) {
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
//        showProgress(HomeLabourActivity.this);


        Log.e("SENDDATA", "" + SharedPrefManager.getAccessToken(HomeLabourActivity.this, Constrants.AccessToken) + "  " + SharedPrefManager.getUserID(HomeLabourActivity.this, Constrants.UserID) + "  " + id + "  " + "  ");
        Call<JsonObject> getPostData = apiInterface.getJobDetail(SharedPrefManager.getAccessToken(HomeLabourActivity.this, Constrants.AccessToken), SharedPrefManager.getLangId(HomeLabourActivity.this
                , Constrants.UserLang), id, SharedPrefManager.getUserID(HomeLabourActivity.this, Constrants.UserID));
        getPostData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
//                        close();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("fdvvbsjkdsv", jsonObject.toString() + "");
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
                        JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");
//                        showToast(HomeLabourActivity.this, jsonObjectMsg.getString("replyMessage"));
                        Intent intent = new Intent(HomeLabourActivity.this, BidPlace.class);
                        intent.putExtra("jobId", jsonObjectData.getString("id"));
                        startActivity(intent);
//                        finish();


                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("BidPlace", "" + t);
//                close();
            }
        });
    }

    private void GetSkillsMethod() {
        HashMap objectNew = new HashMap();
        new WebTask(HomeLabourActivity.this, WebUrls.BASE_URL + WebUrls.getAllSkills_api, objectNew, HomeLabourActivity.this, RequestCode.CODE_getskills, 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //item.setChecked(true);

        int id = item.getItemId();

        if (id == R.id.navProfileText) {
            Intent intent = new Intent(HomeLabourActivity.this, UpdateProfileLabour.class);
            startActivity(intent);
        } else if (id == R.id.navMyReviewRating) {
            Intent intent = new Intent(HomeLabourActivity.this, PostedRatings.class);
            startActivity(intent);
        } else if (id == R.id.navReviewRating) {
            Intent intent = new Intent(HomeLabourActivity.this, ReceivedRatings.class);
            startActivity(intent);
        } else if (id == R.id.navLogout) {
//
//            Intent intent = new Intent(HomeLabourActivity.this, Login.class);
//            Toast.makeText(HomeLabourActivity.this, "Logout SuccessFully ", Toast.LENGTH_SHORT).show();
//            SharedPrefManager.getInstance(HomeLabourActivity.this).storeIsLoggedIn(false);
//            startActivity(intent);
//            finish();
            logoutUser();



        } else if (id == R.id.navEscalation) {
            Intent intent = new Intent(HomeLabourActivity.this, EscalatedJobs.class);
            startActivity(intent);
        } else if (id == R.id.navWallet) {
            Intent intent = new Intent(HomeLabourActivity.this, Wallet.class);
            startActivity(intent);
        } else if (id == R.id.navConnections) {
            Intent intent = new Intent(HomeLabourActivity.this, ConnectionsLabour.class);
            startActivity(intent);
        } else if (id == R.id.archive) {
            Intent intent = new Intent(HomeLabourActivity.this, AchieveBids.class);
            startActivity(intent);
        }
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        customerPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            searchViewLabour.setVisibility(View.VISIBLE);
            dateSearchLabour.setVisibility(View.GONE);

            filterImg.setVisibility(View.VISIBLE);
            nearMe.setVisibility(View.VISIBLE);
            dateSearchLabour.setText(null);
            searchViewLabour.setText(null);

            view1.setImageResource(R.drawable.home_icon_click);
            dateSearchLabour.setHint(getResources().getString(R.string.date));
            SharedPreferences.Editor editor = getSharedPreferences("CURRENT_TAB", MODE_PRIVATE).edit();
            editor.putString("tab_name", "home");
            editor.apply();
        }
        if (tab.getPosition() == 1) {
            searchViewLabour.setVisibility(View.VISIBLE);
            dateSearchLabour.setVisibility(View.VISIBLE);

            filterImg.setVisibility(View.GONE);
            nearMe.setVisibility(View.GONE);
            view2.setImageResource(R.drawable.list_icon_click);

            dateSearchLabour.setText(null);
            searchViewLabour.setText(null);
            dateSearchLabour.setHint(getResources().getString(R.string.date));
            SharedPreferences.Editor editor = getSharedPreferences("CURRENT_TAB", MODE_PRIVATE).edit();
            editor.putString("tab_name", "myJob");
            editor.apply();
        }
        if (tab.getPosition() == 2) {
            searchViewLabour.setVisibility(View.VISIBLE);
            dateSearchLabour.setVisibility(View.GONE);
            filterImg.setVisibility(View.GONE);
            nearMe.setVisibility(View.GONE);
            view3.setImageResource(R.drawable.message_icon_click);

            dateSearchLabour.setText(null);
            searchViewLabour.setText(null);
            dateSearchLabour.setHint(getResources().getString(R.string.date));
            SharedPreferences.Editor editor = getSharedPreferences("CURRENT_TAB", MODE_PRIVATE).edit();
            editor.putString("tab_name", "message");
            editor.apply();
        }
        if (tab.getPosition() == 3) {
            searchViewLabour.setVisibility(View.GONE);
            dateSearchLabour.setVisibility(View.GONE);
            filterImg.setVisibility(View.GONE);
            nearMe.setVisibility(View.GONE);
            dateSearchLabour.setText(null);
            searchViewLabour.setText(null);
            dateSearchLabour.setHint(getResources().getString(R.string.date));
            view4.setImageResource(R.drawable.setting_icon_click);
            SharedPreferences.Editor editor = getSharedPreferences("CURRENT_TAB", MODE_PRIVATE).edit();
            editor.putString("tab_name", "setting");
            editor.apply();

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        customerPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            view1.setImageResource(R.drawable.home_icon);
        }
        if (tab.getPosition() == 1) {
            view2.setImageResource(R.drawable.list_icon);
        }
        if (tab.getPosition() == 2) {
            view3.setImageResource(R.drawable.message_icon);
        }
        if (tab.getPosition() == 3) {
            view4.setImageResource(R.drawable.setting_icon);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        customerPager.setCurrentItem(tab.getPosition());
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

    @Override
    protected void onRestart() {
        super.onRestart();
        userName.setText(SharedPrefManager.getUserName(HomeLabourActivity.this, Constrants.UserName));
        Picasso.get().load(SharedPrefManager.getUserPic(HomeLabourActivity.this, Constrants.UserPic))
                .placeholder(R.drawable.user).into(navProfile);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_getskills) {
                Log.d("Signup_getskills_res", response);

                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONArray jsonArray = jsonObjSuccess.getJSONArray("data");
                //JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                listSkillsName = new ArrayList<>();

                listSkillsName.add(0, getResources().getString(R.string.select_skills));
                listSkillsId.add(0, "0");

                for (int i = 0; i < jsonArray.length(); i++) {
                    listSkillsName.add(jsonArray.getJSONObject(i).optString("name"));
                    listSkillsId.add(jsonArray.getJSONObject(i).optString("id"));
                    // Log.e("vdregrehdhrty",jsonArray.getJSONObject(i).optString("name")+"  "+listSkillsName.get(i));
                }


                //arrayListSkill = new ArrayList<>(Arrays.asList(listSkillsName));


            }
        } catch (Exception e) {

        }
    }

    private void spinnerclick() {
        etSkillsLabour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //  String selectedItem = adapterView.getItemAtPosition(position).toString();
                String selectedItemS = etSkillsLabour.getSelectedItem().toString();
                recyclerSkillJob.setVisibility(View.VISIBLE);
                if (selectedItemS.equals(getResources().getString(R.string.select_skills))) {
                    // do your stuff
                } else {
                    if (listSelectSkill.size() == 0) {
                        listSelectSkill.add(selectedItemS);
                        listSelectedSkillsid.add(listSkillsId.get(position));
                        addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listSelectSkill, HomeLabourActivity.this, recyclerSkillJob, etSkillsLabour);
                        recyclerSkillJob.setAdapter(addSkillAdapter);
                        recyclerSkillJob.setVisibility(View.VISIBLE);
                    } else {
                        if (listSelectSkill.contains(selectedItemS)) {
                            if (cl == false) {
                                showToast(HomeLabourActivity.this, getResources().getString(R.string.select_already_select_skills));
                            }

                        } else {
                            listSelectSkill.add(selectedItemS);
                            listSelectedSkillsid.add(listSkillsId.get(position));
                            addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listSelectSkill, HomeLabourActivity.this, recyclerSkillJob, etSkillsLabour);
                            recyclerSkillJob.setAdapter(addSkillAdapter);
                            recyclerSkillJob.setVisibility(View.VISIBLE);
                        }
                        cl = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


  /*  @Override
    protected void onRestart() {
        super.onRestart();
        userName.setText(SharedPrefManager.getUserName(HomeLabourActivity.this,Constrants.UserName));
        Picasso.get().load(SharedPrefManager.getUserPic(HomeLabourActivity.this,Constrants.UserPic))
                .placeholder(R.drawable.user).into(navProfile);
    }*/

    public void checkdateEmpty() {
        if (TextUtils.isEmpty(dateSearchLabour.getText())) {
            HomeAdminFragment.getmInstance().getAllJob("", "", 0.0, 0.0, 0.0, null, null);
        } else {
            HomeAdminFragment.getmInstance().getAllJob(dateSearchLabour.getText().toString(), "", 0.0, 0.0, 0.0, null, null);
        }
    }

    public class PagerAdpter extends FragmentStatePagerAdapter {

        int tabCount;

        public PagerAdpter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HomeAdminFragment homeAdminFragment = new HomeAdminFragment();
                    return homeAdminFragment;
                case 1:
                    BidAdminFragment bidAdminFragment = new BidAdminFragment();
                    return bidAdminFragment;
                case 2:
                    MessageLabourFragment messageAminFragment = new MessageLabourFragment();
                    return messageAminFragment;
                case 3:
                    SettingCustomerFragment settingAminFragment = new SettingCustomerFragment();
                    return settingAminFragment;
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
