package leebeno.com.leebeno;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Common.SharedPrefManager;

import static leebeno.com.leebeno.Services.Utility.progressDialog;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener,WebCompleteTask {
    @Bind(R.id.backForgot)
    ImageView backForgot;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.btnVerify)
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        backForgot.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnVerify) {
           forgotPasswordMethod();
//            finish();
        }
        if (view == backForgot) {
            finish();
        }
    }

    private void forgotPasswordMethod() {
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError(getString(R.string.plz_ent_email));
            etEmail.requestFocus();
        }else if (!SharedPrefManager.getInstance(ForgetPassword.this).isValidEmail(etEmail.getText().toString())){
            etEmail.setError(getString(R.string.email_not_valid));
            etEmail.requestFocus();
        } else {
            HashMap objectNew = new HashMap();
            objectNew.put("realm", "user_");
            objectNew.put("mobile", etEmail);

          //  new WebTask(ForgetPassword.this, WebUrls.BASE_URL + WebUrls.resetPassRequest, objectNew, ForgetPassword.this, RequestCode.CODE_ResetPassRequest, 1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode)
    {
        Log.d("response",response);
        if (taskcode== RequestCode.CODE_ResetPassRequest)
        {
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();


                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.optJSONObject("success");
                JSONObject data = success.optJSONObject("data");
                String adminApproval = data.optString("adminApproval");
                String realm = data.optString("realm");
                String id = data.optString("id");
                String fullName = data.optString("fullName");
                String firstName = data.optString("firstName");
                String lastName = data.optString("lastName");
                String address = data.optJSONObject("address").optString("address");
                String address_lat = data.optJSONObject("address").optJSONObject("location").optString("lat");
                String address_lng = data.optJSONObject("address").optJSONObject("location").optString("lng");
                String mobile = data.optString("mobile");
                String createdAt = data.optString("createdAt");
                String updatedAt = data.optString("updatedAt");
                Boolean mobileVerified = data.optBoolean("mobileVerified");

                JSONObject signupOtp = data.optJSONObject("signupOtp");
                String createdAt_ex = signupOtp.optString("createdAt");
                String expireAt_ex = signupOtp.optString("expireAt");
                String otp_ex = signupOtp.optString("otp");
                Log.d("otp_exp_ForgetPassword",otp_ex);

                JSONObject passwordOtp = data.optJSONObject("passwordOtp");
                String createdAt_n = passwordOtp.optString("createdAt");
                String expireAt_n = passwordOtp.optString("expireAt");
                Integer otp_n = passwordOtp.getInt("otp");
                Log.d("otp_new_ForgetPassword", String.valueOf(otp_n));

                JSONObject msg = success.optJSONObject("msg");
                String replyCode = msg.optString("replyCode");
                String replyMessage = msg.optString("replyMessage");

                SharedPrefManager.getInstance(getApplicationContext()).storeRegPeopleOtp(otp_n);
                SharedPrefManager.getInstance(getApplicationContext()).storeSignPeopleId(id);
                if (realm.equals("user_"))
                {
                    Intent intent = new Intent(ForgetPassword.this, Login.class);
//                    Toast.makeText(ForgetPassword.this, getString(R.string.otp_sent), Toast.LENGTH_SHORT).show();
//                    Pair[] pairs = new Pair[3];
//                    pairs[0] = new Pair<View,String>(logo_login,"logoTransition");
//                    pairs[1] = new Pair<View,String>(mobileno_et,"firsetTransition");
//                    pairs[2] = new Pair<View,String>(submit_forgot_btn,"btnTransition");

//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPassword.this,pairs);

//                    startActivity(intent,options.toBundle());
                    startActivity(intent);
                }else {
                    Toast.makeText(ForgetPassword.this,R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
