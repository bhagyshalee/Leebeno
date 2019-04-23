package leebeno.com.leebeno.LabourPart;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AccountSetting;
import leebeno.com.leebeno.Adapters.AddSkillAdapter;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.MySpinner;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;
import leebeno.com.leebeno.Signup;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static leebeno.com.leebeno.Api.WebUrls.changePassword;
import static leebeno.com.leebeno.Services.Config.skillsLabour;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class UpdateProfileLabour extends AppCompatActivity implements View.OnClickListener, WebCompleteTask {

    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.profilePicUpdate)
    ImageView profilePicUpdate;
    Uri imageUri;

    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etAddress)
    EditText etAddress;
    @Bind(R.id.etStreetA)
    EditText etStreetA;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.etMinimumPayPerHour)
    EditText etMinimumPayPerHour;
    @Bind(R.id.etSkillsLabour)
    MySpinner etSkillsLabour;
    @Bind(R.id.recyclerSkill)
    RecyclerView recyclerSkill;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.profile_edit_t)
    TextView profile_edit_t;

    @Bind(R.id.changePassword)
    TextView changePasswordtext;

    public static boolean edit = false;
    public static String labour;
    double latitude, longitude;
    File compressedImage;
    Dialog dialog;

    //    List<String> skillsaArray;
//    List<String> skillsaArrayID ;
    ArrayList<String> listSelectedSkillsid;
    List<String> listsSelectSkill;
    AddSkillAdapter addSkillAdapter;
    private static final int PICK_Camera_IMAGE = 100;

    List<String> listSkillsId;
    public static List<String> listSkillsName;
    AdapterView<?> adapterVie;
    boolean getMenuallyLogin = false;
    public static UpdateProfileLabour mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_update_profile_labour);

        getMenuallyLogin = getIntent().getBooleanExtra("manually", false);

        ButterKnife.bind(this);
        etAddress.setOnClickListener(this);
        backSignUp.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        profilePicUpdate.setOnClickListener(this);

        mInstance = this;

//        skillsaArray =  new ArrayList<>();
//        skillsaArrayID = new ArrayList<>();
        listsSelectSkill = new ArrayList<>();
        listSelectedSkillsid = new ArrayList<>();
        skillsLabour = new ArrayList<>();

        profilePicUpdate.setClickable(false);
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etAddress.setEnabled(false);
        etStreetA.setEnabled(false);
        btnSubmit.setClickable(false);
        etDescription.setEnabled(false);
        etMinimumPayPerHour.setEnabled(false);
        etSkillsLabour.setEnabled(false);
        recyclerSkill.setClickable(false);
