package leebeno.com.leebeno.LabourPart;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.CreateNewLabour;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.Login;
import leebeno.com.leebeno.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class MenuallyLabourLogin extends AppCompatActivity {


    @Bind(R.id.titleLabourName)
    TextView titleLabourName;
    @Bind(R.id.layoutEditProfile)
    RelativeLayout layoutEditProfile;
    @Bind(R.id.LayoutViewMsg)
    RelativeLayout LayoutViewMsg;
    @Bind(R.id.logoutImg)
    ImageView logoutImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menually_labour_login);
        ButterKnife.bind(this);
        titleLabourName.setText("Webcome "+SharedPrefManager.getUserName(MenuallyLabourLogin.this, Constrants.UserName));

        logoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(MenuallyLabourLogin.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.show_msg_trans);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                    final TextView titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
                    final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                    dialog.show();

                    btnSubmit.setText(getResources().getString(R.string.yes));
                    btnCancel.setText(getResources().getString(R.string.no));
                    titlePopup.setText(getResources().getString(R.string.logout_confirmation_text));

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                          /*  Intent intent = new Intent(MenuallyLabourLogin.this, Login.class);
                            Toast.makeText(MenuallyLabourLogin.this, "Logout SuccessFully ", Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(MenuallyLabourLogin.this).storeIsLoggedIn(false);
                            startActivity(intent);
                            finish();*/
                            logoutUser();
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
        });

        layoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuallyLabourLogin.this, UpdateProfileLabour.class);
                intent.putExtra("manually",true);
                startActivity(intent);
            }
        });
    }

    private void logoutUser() {
        showProgress(MenuallyLabourLogin.this);
        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("jobId", jobId);
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JsonObject> call = apiInterface.logoutService(SharedPrefManager.getAccessToken(MenuallyLabourLogin.this, Constrants.AccessToken),SharedPrefManager.getLangId(MenuallyLabourLogin.this, Constrants.UserLang));
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
                        showToast(MenuallyLabourLogin.this, "" +jsonObjError.getJSONObject("msg").getString("replyMessage"));
//                        SharedPrefManager.setUserStatus(HomeLabourActivity.this, Constrants.UserStatus,"");

                        SharedPrefManager.getInstance(MenuallyLabourLogin.this).storeIsLoggedIn(false);
                        Intent intent = new Intent(MenuallyLabourLogin.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                       /* Intent intent = new Intent(HomeLabourActivity.this, Login.class);
                        Toast.makeText(HomeLabourActivity.this,"Logout SuccessFully ", Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(HomeLabourActivity.this).storeIsLoggedIn(false);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(MenuallyLabourLogin.this, "" + jsonObjError.getString("message"));
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

}
