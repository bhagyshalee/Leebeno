package leebeno.com.leebeno;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.SharedPrefManager;

import static leebeno.com.leebeno.Api.WebUrls.changePassword;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class AccountSetting extends AppCompatActivity implements WebCompleteTask {


    @Bind(R.id.txtChangePayment)
    TextView txtChangePayment;
    @Bind(R.id.txtChangePassword)
    TextView txtChangePassword;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    Dialog  dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog= new Dialog(AccountSetting.this);
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
                            } else if (!SharedPrefManager.getInstance(AccountSetting.this).CheckPassword(etNewPassword.getText().toString())){
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

                                new WebTask(AccountSetting.this, WebUrls.BASE_URL+changePassword,hashMap,AccountSetting.this, RequestCode.CODE_ChangePassword,1);

                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
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
                    showToast(AccountSetting.this, "" + jsonMsg.getString("replyMessage"));
                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e);
        }
    }
}