//        etSkillsLabour.setVisibility(View.GONE);
//        selected_skills.setVisibility(View.VISIBLE);

        labour = "up";
        recyclerSkill.setHasFixedSize(false);
        recyclerSkill.setNestedScrollingEnabled(true);
        recyclerSkill.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        SpannableString content = new SpannableString(changePasswordtext.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, changePasswordtext.getText().toString().length(), 0);
        changePasswordtext.setText(content);

        changePasswordtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                     dialog= new Dialog(UpdateProfileLabour.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.change_password);
                    dialog.setCanceledOnTouchOutside(false);
                    final ImageView crossPopup = (ImageView) dialog.findViewById(R.id.crossPopup);
                    final EditText etCurrentPassword = (EditText) dialog.findViewById(R.id.etCurrentPassword);
                    final EditText etNewPassword = (EditText) dialog.findViewById(R.id.etNewPassword);
                    final EditText etConfirmPassword = (EditText) dialog.findViewById(R.id.etConfirmPassword);
                    Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                    dialog.show();


                    crossPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etCurrentPassword.getText().toString().isEmpty())
                            {
                                etCurrentPassword.setError(getString(R.string.current_pass));
                                etCurrentPassword.requestFocus();
                            }
                            else if (etNewPassword.getText().toString().isEmpty())
                            {
                                etNewPassword.setError(getString(R.string.ent_new_pass));
                                etNewPassword.requestFocus();
                            }else if (etNewPassword.getText().toString().length()<6){
                                etNewPassword.setError(getString(R.string.pass_must_6));
                                etNewPassword.requestFocus();
                            } else if (!SharedPrefManager.getInstance(UpdateProfileLabour.this).CheckPassword(etNewPassword.getText().toString())){
                                etNewPassword.setError(getString(R.string.pass_must_6));
                                etNewPassword.requestFocus();
                            } else if (etConfirmPassword.getText().toString().isEmpty())
                            {
                                etConfirmPassword.setError(getResources().getString(R.string.plz_ent_cnf_pass));
                                etConfirmPassword.requestFocus();
                            }
                            else if (etConfirmPassword.getText().toString().compareTo(etNewPassword.getText().toString())!=0)
                            {
                                etConfirmPassword.setError(getResources().getString(R.string.pass_nd_cnf_pass_not));
                                etConfirmPassword.requestFocus();
                            }else
                            {
                                HashMap hashMap=new HashMap();
                                hashMap.put("oldPassword",etCurrentPassword.getText().toString());
                                hashMap.put("newPassword",etNewPassword.getText().toString());
                                new WebTask(UpdateProfileLabour.this, WebUrls.BASE_URL+changePassword,hashMap,UpdateProfileLabour.this, RequestCode.CODE_ChangePassword,1);

                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        if (getMenuallyLogin) {
            etAddress.setVisibility(View.GONE);
            etMinimumPayPerHour.setVisibility(View.GONE);
            etStreetA.setVisibility(View.GONE);
//            etMinimumPayPerHour.setVisibility(View.GONE);

        }

        profile_edit_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit) {
                    profilePicUpdate.setClickable(false);
                    etName.setEnabled(false);
                    etEmail.setEnabled(false);
                    etDescription.setEnabled(false);
                    etMinimumPayPerHour.setEnabled(false);
                    etAddress.setEnabled(false);
                    etStreetA.setEnabled(false);
                    btnSubmit.setClickable(false);
                    etSkillsLabour.setEnabled(false);
                    recyclerSkill.setClickable(false);
//                    etSkillsLabour.setVisibility(View.GONE);
//                    selected_skills.setVisibility(View.VISIBLE);
                    GetProfileMothed();
                    edit = false;
                } else {
                    profilePicUpdate.setClickable(true);
                    etName.setEnabled(true);
                    etEmail.setEnabled(false);
                    etDescription.setEnabled(true);
                    etMinimumPayPerHour.setEnabled(true);
                    etAddress.setEnabled(true);
                    etStreetA.setEnabled(true);
                    btnSubmit.setClickable(true);
                    etSkillsLabour.setEnabled(true);
                    recyclerSkill.setClickable(true);
//                    etSkillsLabour.setVisibility(View.VISIBLE);
//                    selected_skills.setVisibility(View.GONE);
                    edit = true;
                }

            }
        });
        GetProfileMothed();

        etSkillsLabour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String selectedItem = adapterView.getItemAtPosition(position).toString();
                String selectedItemS = etSkillsLabour.getSelectedItem().toString();
                if (selectedItemS.equals(getResources().getString(R.string.select_skills))) {

                } else {
                    if (edit) {
                        if (listsSelectSkill.size() == 0) {
                            listsSelectSkill.add(selectedItemS);
                            listSelectedSkillsid.add(listSkillsId.get(position));
                            addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listsSelectSkill, UpdateProfileLabour.this, recyclerSkill, etSkillsLabour);
                            recyclerSkill.setAdapter(addSkillAdapter);
                            recyclerSkill.setVisibility(View.VISIBLE);
                        } else {
                            if (listsSelectSkill.contains(selectedItemS)) {
                                if (edit) {
                                    if (Signup.cl == false) {
                                        showToast(UpdateProfileLabour.this, getResources().getString(R.string.select_already_select_skills));
                                    }

                                }
                            } else {
                                listsSelectSkill.add(selectedItemS);
                                listSelectedSkillsid.add(listSkillsId.get(position));
                                addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listsSelectSkill, UpdateProfileLabour.this, recyclerSkill, etSkillsLabour);
                                recyclerSkill.setAdapter(addSkillAdapter);
                                recyclerSkill.setVisibility(View.VISIBLE);
                            }
                            Signup.cl = false;
                        }

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    @Override
    public void onClick(View view) {
        if (view == etAddress) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, RequestCode.AUTOPLACE_CODE);
            } catch (Exception e) {
                Log.e("vfkldfnv", "" + e);
            }
        }
        if (view == profilePicUpdate) {
            try {
                final Dialog dialog = new Dialog(UpdateProfileLabour.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popuup_camragallary_design);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                LinearLayout txtCamera = (LinearLayout) dialog.findViewById(R.id.layoutGallery);
                LinearLayout txtGallery = (LinearLayout) dialog.findViewById(R.id.layoutCamera);

                txtGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(UpdateProfileLabour.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UpdateProfileLabour.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                            } else {

                                selectCameraImage();
                                dialog.dismiss();
                            }
                        } else {
                            selectCameraImage();
                            dialog.dismiss();
                        }
                    }
                });
                txtCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(UpdateProfileLabour.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UpdateProfileLabour.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                            } else {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_PICK);
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 200);

                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), 200);
                            dialog.dismiss();
                        }
                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (view == backSignUp) {
            edit = false;
            finish();
        }
        if (view == btnSubmit) {
            if (getMenuallyLogin) {
                if (etName.getText().toString().trim().isEmpty()) {
                    etName.setError(getResources().getString(R.string.plz_ent_ur_name));
                    etName.requestFocus();
                } else if (etName.getText().toString().trim().length() >= 20) {
                    etName.setError(getString(R.string.name_is_large));
                    etName.requestFocus();
                } else if (etDescription.getText().toString().trim().isEmpty()) {
                    etDescription.setError(getResources().getString(R.string.plz_ent_des));
                    etDescription.requestFocus();
                }else if (listsSelectSkill != null && listsSelectSkill.size() < 1) {
                    Toast.makeText(UpdateProfileLabour.this, R.string.atleastone, Toast.LENGTH_SHORT).show();
                } else {
                    changeLabourProfile();
                }
            }else {
                if (etName.getText().toString().trim().isEmpty()) {
                    etName.setError(getResources().getString(R.string.plz_ent_ur_name));
                    etName.requestFocus();
                } else if (etName.getText().toString().trim().length() >= 20) {
                    etName.setError(getString(R.string.name_is_large));
                    etName.requestFocus();
                } else if (etDescription.getText().toString().trim().isEmpty()) {
                    etDescription.setError(getResources().getString(R.string.plz_ent_des));
                    etDescription.requestFocus();
                } else if (etMinimumPayPerHour.getText().toString().trim().isEmpty()) {
                    etMinimumPayPerHour.setError(getResources().getString(R.string.mini_pay_per));
                    etMinimumPayPerHour.requestFocus();
                } else if (etAddress.getText().toString().trim().isEmpty()) {
                    etAddress.setError(getResources().getString(R.string.address));
                    etAddress.requestFocus();
                } else if (etStreetA.getText().toString().trim().isEmpty()) {
                    etStreetA.setError(getResources().getString(R.string.ent_str_or_house_no));
                    etStreetA.requestFocus();
                } else if (listsSelectSkill != null && listsSelectSkill.size() < 1) {
                    Toast.makeText(UpdateProfileLabour.this, R.string.atleastone, Toast.LENGTH_SHORT).show();
                } else {
                    changeLabourProfile();
                }
            }

        }
    }

    private void changeLabourProfile() {
        JSONObject subObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        JSONObject latlong = new JSONObject();

        try {
            latlong.put("lat", latitude);
            latlong.put("lng", longitude);
            subObject.put("value", "" + etAddress.getText().toString().trim());
            subObject.put("location", latlong);
            subObject.put("street", "" + etStreetA.getText().toString().trim());
            //jsonObject.put("address", subObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray();

        for (int i = 0; i < listSelectedSkillsid.size(); i++) {
            array.put(listSelectedSkillsid.get(i));
        }
               /* String[] strings = new String[listSelectSkill.size()];
                strings = listSelectSkill.toArray(strings);*/

        //Log.e("gbgegergrt", "" + strings+" " +  Arrays.toString(strings));
        Log.e("gbgegergrt", "" + array);
        HashMap hashMap = new HashMap();
        hashMap.put("fullName", "" + etName.getText().toString());
        hashMap.put("description", "" + etDescription.getText().toString());
        hashMap.put("pph", "" + etMinimumPayPerHour.getText().toString());
        hashMap.put("skillIds", "" + array);
        hashMap.put("address", "" + subObject);
        Log.e("postData", "" + hashMap);
        //new WebTask(UpdateProfileCustomer.this, WebUrls.BASE_URL + updateCustomerProfileApi, hashMap, UpdateProfileCustomer.this, RequestCode.CODE_UpdateCustomerProfile, 1);

        new WebTask(UpdateProfileLabour.this, WebUrls.BASE_URL + WebUrls.updateLabourProfileApi, hashMap, UpdateProfileLabour.this, RequestCode.CODE_UpdateLabourProfile, 1);

    }

    public void GetSkillsMethod() {
        HashMap objectNew = new HashMap();
        new WebTask(UpdateProfileLabour.this, WebUrls.BASE_URL + WebUrls.getAllSkills_api, objectNew, UpdateProfileLabour.this, RequestCode.CODE_getskills, 0);
    }

    public void GetProfileMothed() {
        HashMap objectNew = new HashMap();
        new WebTask(UpdateProfileLabour.this, WebUrls.BASE_URL + WebUrls.getProfileApi, objectNew, UpdateProfileLabour.this, RequestCode.CODE_getProfileInfo, 0);

    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {

            if (taskcode == RequestCode.CODE_ChangePassword) {
                Log.e("responseSS", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                    showToast(UpdateProfileLabour.this, "" + jsonMsg.getString("replyMessage"));
                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (taskcode == RequestCode.CODE_UpdateLabourProfile) {
                Log.e("responseSS", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    JSONObject dataObj = jsonObjSuccess.optJSONObject("data");

                    SharedPrefManager.setUserPic(UpdateProfileLabour.this, Constrants.UserPic, WebUrls.BASE_URL + dataObj.optString("image"));
                    SharedPrefManager.setUserName(UpdateProfileLabour.this, Constrants.UserName, dataObj.optString("fullName"));
                    JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                    skillsLabour.clear();
                    for (int i = 0; i < dataObj.getJSONArray("skillIds").length(); i++) {
                        skillsLabour.add(dataObj.getJSONArray("skillIds").optString(i));
                    }

                    Signup.cl = true;
                    showToast(UpdateProfileLabour.this, "" + jsonMsg.getString("replyMessage"));
                    GetProfileMothed();
                    // finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (taskcode == RequestCode.CODE_getProfileInfo) {
                Log.e("responseProfile", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    JSONObject dataObj = jsonObjSuccess.optJSONObject("data");
                    etName.setText(dataObj.optString("fullName"));
                    etEmail.setText(dataObj.optString("email"));
                    etAddress.setText(dataObj.optJSONObject("address").optString("value"));
                    etStreetA.setText(dataObj.optJSONObject("address").optString("street"));
                    etDescription.setText(dataObj.optString("description"));
                    etMinimumPayPerHour.setText(dataObj.optString("pph"));

                    SharedPrefManager.setUserPic(UpdateProfileLabour.this, Constrants.UserPic, WebUrls.BASE_URL + dataObj.optString("image"));
                    Picasso.get().load(WebUrls.BASE_URL + dataObj.optString("image"))
                            .fit()
                            .placeholder(R.drawable.user_).into(profilePicUpdate);

                    listsSelectSkill.clear();
                    listSelectedSkillsid.clear();
                    JSONArray skillsget = dataObj.optJSONArray("skill");
                    if (skillsget != null && skillsget.length() > 0) {
                        for (int i = 0; i < skillsget.length(); i++) {
                            listsSelectSkill.add(skillsget.optJSONObject(i).optString("name"));
                            listSelectedSkillsid.add(skillsget.optJSONObject(i).optString("id"));
                        }
                        recyclerSkill.setVisibility(View.VISIBLE);
                        addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid, listsSelectSkill, UpdateProfileLabour.this, recyclerSkill, etSkillsLabour);
                        recyclerSkill.setAdapter(addSkillAdapter);
                    }

                    GetSkillsMethod();
                    JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
//                    showToast(UpdateProfileCustomer.this, "" + jsonMsg.getString("replyMessage"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (taskcode == RequestCode.CODE_getskills) {
                Log.d("Signup_getskills_res", response);

                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONArray jsonArray = jsonObjSuccess.getJSONArray("data");
                //JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                listSkillsName = new ArrayList<>();
                listSkillsId = new ArrayList<>();
                listSkillsName.add(0, getResources().getString(R.string.select_skills));
                listSkillsId.add(0, "0");

                for (int i = 0; i < jsonArray.length(); i++) {
                    listSkillsName.add(jsonArray.getJSONObject(i).optString("name"));
                    listSkillsId.add(jsonArray.getJSONObject(i).optString("id"));
                    // Log.e("vdregrehdhrty",jsonArray.getJSONObject(i).optString("name")+"  "+listSkillsName.get(i));
                }

                //arrayListSkill = new ArrayList<>(Arrays.asList(listSkillsName));
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UpdateProfileLabour.this, R.layout.design_spinner, R.id.textSpinner, listSkillsName) {
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

                if (listsSelectSkill != null && listsSelectSkill.size() > 0) {
                    int po = spinnerArrayAdapter.getPosition(listsSelectSkill.get(listsSelectSkill.size() - 1));
                    etSkillsLabour.setSelection(po);
                } else {
                    etSkillsLabour.setAdapter(spinnerArrayAdapter);
                }

            }
        } catch (Exception e) {
            Log.e("Exception", "" + e);
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        edit = false;
//    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void selectCameraImage() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, PICK_Camera_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/leebeno");

                    new ImageCompressionAsyncTask(UpdateProfileLabour.this).execute(data.getData().toString(),
                            fileImage.getPath());
                }
            }
        } else if (requestCode == 100) {
            Uri selectedImageUri = null;
            String filePath = null;
            selectedImageUri = imageUri;

            if (selectedImageUri != null) {
                try {
                    String filemanagerstring = selectedImageUri.getPath();
                    String selectedImagePath = getPath(selectedImageUri);
                    if (selectedImagePath != null) {
                        filePath = selectedImagePath;

                    } else if (filemanagerstring != null) {
                        filePath = filemanagerstring;
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown path",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Internal error",
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }

            File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/leebeno");
            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTask(UpdateProfileLabour.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }
        }
        if (requestCode == RequestCode.AUTOPLACE_CODE) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                etAddress.setText(place.getAddress());

//                Toast.makeText(this,place.getAddress()+"\n"+place.getLatLng(),Toast.LENGTH_SHORT).show();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("df", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user_ canceled the operation.
            }
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(UpdateProfileLabour.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(UpdateProfileLabour.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                String strCompressedFileImage = s;
                compressedImage = new File(s);
                Uri compressUri = Uri.fromFile(compressedImage);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UpdateProfileLabour.this.getContentResolver(), compressUri);

                    profilePicUpdate.setImageBitmap(bitmap);
                    uploadBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }

    private void uploadBitmap(final Bitmap bitmap) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

//        String jsonObjData = String.valueOf(data);
//        RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);

        List<MultipartBody.Part> photos = new ArrayList<>();
        MultipartBody.Part filePartmultipleImages, filePartmultipleImagesPro;
        RequestBody requestBodyProfile = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        filePartmultipleImagesPro = MultipartBody.Part.createFormData("image", compressedImage.getName(), requestBodyProfile);

//        for (int i = 0; i < fileDoc.size(); i++) {
//            File file = null;
//            file = new File(String.valueOf(fileDoc.get(i)));
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//            filePartmultipleImages = MultipartBody.Part.createFormData("docs", file.getName(), requestBody);
//            photos.add(filePartmultipleImages);
//        }

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JsonObject> call = getResponse.UpdateProfilePic(
                SharedPrefManager.getLangId(UpdateProfileLabour.this, Constrants.UserLang),
                SharedPrefManager.getInstance(UpdateProfileLabour.this).getAccessToken(),
                filePartmultipleImagesPro);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                        dismissProgressBar();
                if (response.isSuccessful()) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.body().toString());
                        Log.d("Tag", "response_body" + response.body().toString());
                        JSONObject successObj = obj.optJSONObject("success");
                        JSONObject dataObj = successObj.optJSONObject("data");
                        JSONObject msgObj = successObj.optJSONObject("msg");
                        String image = dataObj.optString("image");
                        if (msgObj.optString("replyCode").compareTo("Success") == 0) {
                            try {
                                SharedPrefManager.setUserPic(UpdateProfileLabour.this, Constrants.UserPic, WebUrls.BASE_URL + image);
                                Toast.makeText(UpdateProfileLabour.this, msgObj.getString("replyMessage"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } catch (Exception e) {
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        System.out.println("error response " + jsonObject.toString());
                        progressDialog.dismiss();

                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                        dismissProgressBar();
                //  TestFragment.imageStringpath.clear();
                Toast.makeText(UpdateProfileLabour.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("fail response.." + t.toString());

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        edit = false;
    }

    public static UpdateProfileLabour getInstance() {
        return mInstance;
    }
}
