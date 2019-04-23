package leebeno.com.leebeno.AdminPart;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static leebeno.com.leebeno.Services.Utility.showToast;

public class UpdateProfileAdmin extends AppCompatActivity implements View.OnClickListener ,WebCompleteTask {

    File compressedImage;
    private static final int PICK_Camera_IMAGE = 100;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.profilePicUpdate)
    ImageView profilePicUpdate;
    Uri imageUri;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.etAddress)
    EditText etAddress;
    @Bind(R.id.etStreet)
    EditText etStreet;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.etGroupTitle)
    EditText etGroupTitle;
    @Bind(R.id.profile_edit_t)
    TextView profile_edit_t;

    @Bind(R.id.admin_1)
    ImageView admin_1;
    @Bind(R.id.admin_2)
    ImageView admin_2;

    double latitude, longitude;
    boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_update_profile_admin);
        ButterKnife.bind(this);

        backSignUp.setOnClickListener(this);
        profilePicUpdate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etAddress.setOnClickListener(this);


        profilePicUpdate.setClickable(false);
        etName.setEnabled(false);
        etGroupTitle.setEnabled(false);
        etEmail.setEnabled(false);
        etDescription.setEnabled(false);
        etAddress.setEnabled(false);
        etStreet.setEnabled(false);
        btnSubmit.setClickable(false);

        profile_edit_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit){
                    profilePicUpdate.setClickable(false);
                    etName.setEnabled(false);
                    etGroupTitle.setEnabled(false);
                    etEmail.setEnabled(false);
                    etDescription.setEnabled(false);
                    etAddress.setEnabled(false);
                    etStreet.setEnabled(false);
                    btnSubmit.setClickable(false);
                    GetProfileMothed();
                    edit = false;
                }else {
                    profilePicUpdate.setClickable(true);
                    etName.setEnabled(true);
                    etGroupTitle.setEnabled(true);
                    etEmail.setEnabled(true);
                    etDescription.setEnabled(true);
                    etAddress.setEnabled(true);
                    etStreet.setEnabled(true);
                    btnSubmit.setClickable(true);
                    edit = true;
                }

            }
        });
        GetProfileMothed();
        Log.d("Langu", SharedPrefManager.getLangId(UpdateProfileAdmin.this, Constrants.UserLang));


    }

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

    public  void GetProfileMothed(){
        HashMap objectNew = new HashMap();
        new WebTask(UpdateProfileAdmin.this, WebUrls.BASE_URL+WebUrls.getProfileApi,objectNew,UpdateProfileAdmin.this, RequestCode.CODE_getProfileInfo,0);

    }

    @Override
    public void onClick(View v) {
        if (v == etAddress) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, RequestCode.AUTOPLACE_CODE);
            } catch (Exception e) {
                Log.e("vfkldfnv", "" + e);
            }
        }

        if (v == btnSubmit) {
            if (etName.getText().toString().trim().isEmpty()) {
                etName.setError(getResources().getString(R.string.plz_ent_ur_name
                ));
                etName.requestFocus();
            } else if (etName.getText().toString().trim().length() >= 20) {
                etName.setError("length is too large. only 20 character");
                etName.requestFocus();
            } else if (etGroupTitle.getText().toString().trim().isEmpty()) {
                etGroupTitle.setError(getResources().getString(R.string.title));
                etGroupTitle.requestFocus();
            } else if (etDescription.getText().toString().trim().isEmpty()) {
                etDescription.setError(getResources().getString(R.string.plz_ent_des));
                etDescription.requestFocus();
            } else if (etAddress.getText().toString().trim().isEmpty()) {
                etAddress.setError(getResources().getString(R.string.address));
                etAddress.requestFocus();
            } else if (etStreet.getText().toString().trim().isEmpty()) {
                etStreet.setError(getResources().getString(R.string.ent_str_or_house_no));
                etStreet.requestFocus();
            } else {


                JSONObject subObject = new JSONObject();
//                JSONObject jsonObject = new JSONObject();
                JSONObject latlong = new JSONObject();
                try {
                    latlong.put("lat", latitude);
                    latlong.put("lng", longitude);
                    subObject.put("value", "" + etAddress.getText().toString().trim());
                    subObject.put("location", latlong);
                    subObject.put("street", "" + etStreet.getText().toString().trim());
                    //jsonObject.put("address", subObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HashMap hashMap = new HashMap();
                hashMap.put("fullName", "" + etName.getText().toString());
                hashMap.put("title", "" + etGroupTitle.getText().toString());
                hashMap.put("description", "" + etDescription.getText().toString());
                hashMap.put("address", "" + subObject);
                //Log.e("postData", "" + hashMap);
                //new WebTask(UpdateProfileCustomer.this, WebUrls.BASE_URL + updateCustomerProfileApi, hashMap, UpdateProfileCustomer.this, RequestCode.CODE_UpdateCustomerProfile, 1);

                new WebTask(UpdateProfileAdmin.this, WebUrls.BASE_URL + WebUrls.updateAdminProfileApi, hashMap, UpdateProfileAdmin.this, RequestCode.CODE_UpdateAdminProfile, 1);

            }
        }
        if (v == backSignUp) {
            finish();
        }
        if (v == profilePicUpdate) {
            try {
                final Dialog dialog = new Dialog(UpdateProfileAdmin.this);
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
                            if (ActivityCompat.checkSelfPermission(UpdateProfileAdmin.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UpdateProfileAdmin.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
                            if (ActivityCompat.checkSelfPermission(UpdateProfileAdmin.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UpdateProfileAdmin.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
        if (requestCode == 200) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/leebeno");

                    new ImageCompressionAsyncTask(UpdateProfileAdmin.this).execute(data.getData().toString(),
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

                    new ImageCompressionAsyncTask(UpdateProfileAdmin.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_UpdateAdminProfile) {
                Log.e("responseSS", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    JSONObject dataObj = jsonObjSuccess.optJSONObject("data");

                    SharedPrefManager.setUserPic(UpdateProfileAdmin.this,Constrants.UserPic,WebUrls.BASE_URL+ dataObj.optString("image"));
                    SharedPrefManager.setUserName(UpdateProfileAdmin.this,Constrants.UserName,dataObj.optString("fullName"));
                    JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                    showToast(UpdateProfileAdmin.this, "" + jsonMsg.getString("replyMessage"));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (taskcode == RequestCode.CODE_getProfileInfo){
                Log.e("responseProfile", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    JSONObject dataObj = jsonObjSuccess.optJSONObject("data");
                    etName.setText(dataObj.optString("fullName"));
                    etEmail.setText(dataObj.optString("email"));
                    etGroupTitle.setText(dataObj.optString("title"));
                    etDescription.setText(dataObj.optString("description"));
                    etAddress.setText(dataObj.optJSONObject("address").optString("value"));
                    etStreet.setText(dataObj.optJSONObject("address").optString("street"));

                    SharedPrefManager.setUserPic(UpdateProfileAdmin.this,Constrants.UserPic,WebUrls.BASE_URL+dataObj.optString("image"));
                    Picasso.get().load(WebUrls.BASE_URL+dataObj.optString("image"))
                            .fit()
                            .placeholder(R.drawable.user_).into(profilePicUpdate);

                    String image1,image2;
                    JSONArray docsArray = dataObj.getJSONArray("docs");
                    image1 = WebUrls.BASE_URL+docsArray.optString(0);
                    image2 = WebUrls.BASE_URL+docsArray.optString(1);
                    Picasso.get().load(image1)
                            .fit()
                            .placeholder(R.drawable.user_).into(admin_1);
                    Picasso.get().load(image2)
                            .fit()
                            .placeholder(R.drawable.user_).into(admin_2);

                    JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
//                    showToast(UpdateProfileAdmin.this, "" + jsonMsg.getString("replyMessage"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e);
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
            Utility.showProgress(UpdateProfileAdmin.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(UpdateProfileAdmin.this).compress(params[0], new File(params[1]));

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UpdateProfileAdmin.this.getContentResolver(), compressUri);

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
        MultipartBody.Part filePartmultipleImages,filePartmultipleImagesPro;
        RequestBody requestBodyProfile = RequestBody.create(MediaType.parse("image/*"),  compressedImage);
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
                SharedPrefManager.getLangId(UpdateProfileAdmin.this, Constrants.UserLang),
                SharedPrefManager.getInstance(UpdateProfileAdmin.this).getAccessToken(),
                filePartmultipleImagesPro);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                        dismissProgressBar();
                if (response.isSuccessful()) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.body().toString());
                        Log.d("Tag","response_body"+  response.body().toString());
                        JSONObject successObj = obj.optJSONObject("success");
                        JSONObject dataObj = successObj.optJSONObject("data");
                        JSONObject msgObj = successObj.optJSONObject("msg");
                        String image = dataObj.optString("image");
                        if (msgObj.optString("replyCode").compareTo("Success") == 0) {
                            try {
                                SharedPrefManager.setUserPic(UpdateProfileAdmin.this,Constrants.UserPic,WebUrls.BASE_URL+image);
                                Toast.makeText(UpdateProfileAdmin.this, msgObj.getString("replyMessage"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProfileAdmin.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("fail response.." + t.toString());

            }
        });
    }
}
