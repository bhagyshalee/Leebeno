package leebeno.com.leebeno;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import leebeno.com.leebeno.AdminPart.HomeAdminActivity;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.LabourPart.HomeLabourActivity;
import leebeno.com.leebeno.LabourPart.MenuallyLabourLogin;
import leebeno.com.leebeno.Services.NotificationClasses.MyFirebaseMessegingService;

import java.util.Locale;

import static leebeno.com.leebeno.Services.Config.editor;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class SpleshScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    Dialog dialog;

    String jobId = null, title, message,requestId=null,groupId=null,walletId=null;
    static int PopupShow=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splesh_screen);

        SharedPreferences shared = getSharedPreferences("LANGUAGE", MODE_PRIVATE);
        Boolean channel = (shared.getBoolean("selectLanguage", false));
        Log.e("selectLanguage",""+channel);

//        if (channel) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (getIntent().getExtras() != null) {
                        jobId = getIntent().getStringExtra("jobId");
                        message = getIntent().getStringExtra("body");
                        title = getIntent().getStringExtra("title");
                        requestId = getIntent().getStringExtra("requestId");
                        groupId = getIntent().getStringExtra("groupId");
                        walletId = getIntent().getStringExtra("walletId");
                        Log.e("SpleshLogin", "" + message + " " + jobId + " " + title + " " + SharedPrefManager.getUserStatus(SpleshScreen.this, Constrants.UserStatus));
                    }
                    if (SharedPrefManager.getInstance(SpleshScreen.this).getIsLoggedIn(false)){
                        if (SharedPrefManager.getUserStatus(SpleshScreen.this, Constrants.UserStatus).compareTo("customer") == 0) {
                            Intent i = new Intent(SpleshScreen.this, HomeCustomerActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                        else if (SharedPrefManager.getUserStatus(SpleshScreen.this, Constrants.UserStatus).compareTo("labour") == 0) {


                            if (SharedPrefManager.getLoginType(SpleshScreen.this,Constrants.loginType).compareTo("System")==0) {
                                Intent i = new Intent(SpleshScreen.this, HomeLabourActivity.class);

                                if (jobId!=null) {
                                    i.putExtra("jobId", "" + jobId);
//                                i.putExtra("jobStatus", "pending");
                                }

                                if (requestId!=null)
                                {
                                    i.putExtra("requestId", "" + requestId);
                                }
                                if (groupId!=null)
                                {
                                    i.putExtra("groupId", "" + groupId);
                                }
                                if (walletId!=null)
                                {
                                    i.putExtra("walletId", "" + walletId);
                                }

                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }else
                            {
                                Intent i = new Intent(SpleshScreen.this, MenuallyLabourLogin.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
//                                Intent intent = new Intent(SpleshScreen.this, MenuallyLabourLogin.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                finish();
                            }

                        }
                        else  if (SharedPrefManager.getUserStatus(SpleshScreen.this, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                            Intent i = new Intent(SpleshScreen.this, HomeAdminActivity.class);

                            if (jobId!=null) {
                                i.putExtra("jobId", "" + jobId);
//                                i.putExtra("jobStatus", "pending");
                            }
                            if (requestId!=null)
                            {
                                i.putExtra("requestId", "" + requestId);
                            }
                            if (groupId!=null)
                            {
                                i.putExtra("groupId", "" + groupId);
                            }
                            if (walletId!=null)
                            {
                                i.putExtra("walletId", "" + walletId);
                            }

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    } else {

                        Intent intent = new Intent(SpleshScreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);

       /* } else {
            if (PopupShow==0)
            {
                selectLanguage();
            }else
            {
                Intent intent = new Intent(SpleshScreen.this, Login.class);
                startActivity(intent);
                finish();
            }

        }*/

    }

    private void selectLanguage() {
        try {
            dialog = new Dialog(SpleshScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setContentView(R.layout.popup_select_language);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
            dialog.show();
            PopupShow=1;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    editor = getSharedPreferences("LANGUAGE", MODE_PRIVATE).edit();
                    editor.putBoolean("selectLanguage", true);
                    editor.apply();
                    editor.commit();
                    RadioButton radioHome = (RadioButton) dialog.findViewById(selectedId);

                    String strUsertType = radioHome.getText().toString().trim();

                    if (strUsertType.equals("English") || strUsertType.equals("Anh")) {
                        dialog.dismiss();
                        updateLanguage("en");
                       /* Intent intent = new Intent(SpleshScreen.this, Login.class);
                        startActivity(intent);
                        finish();*/

                    }
                    if (strUsertType.equals("Vietnamese") || strUsertType.equals("Tiếng Việt")) {

                       // dialog.dismiss();

                        /*Intent intent = new Intent(SpleshScreen.this, Login.class);
                        startActivity(intent);
                        finish();
*/


                      /*  editor.putBoolean("selectLanguage", true);
                        editor.apply();
                        editor.commit();*/
                        updateLanguage("en");
                     /*   Intent intent = new Intent(SpleshScreen.this, Login.class);
                        startActivity(intent);
                        finish();*/

                        showToast(SpleshScreen.this,"Still We don't have content");

                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("nsdnvnsv ",e+"");
        }
    }

    private void updateLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        editor.putBoolean("selectLanguage", true);
        editor.apply();
        editor.commit();

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        SharedPrefManager.setLangId(SpleshScreen.this, Constrants.UserLang,languageCode);
        Intent intent = new Intent(SpleshScreen.this, Login.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
