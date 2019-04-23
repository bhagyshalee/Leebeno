package leebeno.com.leebeno.CustomerPart.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AccountSetting;
import leebeno.com.leebeno.AdminPart.UpdateProfileAdmin;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.UpdateProfileCustomer;
import leebeno.com.leebeno.LabourPart.UpdateProfileLabour;
import leebeno.com.leebeno.Login;
import leebeno.com.leebeno.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static leebeno.com.leebeno.Api.WebUrls.changeLanguage;
import static leebeno.com.leebeno.Services.Config.editor;
import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;


public class SettingCustomerFragment extends Fragment implements View.OnClickListener, WebCompleteTask {

    @Bind(R.id.txtProfile)
    TextView txtProfile;
    @Bind(R.id.txtAccountSetting)
    TextView txtAccountSetting;
    @Bind(R.id.toggleNotification)
    ToggleButton toggleNotification;
    @Bind(R.id.txtChangeLang)
    TextView txtChangeLang;
    @Bind(R.id.txtCloseAccount)
    TextView txtCloseAccount;
    Dialog dialog;
    List<String> arrayListLang;
    String selectedLang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting_customer, container, false);
        ButterKnife.bind(this, v);
        txtProfile.setOnClickListener(this);
        txtAccountSetting.setOnClickListener(this);
        txtChangeLang.setOnClickListener(this);
        txtCloseAccount.setOnClickListener(this);
        if (SharedPrefManager.getNotyMute(getContext(), Constrants.notyMute)) {
            toggleNotification.setChecked(true);
        } else {
            toggleNotification.setChecked(false);
        }

        toggleNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                muteNotyMethod(isChecked);
            }
        });

        return v;
    }


    private void muteNotyMethod(boolean getvisi) {
        showProgress(getActivity());
        HashMap hashMap = new HashMap();
        hashMap.put("notyMute", getvisi);

        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JsonObject> call = apiInterface.getNotyMute(SharedPrefManager.getAccessToken(getActivity(), Constrants.AccessToken), hashMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                close();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("fdvvbsjkdsv", jsonObject.toString() + "");
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
                        JSONObject jsonObjectdata = jsonObjSuccess.getJSONObject("data");
//                        showToast(getActivity(), jsonObjectMsg.getString("replyMessage"));
                        boolean getNoty=jsonObjectdata.getBoolean("notyMute");
                        SharedPrefManager.setNotyMute(getActivity(), Constrants.notyMute,getNoty);

                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(getActivity(), "" + jsonObjError.getString("message"));
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
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == txtProfile) {
            if (SharedPrefManager.getUserStatus(getContext(), Constrants.UserStatus).compareTo("customer") == 0) {
                Intent intent = new Intent(getActivity(), UpdateProfileCustomer.class);
                startActivity(intent);
            }
            if (SharedPrefManager.getUserStatus(getContext(), Constrants.UserStatus).compareTo("labour") == 0) {
                Intent intent = new Intent(getActivity(), UpdateProfileLabour.class);
                startActivity(intent);
            }
            if (SharedPrefManager.getUserStatus(getContext(), Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                Intent intent = new Intent(getActivity(), UpdateProfileAdmin.class);
                startActivity(intent);
            }

        }
        if (view == txtAccountSetting) {
            Intent intent = new Intent(getActivity(), AccountSetting.class);
            startActivity(intent);
        }
        if (view == txtChangeLang) {
            try {
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popup_choose_language);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                final ImageView crossIm = (ImageView) dialog.findViewById(R.id.crossPopup);
                final TextView title = (TextView) dialog.findViewById(R.id.titlePopup);
                final Spinner spilLang = (Spinner) dialog.findViewById(R.id.etSpinLanguage);
                final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                dialog.show();


                crossIm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                String[] plants = new String[]{
                        "Language",
                        getResources().getString(R.string.english),
                        getResources().getString(R.string.vietnamese)

                };
                arrayListLang = new ArrayList<>(Arrays.asList(plants));
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.design_spinner, R.id.textSpinner, arrayListLang) {
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
                spilLang.setAdapter(spinnerArrayAdapter);


                spilLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        String selectedItem = adapterView.getItemAtPosition(position).toString();
                        selectedLang = adapterView.getSelectedItem().toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedLang.equals("Language")) {
                            showToast(getActivity(), "Please Select Language");
                        } else {

                            editor = getActivity().getSharedPreferences("LANGUAGE", MODE_PRIVATE).edit();
                            if (selectedLang.equals("English") || selectedLang.equals("Anh")) {
                                dialog.dismiss();
                                updateLanguage("en");
                               /* Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);

                                editor.putBoolean("selectLanguage", true);
                                editor.apply();
                                editor.commit();*/
                            }
                            if (selectedLang.equals("Vietnamese") || selectedLang.equals("Tiếng Việt")) {

                                dialog.dismiss();
                                updateLanguage("vi");


                               /* editor.putBoolean("selectLanguage", true);
                                editor.apply();
                                editor.commit();*/
                                //showToast(getActivity(), "Still We don't have content");

                            }
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("gregreggre", e + "");
            }

        }
        if (view == txtCloseAccount) {
            try {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.deactivate_account);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                final ImageView crossPopup = (ImageView) dialog.findViewById(R.id.crossPopup);
                final EditText etFeedback = (EditText) dialog.findViewById(R.id.etFeedback);
                final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                dialog.show();


                crossPopup.setOnClickListener(new View.OnClickListener() {
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

    private void updateLanguage(String languageCode) {

        HashMap hashMap = new HashMap();
        hashMap.put("ln", languageCode);
        // new WebTask(UpdateProfileCustomer.this, WebUrls.BASE_URL + updateCustomerProfileApi, hashMap, UpdateProfileCustomer.this, RequestCode.CODE_UpdateCustomerProfile, 1);

        new WebTask(getActivity(), WebUrls.BASE_URL + changeLanguage, hashMap, SettingCustomerFragment.this, RequestCode.CODE_ChangeLanguage, 1);


        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        //LocaleHelper.setLocale(getActivity(), languageCode);

    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_ChangeLanguage) {
                Log.e("responseSS", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                    showToast(getActivity(), "" + jsonMsg.getString("replyMessage"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e);
        }
    }
}
