package leebeno.com.leebeno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.HomeAdminActivity;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.LabourPart.HomeLabourActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class RatingReview extends AppCompatActivity {

    @Bind(R.id.backSignUp)
    ImageView backSignUp;

    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.etReview)
    EditText etReview;
    double ratingValue;
    String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_review);
        ButterKnife.bind(this);

        jobId = getIntent().getStringExtra("jobId");
        Log.e("dfgdf", "" + jobId);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingValue = rating;

            }
        });
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(RatingReview.this);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("jobId", jobId);
                jsonObject.addProperty("userRating", ratingValue);
                jsonObject.addProperty("comment", "" + etReview.getText().toString().trim());
                ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
                Call<JsonObject> call = apiInterface.giveRating(SharedPrefManager.getAccessToken(RatingReview.this, Constrants.AccessToken),SharedPrefManager.getLangId(RatingReview.this, Constrants.UserLang), jsonObject);
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
                                showToast(RatingReview.this, "" +jsonObjError.getJSONObject("msg").getString("replyMessage"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (SharedPrefManager.getUserStatus(RatingReview.this, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                                Intent intent = new Intent(RatingReview.this, HomeAdminActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            if (SharedPrefManager.getUserStatus(RatingReview.this, Constrants.UserStatus).compareTo("labour") == 0) {
                                Intent intent = new Intent(RatingReview.this, HomeLabourActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            if (SharedPrefManager.getUserStatus(RatingReview.this, Constrants.UserStatus).compareTo("customer") == 0) {
                                Intent intent = new Intent(RatingReview.this, HomeCustomerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                JSONObject jsonObjError = jsonObject.getJSONObject("error");
                                showToast(RatingReview.this, "" + jsonObjError.getString("message"));
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
        });

    }
}
