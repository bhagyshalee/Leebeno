package leebeno.com.leebeno;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.HomeAdminActivity;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.LabourPart.HomeLabourActivity;
import leebeno.com.leebeno.LabourPart.MenuallyLabourLogin;

import static leebeno.com.leebeno.Common.SharedPrefManager.getLoginType;
import static leebeno.com.leebeno.Services.Config.editor;
import static leebeno.com.leebeno.Services.Config.getLoginStatus;
import static leebeno.com.leebeno.Services.Config.skillsLabour;


public class Login extends AppCompatActivity implements View.OnClickListener, WebCompleteTask {

    @Bind(R.id.customerLabel)
    TextView customerLabel;
    @Bind(R.id.labourLabel)
    TextView labourLabel;
    @Bind(R.id.groupAdminLabel)
    TextView groupAdminLabel;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.rememberCheck)
    CheckBox rememberCheck;
    @Bind(R.id.forgetCheck)
    TextView forgetCheck;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.signupText)
    TextView signupText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);
        getLoginStatus = "customer";
        ButterKnife.bind(this);
        customerLabel.setOnClickListener(this);
        labourLabel.setOnClickListener(this);
        groupAdminLabel.setOnClickListener(this);
        signupText.setOnClickListener(this);
        forgetCheck.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        skillsLabour = new ArrayList<>();
        CheckLastLogin();

        editor = getSharedPreferences("LANGUAGE", MODE_PRIVATE).edit();
        editor.putBoolean("selectLanguage", true);
        editor.apply();
        editor.commit();

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 100);
            }
            if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 100);
            }
            if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.INTERNET}, 100);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == customerLabel) {
            customerLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
            labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            customerLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            labourLabel.setTextColor(getResources().getColor(R.color.white));
            groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
            getLoginStatus = "customer";

        }
        if (view == labourLabel) {
            customerLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            labourLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
            groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            customerLabel.setTextColor(getResources().getColor(R.color.white));
            labourLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
            getLoginStatus = "labour";

        }
        if (view == groupAdminLabel) {
            customerLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
            customerLabel.setTextColor(getResources().getColor(R.color.white));
            labourLabel.setTextColor(getResources().getColor(R.color.white));
            groupAdminLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            getLoginStatus = "groupAdmin";

        }
        if (view == signupText) {
            //  if (getLoginStatus.equals("customer")) {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
            // }
           /* if (getLoginStatus.equals("labour")) {
                Intent intent = new Intent(Login.this, SignupLabour.class);
                startActivity(intent);
            }
            if (getLoginStatus.equals("groupAdmin")) {
                Intent intent = new Intent(Login.this, SignupAdmin.class);
                startActivity(intent);
            }*/
            //  finish();
        }
        if (view == forgetCheck) {
            Intent intent = new Intent(Login.this, ForgetPassword.class);
            startActivity(intent);
            //finish();
        }
        if (view == btnLogin) {
            if (getLoginStatus.equals("customer")) {
                loginServiceMethod();
            }
            if (getLoginStatus.equals("labour")) {
                loginServiceMethod();
//                Intent intent=new Intent(Login.this,HomeLabourActivity.class);
//                startActivity(intent);
//                finish();
            }
            if (getLoginStatus.equals("groupAdmin")) {
                loginServiceMethod();
//                Intent intent=new Intent(Login.this,HomeAdminActivity.class);
//                startActivity(intent);
//                finish();
            }

        }
    }

    public void loginServiceMethod() {
        SharedPreferences shared = getSharedPreferences("REG_TOKEN", MODE_PRIVATE);
        String channel = shared.getString(getResources().getString(R.string.firebase_token), "");

        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError(getString(R.string.plz_ent_email));
            etEmail.requestFocus();
        } else if (!SharedPrefManager.getInstance(Login.this).isValidEmail(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.email_not_valid));
            etEmail.requestFocus();
        } else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            etPassword.setError("Please enter Password");
            etPassword.requestFocus();
        } else {
//            firebase_token = SharedPrefManager.getInstance(this).getFireBaseToken();
            HashMap objectNew = new HashMap();
            objectNew.put("realm", getLoginStatus);
            objectNew.put("password", etPassword.getText().toString().trim());
            objectNew.put("email", etEmail.getText().toString().trim());
            objectNew.put("firebaseToken", channel);
//            objectNew.put("ln", SharedPrefManager.getLangId(this, RequestCode.LangId));

//            session.createLoginSession(mobile_string, firebase_token);
            new WebTask(Login.this, WebUrls.BASE_URL + WebUrls.login_api, objectNew, Login.this, RequestCode.CODE_Login, 1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
//        progressDialog.show();

        if (taskcode == RequestCode.CODE_Login) {
            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);

                Log.e("responseLOgin", jsonObject.toString());

                JSONObject success = jsonObject.optJSONObject("success");
//                JSONObject data = success.optJSONObject("data");
                String id = success.optString("id");
                String userId = success.optString("userId");
                String ln = success.optString("ln");

                JSONObject user = success.optJSONObject("user");
                String fullName = user.optString("fullName");

//                JSONObject address = user_.optJSONObject("address");
//                String addressvalue = address.optString("value");
//                String street = address.optString("street");
//
//                JSONObject location = address.optJSONObject("location");
//                String location_lat= location.optString("lat");
//                String location_lng= location.optString("lng");

                Boolean active = user.optBoolean("active");
                String title = user.optString("title");
                String description = user.optString("description");
//                String pph=user_.optString("pph");
                String groupMembers = user.optString("groupMembers");
                String realm = user.optString("realm");
                String email = user.optString("email");
                Boolean emailVerified = user.optBoolean("emailVerified");
                String image = user.optString("image");



                String access_token = success.optString("access_token");
                boolean notyMute = user.optBoolean("notyMute");

                SharedPrefManager.setID(Login.this, Constrants.ID, id);
                SharedPrefManager.setUserID(Login.this, Constrants.UserID, userId);
                SharedPrefManager.setNotyMute(Login.this, Constrants.notyMute,notyMute);
                Log.e("notiGetStatus",""+SharedPrefManager.getNotyMute(Login.this, Constrants.notyMute)
                +"  "+SharedPrefManager.getID(Login.this, Constrants.ID)+"  "+success.optBoolean("notyMute"));
                SharedPrefManager.setAccessToken(Login.this, Constrants.AccessToken, access_token);
                SharedPrefManager.setUserPic(Login.this, Constrants.UserPic, WebUrls.BASE_URL + image);
                SharedPrefManager.setUserName(Login.this, Constrants.UserName, fullName);
                SharedPrefManager.setLangId(Login.this, Constrants.UserLang, ln);
                SharedPrefManager.getInstance(getApplicationContext()).storeAccessToken(access_token);
                if (realm.equals("customer")) {
                    Intent intent = new Intent(Login.this, HomeCustomerActivity.class);
                    Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(Login.this).storeIsLoggedIn(true);
                    SharedPrefManager.setUserStatus(Login.this, Constrants.UserStatus, realm);
                    if (rememberCheck.isChecked()) {
                        SharedPrefManager.setUserEmail(Login.this, Constrants.Email, email);
                        SharedPrefManager.setUserPassword(Login.this, Constrants.Password, etPassword.getText().toString());
                        SharedPrefManager.getInstance(Login.this).storeIsChecked(Login.this, Constrants.IsChecked, true);
                    } else {
                        SharedPrefManager.getInstance(Login.this).storeIsChecked(Login.this, Constrants.IsChecked, false);
                        SharedPrefManager.setUserEmail(Login.this, Constrants.Email, "");
                        SharedPrefManager.setUserPassword(Login.this, Constrants.Password, "");
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (realm.equals("labour")) {


                    skillsLabour.clear();
                    for (int i = 0; i < user.getJSONArray("skillIds").length(); i++) {
                        skillsLabour.add(user.getJSONArray("skillIds").optString(i));
                    }


//                    Set<String> set = myScores.getStringSet("key", null);

                    Set<String> set = new HashSet<String>();
                    set.addAll(skillsLabour);
                    SharedPrefManager.setLabourSkills(Login.this, Constrants.LabourSkill, set);



                    Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(Login.this).storeIsLoggedIn(true);
                    SharedPrefManager.setUserStatus(Login.this, Constrants.UserStatus, realm);
                    if (rememberCheck.isChecked()) {
                        String CreatedBY = user.optString("createdBy");
                        SharedPrefManager.setLoginType(Login.this, Constrants.loginType, CreatedBY);
                        SharedPrefManager.setUserEmail(Login.this, Constrants.Email, email);
                        SharedPrefManager.setUserPassword(Login.this, Constrants.Password, etPassword.getText().toString());
                        SharedPrefManager.getInstance(Login.this).storeIsChecked(Login.this, Constrants.IsChecked, true);
                    } else {
                        SharedPrefManager.getInstance(Login.this).storeIsChecked(Login.this, Constrants.IsChecked, false);
                        SharedPrefManager.setUserEmail(Login.this, Constrants.Email, "");
                        SharedPrefManager.setUserPassword(Login.this, Constrants.Password, "");
                    }

                    Log.e("loginTTYpeLOGIn",SharedPrefManager.getLoginType(Login.this, Constrants.loginType));
                    if (user.optString("createdBy").compareTo("System")==0) {
                        Intent intent = new Intent(Login.this, HomeLabourActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Intent intent = new Intent(Login.this, MenuallyLabourLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                } else if (realm.equals("groupAdmin")) {
                    Intent intent = new Intent(Login.this, HomeAdminActivity.class);
                    Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(Login.this).storeIsLoggedIn(true);
                    SharedPrefManager.setUserStatus(Login.this, Constrants.UserStatus, realm);
                    if (rememberCheck.isChecked()) {
                        SharedPrefManager.setUserEmail(Login.this, Constrants.Email, email);
                        SharedPrefManager.setUserPassword(Login.this, Constrants.Password, etPassword.getText().toString());
                        SharedPrefManager.getInstance(Login.this).storeIsChecked(Login.this, Constrants.IsChecked, true);
                    } else {
                        SharedPrefManager.getInstance(Login.this).storeIsChecked(Login.this, Constrants.IsChecked, false);
                        SharedPrefManager.setUserEmail(Login.this, Constrants.Email, "");
                        SharedPrefManager.setUserPassword(Login.this, Constrants.Password, "");
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplication(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LoginExceptionsss",e+"");
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        CheckLastLogin();

    }

    public void CheckLastLogin() {
        if (SharedPrefManager.getUserStatus(Login.this, Constrants.UserStatus).compareTo("customer") == 0) {
            if (SharedPrefManager.getInstance(Login.this).getIsChecked(Login.this, Constrants.IsChecked)) {
                customerLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
                labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                customerLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                labourLabel.setTextColor(getResources().getColor(R.color.white));
                groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
                getLoginStatus = "customer";
                etEmail.setText(SharedPrefManager.getUserEmail(Login.this, Constrants.Email));
                etPassword.setText(SharedPrefManager.getUserPassword(Login.this, Constrants.Password));
                rememberCheck.setChecked(true);
            } else {
                customerLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
                labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                customerLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                labourLabel.setTextColor(getResources().getColor(R.color.white));
                groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
                getLoginStatus = "customer";
                rememberCheck.setChecked(false);
            }

            if (SharedPrefManager.getInstance(Login.this).getIsLoggedIn(false)) {
                Intent intent = new Intent(Login.this, HomeCustomerActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (SharedPrefManager.getUserStatus(Login.this, Constrants.UserStatus).compareTo("labour") == 0) {

            if (SharedPrefManager.getInstance(Login.this).getIsChecked(Login.this, Constrants.IsChecked)) {
                customerLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                labourLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
                groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                customerLabel.setTextColor(getResources().getColor(R.color.white));
                labourLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
                getLoginStatus = "labour";
                etEmail.setText(SharedPrefManager.getUserEmail(Login.this, Constrants.Email));
                etPassword.setText(SharedPrefManager.getUserPassword(Login.this, Constrants.Password));
                rememberCheck.setChecked(true);
            } else {
                customerLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
                labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                customerLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                labourLabel.setTextColor(getResources().getColor(R.color.white));
                groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
                getLoginStatus = "customer";
                rememberCheck.setChecked(false);
            }

            Log.e("loginTTYpe",SharedPrefManager.getLoginType(Login.this, Constrants.loginType));

            if (SharedPrefManager.getInstance(Login.this).getIsLoggedIn(false)) {
                if (SharedPrefManager.getLoginType(Login.this, Constrants.loginType).compareTo("System")==0)
                {
                    Intent intent = new Intent(Login.this, HomeLabourActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Intent intent = new Intent(Login.this, MenuallyLabourLogin.class);
                    startActivity(intent);
                    finish();
                }

            }
        } else if (SharedPrefManager.getUserStatus(Login.this, Constrants.UserStatus).compareTo("groupAdmin") == 0) {

            if (SharedPrefManager.getInstance(Login.this).getIsChecked(Login.this, Constrants.IsChecked)) {
                customerLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
                customerLabel.setTextColor(getResources().getColor(R.color.white));
                labourLabel.setTextColor(getResources().getColor(R.color.white));
                groupAdminLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                getLoginStatus = "groupAdmin";
                etEmail.setText(SharedPrefManager.getUserEmail(Login.this, Constrants.Email));
                etPassword.setText(SharedPrefManager.getUserPassword(Login.this, Constrants.Password));
                rememberCheck.setChecked(true);
            } else {
                customerLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
                labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
                customerLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                labourLabel.setTextColor(getResources().getColor(R.color.white));
                groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
                getLoginStatus = "customer";
                rememberCheck.setChecked(false);
            }
            if (SharedPrefManager.getInstance(Login.this).getIsLoggedIn(false)) {
                Intent intent = new Intent(Login.this, HomeAdminActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}
