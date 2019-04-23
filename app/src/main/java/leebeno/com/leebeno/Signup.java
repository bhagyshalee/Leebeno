package leebeno.com.leebeno;

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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.AddSkillAdapter;
import leebeno.com.leebeno.Adapters.IdProofImagesAdapter;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.DataPart;
import leebeno.com.leebeno.Common.MySpinner;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Common.VolleyMultipartRequest;
import leebeno.com.leebeno.CustomerPart.CreateJobActivity;
import leebeno.com.leebeno.LabourPart.UpdateProfileLabour;
import leebeno.com.leebeno.Services.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static leebeno.com.leebeno.Api.RequestCode.CODE_createJob;
import static leebeno.com.leebeno.Services.Utility.progressDialog;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class Signup extends AppCompatActivity implements View.OnClickListener,WebCompleteTask {

    private static final int PICK_Camera_IMAGE = 100;
    int REQUEST_CAMERA_PERMISSION = 1;

    @Bind(R.id.etUserName)
    EditText etUserName;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @Bind(R.id.checkboxTerm)
    CheckBox checkboxTerm;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.customerLabel)
    TextView customerLabel;
    @Bind(R.id.labourLabel)
    TextView labourLabel;
    @Bind(R.id.groupAdminLabel)
    TextView groupAdminLabel;
    @Bind(R.id.etYourLocation)
    EditText etYourLocation;
    @Bind(R.id.etStreet)
    EditText etStreet;
    @Bind(R.id.cardAdmin)
    CardView cardAdmin;
    @Bind(R.id.profilePic)
    ImageView profilePic;
    @Bind(R.id.etDescriptionLabour)
    EditText etDescriptionLabour;
    @Bind(R.id.etSkillsLabour)
    MySpinner etSkillsLabour;

    @Bind(R.id.etNameA)
    EditText etNameA;
    @Bind(R.id.etTitleA)
    EditText etTitleA;
    @Bind(R.id.etUserEmailA)
    EditText etUserEmailA;
    @Bind(R.id.etPasswordA)
    EditText etPasswordA;
    @Bind(R.id.etConfirmPasswordA)
    EditText etConfirmPasswordA;
    @Bind(R.id.etYourLocationA)
    EditText etYourLocationA;
    @Bind(R.id.etStreetA)
    EditText etStreetA;
    @Bind(R.id.etDescriptionA)
    EditText etDescriptionA;
    @Bind(R.id.uploadImageLayout)
    LinearLayout uploadImageLayout;
    @Bind(R.id.uploadImage)
    ImageView uploadImage;
    @Bind(R.id.uploadImageText)
    TextView uploadImageText;
    @Bind(R.id.customerLayout)
    LinearLayout customerLayout;
    @Bind(R.id.descriptionIdProof)
    TextView descriptionIdProof;
    @Bind(R.id.profilePicA)
    ImageView profilePicA;
    String getLoginStatus = "customer";
    Uri imageUri,imageUriIDProof;

    File fileImage, compressedImage,Profile_file;
    String strCompressedFileImage;

    @Bind(R.id.recyclerSkill)
    RecyclerView recyclerSkill;
    AddSkillAdapter addSkillAdapter;
    List<String> listSelectSkill;
    ArrayList<String> fileDoc=new ArrayList<>();
    List<Bitmap> listBitmap;

    @Bind(R.id.profilePicLabour)
    ImageView profilePicLabour;
    @Bind(R.id.imageRecycler)
    RecyclerView imageRecycler;
    @Bind(R.id.etUserNameLabour)
    EditText etUserNameLabour;
    @Bind(R.id.etUserEmailLabour)
    EditText etUserEmailLabour;
    @Bind(R.id.etPasswordLabour)
    EditText etPasswordLabour;
    @Bind(R.id.etConfirmPasswordLabour)
    EditText etConfirmPasswordLabour;
    @Bind(R.id.etYourLocationLabour)
    EditText etYourLocationLabour;
    @Bind(R.id.etStreetLabour)
    EditText etStreetLabour;
    @Bind(R.id.etMinimumPayPerHour)
    EditText etMinimumPayPerHour;
    @Bind(R.id.btnSubmitLabour)
    Button btnSubmitLabour;

    @Bind(R.id.cardLabour)
    CardView cardLabour;
    IdProofImagesAdapter imagesAdapter;


    @Bind(R.id.btnSubmitA)
    Button btnSubmitA;


    ArrayList<String> listSelectedSkillsid;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    JSONObject addressObj=new JSONObject();
    JSONObject data;
    String image_url;
    Bitmap bitmap;
    LatLng latLng;
    Boolean tremcheck = false;
    private static final String TAG = "Signup";
    List<String> listSkillsId = new ArrayList<>();
    public static List<String> listSkillsName;
    public static Boolean cl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_signup);

        listSelectSkill = new ArrayList<>();
        listSelectedSkillsid = new ArrayList<>();
        listBitmap = new ArrayList<>();

        ButterKnife.bind(this);
        recyclerSkill.setHasFixedSize(true);
        recyclerSkill.setNestedScrollingEnabled(true);
        recyclerSkill.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        imageRecycler.setHasFixedSize(true);
        imageRecycler.setNestedScrollingEnabled(true);
        imageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        backSignUp.setOnClickListener(this);
        customerLabel.setOnClickListener(this);
        labourLabel.setOnClickListener(this);
        groupAdminLabel.setOnClickListener(this);
        profilePic.setOnClickListener(this);
        uploadImageLayout.setOnClickListener(this);
        profilePicA.setOnClickListener(this);
        profilePicLabour.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


        GetSkillsMethod();


        etYourLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    /*AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build();*/
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            //.setFilter(typeFilter)
                            .build(Signup.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        etYourLocationLabour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    /*AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build();*/
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            //.setFilter(typeFilter)
                            .build(Signup.this);
                    startActivityForResult(intent, 50);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        etYourLocationA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    /*AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build();*/
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            //.setFilter(typeFilter)
                            .build(Signup.this);
                    startActivityForResult(intent, 51);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        btnSubmitLabour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LabourSignupMethod();
            }
        });

        btnSubmitA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSigupMethod();
            }
        });

    }
    @Override
    public void onClick(View view) {
        if (view==btnSubmit)
        {
            CustomerSigupMethod();
        }
        if (view == uploadImageLayout) {
            if (fileDoc.size()<2){
                try {
                    final Dialog dialog = new Dialog(Signup.this);
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
                                if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                                } else {

                                    selectCameraImageDoc();
                                    dialog.dismiss();
                                }
                            } else {
                                selectCameraImageDoc();
                                dialog.dismiss();
                            }
                        }
                    });
                    txtCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int currentAPIVersion = Build.VERSION.SDK_INT;
                            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                } else {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_PICK);
                                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 400);
//                startActivityForResult(i, 100);
                                }
                            } else {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_PICK);
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 400);
                            }
                        }
                    });

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(Signup.this,"You can select only 2 document",Toast.LENGTH_SHORT).show();
            }
        }
        if (view == profilePicA) {
            SelectProfileFromPopup();
        }
        if (view == profilePic) {
            SelectProfileFromPopup();
        }
        if (view == profilePicLabour) {
            SelectProfileFromPopup();
        }
        if (view == customerLabel) {

            customerLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
            labourLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            customerLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            labourLabel.setTextColor(getResources().getColor(R.color.white));
            groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
            customerLayout.setVisibility(View.VISIBLE);
            cardAdmin.setVisibility(View.GONE);
            cardLabour.setVisibility(View.GONE);
            getLoginStatus = "customer";

        }
        if (view == labourLabel) {
            customerLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            labourLabel.setBackground(getResources().getDrawable(R.drawable.white_circle_background));
            groupAdminLabel.setBackground(getResources().getDrawable(R.drawable.transperent_circle_background));
            customerLabel.setTextColor(getResources().getColor(R.color.white));
            labourLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            groupAdminLabel.setTextColor(getResources().getColor(R.color.white));
            customerLayout.setVisibility(View.GONE);
            cardAdmin.setVisibility(View.GONE);
            cardLabour.setVisibility(View.VISIBLE);
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
            customerLayout.setVisibility(View.GONE);
            cardAdmin.setVisibility(View.VISIBLE);
            cardLabour.setVisibility(View.GONE);

        }
        if (view == backSignUp) {
            finish();
        }
    }
    private void SelectProfileFromPopup() {
        try {
            final Dialog dialog = new Dialog(Signup.this);
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
                        if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                        if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
    private void selectCameraImageDoc() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUriIDProof = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriIDProof);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 10);
        startActivityForResult(intent, 300);
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
    public String getRealPathFromURI(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (IllegalStateException e){
                cursor.close();
                showToast(Signup.this,"Unsupported file");
            }catch (IllegalArgumentException e){
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    class ImageCompressionAsyncTaskDoc extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTaskDoc(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            imageRecycler.setVisibility(View.VISIBLE);
            Utility.showProgress(Signup.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {
                filePath = SiliCompressor.with(Signup.this).compress(params[0], new File(params[1]));
            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                strCompressedFileImage = s;
                compressedImage = new File(s);

                Uri compressUri = Uri.fromFile(compressedImage);
                try {
                    imageRecycler.setVisibility(View.VISIBLE);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Signup.this.getContentResolver(), compressUri);
//                    if (fileDoc.get(0).length()/1024)
                    listBitmap.add(bitmap);
                    fileDoc.add(strCompressedFileImage);
//                    if (fileDoc.size()<3){
                        Log.e("bererbwef",listBitmap.size()+"");
                        imagesAdapter = new IdProofImagesAdapter(fileDoc,listBitmap, Signup.this,imageRecycler);
                        imageRecycler.setAdapter(imagesAdapter);
//                    }else {
//                        Toast.makeText(Signup.this,"You can select only 2 document",Toast.LENGTH_SHORT).show();
//                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }

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
            Utility.showProgress(Signup.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(Signup.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                strCompressedFileImage = s;
                Profile_file = new File(s);
                Uri compressUri = Uri.fromFile(Profile_file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Signup.this.getContentResolver(), compressUri);
                    Log.e("rgwrbhfh",getLoginStatus+"");
                    if (getLoginStatus.equals("customer"))
                    {
                        profilePic.setImageBitmap(bitmap);
                    }
                    if (getLoginStatus.equals("labour")) {
                        profilePicLabour.setImageBitmap(bitmap);
                    }
                    if (getLoginStatus.equals("groupAdmin")) {
                        profilePicA.setImageBitmap(bitmap);
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }

// /*   @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 432 && resultCode == RESULT_OK && data != null) {
//
//            //getting the image Uri
//            Uri imageUri = data.getData();
//            try {
//                //getting bitmap object from uri
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//
//                //displaying selected image to imageview
//                profilePicA.setImageBitmap(bitmap);
//                listBitmap.add(bitmap);
//                Log.e("bererbwef",listBitmap.size()+"");
//                imagesAdapter = new IdProofImagesAdapter(listBitmap, Signup.this,imageRecycler);
//                imageRecycler.setAdapter(imagesAdapter);
//
//                //calling the method uploadBitmap to upload image
//              //  uploadBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                //System.out.println(numbers[0]);
                // if (numbers[0].equals("image")) {
                File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/drawim");

                if (fileImage.mkdirs() || fileImage.isDirectory()) {
                    new ImageCompressionAsyncTaskDoc(Signup.this).execute(data.getData().toString(),
                            fileImage.getPath());
                }
                //   }
            }
        } else if (requestCode == 300) {
            Uri selectedImageUri = null;
            String filePath = null;
            selectedImageUri = imageUriIDProof;

/* if (selectedImageUri != null) {
                        try {
                            String filemanagerstring = selectedImageUri.getPath();
                            String selectedImagePath = getPath(selectedImageUri);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Internal error",
                                    Toast.LENGTH_LONG).show();
                            Log.e(e.getClass().getName(), e.getMessage(), e);
                        }
                    }*/


            File  fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/drawim");

            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTaskDoc(Signup.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }

        }
        if (requestCode == 200) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/leebeno");

                    new ImageCompressionAsyncTask(Signup.this).execute(data.getData().toString(),
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

            fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/leebeno");
            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTask(Signup.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                etYourLocation.setText(place.getAddress());
                latLng = place.getLatLng();

//                Toast.makeText(this,place.getAddress()+"\n"+place.getLatLng(),Toast.LENGTH_SHORT).show();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user_ canceled the operation.
            }
        }
        if (requestCode == 50) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                etYourLocationLabour.setText(place.getAddress());
                latLng = place.getLatLng();

//                Toast.makeText(this,place.getAddress()+"\n"+place.getLatLng(),Toast.LENGTH_SHORT).show();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user_ canceled the operation.
            }
        }
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                etYourLocationA.setText(place.getAddress());
                latLng = place.getLatLng();

//                Toast.makeText(this,place.getAddress()+"\n"+place.getLatLng(),Toast.LENGTH_SHORT).show();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user_ canceled the operation.
            }
        }
    }
    private void GetSkillsMethod(){
        HashMap objectNew = new HashMap();
        new WebTask(Signup.this,WebUrls.BASE_URL+WebUrls.getAllSkills_api,objectNew,Signup.this,RequestCode.CODE_getskills,0);
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
                listSkillsName= new ArrayList<>();

                listSkillsName.add(0, getResources().getString(R.string.select_skills));
                listSkillsId.add(0, "0");

                for (int i = 0; i < jsonArray.length(); i++) {
                    listSkillsName.add(jsonArray.getJSONObject(i).optString("name"));
                    listSkillsId.add(jsonArray.getJSONObject(i).optString("id"));
                    // Log.e("vdregrehdhrty",jsonArray.getJSONObject(i).optString("name")+"  "+listSkillsName.get(i));
                }


                //arrayListSkill = new ArrayList<>(Arrays.asList(listSkillsName));
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Signup.this, R.layout.design_spinner, R.id.textSpinner, listSkillsName) {
                  /*  @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        } else {
                            return true;
                        }
                    }*/

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

            }
        } catch (Exception e) {

        }
    }

    private void spinnerclick(){
        etSkillsLabour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
              //  String selectedItem = adapterView.getItemAtPosition(position).toString();
                String selectedItemS = etSkillsLabour.getSelectedItem().toString();

                if (selectedItemS.equals(getResources().getString(R.string.select_skills))) {
                    // do your stuff
                } else {
                    if (listSelectSkill.size() == 0) {
                        listSelectSkill.add(selectedItemS);
                        listSelectedSkillsid.add(listSkillsId.get(position));
                        addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid,listSelectSkill,  Signup.this, recyclerSkill,etSkillsLabour);
                        recyclerSkill.setAdapter(addSkillAdapter);
                        recyclerSkill.setVisibility(View.VISIBLE);
                    } else {
                        if (listSelectSkill.contains(selectedItemS)) {
                            if (cl==false){
                                showToast(Signup.this, getResources().getString(R.string.select_already_select_skills));

                            }

                        } else{
                            listSelectSkill.add(selectedItemS);
                            listSelectedSkillsid.add(listSkillsId.get(position));
                            addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid,listSelectSkill,  Signup.this, recyclerSkill,etSkillsLabour);
                            recyclerSkill.setAdapter(addSkillAdapter);
                            recyclerSkill.setVisibility(View.VISIBLE);
                        }
                        cl=false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void CustomerSigupMethod(){
        if (TextUtils.isEmpty(etUserName.getText().toString())){
            etUserName.setError("Please enter the Name");
            etUserName.requestFocus();
        } else if (TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
        } else if (!SharedPrefManager.getInstance(Signup.this).isValidEmail(etEmail.getText().toString())){
            etEmail.setError("Email not valid");
            etEmail.requestFocus();
        } else if (etPassword.getText().length()<6){
            etPassword.setError("Password must have atleast 6 digit with one character and special character");
            etPassword.requestFocus();
        } else if (!SharedPrefManager.getInstance(Signup.this).CheckPassword(etPassword.getText().toString())){
            etPassword.setError("Password must have atleast 6 digit with one character and special character");
            etPassword.requestFocus();
        } else if (TextUtils.isEmpty(etConfirmPassword.getText().toString())){
            etConfirmPassword.setError("Please enter the confirm Password");
            etConfirmPassword.requestFocus();
        }else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            etConfirmPassword.setError("Password and confirm Password do not match");
            etConfirmPassword.requestFocus();
        }else if (TextUtils.isEmpty(etYourLocation.getText().toString())){
            etYourLocation.setError("Please enter your location");
            etYourLocation.requestFocus();
        }else if (TextUtils.isEmpty(etStreet.getText().toString())){
            etStreet.setError("Please enter your hours no/street/landmark");
            etStreet.requestFocus();
        }else if (!checkboxTerm.isChecked()){
            checkboxTerm.setError("Please accept term and condition");
            checkboxTerm.requestFocus();
        }else {
            data= new JSONObject();
            JSONObject locationobj=null;
            try {
                locationobj= new JSONObject();
                locationobj.put("lat",latLng.latitude);
                locationobj.put("lng", latLng.longitude);
                addressObj.put("value", etYourLocation.getText().toString().trim());
                addressObj.put("street", etStreet.getText().toString().trim());
                addressObj.put("location", locationobj);

                data.put("realm",getLoginStatus);
                data.put("fullName",etUserName.getText().toString().trim());
                data.put("email",etEmail.getText().toString().trim());
                data.put("password",etPassword.getText().toString().trim());
                data.put("address",addressObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (addressObj!=null && locationobj!=null && bitmap != null){
                uploadBitmap(bitmap);
            }else if (addressObj!=null && locationobj!=null){
                bitmap= BitmapFactory.decodeResource(Signup.this.getResources(), R.drawable.user);
                uploadBitmap(bitmap);
            }else {
                Toast.makeText(Signup.this,"Address or location may be Null please select address",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void LabourSignupMethod(){
        if (TextUtils.isEmpty(etUserNameLabour.getText().toString())){
            etUserNameLabour.setError(getString(R.string.plz_ent_ur_name));
            etUserNameLabour.requestFocus();
        }  else if (TextUtils.isEmpty(etUserEmailLabour.getText().toString())){
            etUserEmailLabour.setError(getString(R.string.plz_ent_email));
            etUserEmailLabour.requestFocus();
        } else if (!SharedPrefManager.getInstance(Signup.this).isValidEmail(etUserEmailLabour.getText().toString())){
            etUserEmailLabour.setError(getString(R.string.email_not_valid));
            etUserEmailLabour.requestFocus();
        } else if (etPasswordLabour.getText().length()<6){
            etPasswordLabour.setError(getString(R.string.pass_must_6));
            etPasswordLabour.requestFocus();
        } else if (!SharedPrefManager.getInstance(Signup.this).CheckPassword(etPasswordLabour.getText().toString())){
            etPasswordLabour.setError(getString(R.string.pass_must_6));
            etPasswordLabour.requestFocus();
        } else if (TextUtils.isEmpty(etConfirmPasswordLabour.getText().toString())){
            etConfirmPasswordLabour.setError(getString(R.string.plz_ent_cnf_pass));
            etConfirmPasswordLabour.requestFocus();
        }else if (!etPasswordLabour.getText().toString().equals(etConfirmPasswordLabour.getText().toString())){
            etConfirmPasswordLabour.setError(getString(R.string.pass_nd_cnf_pass_not));
            etConfirmPasswordLabour.requestFocus();
        }else if (TextUtils.isEmpty(etYourLocationLabour.getText().toString())){
            etYourLocationLabour.setError(getString(R.string.ent_ur_loc));
            etYourLocationLabour.requestFocus();
        }else if (TextUtils.isEmpty(etStreetLabour.getText().toString())){
            etStreetLabour.setError(getString(R.string.ent_str_or_house_no));
            etStreetLabour.requestFocus();
        }else if (TextUtils.isEmpty(etDescriptionLabour.getText().toString().trim())){
            etDescriptionLabour.setError(getString(R.string.plz_ent_des));
            etDescriptionLabour.requestFocus();
        }else if (TextUtils.isEmpty(etMinimumPayPerHour.getText().toString().trim())){
            etMinimumPayPerHour.setError(getString(R.string.mini_pay_per));
            etMinimumPayPerHour.requestFocus();
        } else if (listSelectSkill!=null && listSelectSkill.size()>0){
            data= new JSONObject();
            JSONObject locationobj=null;
            try {
                locationobj= new JSONObject();
                locationobj.put("lat",latLng.latitude);
                locationobj.put("lng", latLng.longitude);
                addressObj.put("value", etYourLocationLabour.getText().toString().trim());
                addressObj.put("street", etStreetLabour.getText().toString().trim());
                addressObj.put("location", locationobj);

                JSONArray ssk= new JSONArray();

                for (int i=0;i<listSelectedSkillsid.size();i++){
                    ssk.put(listSelectedSkillsid.get(i));
                }

                data.put("realm",getLoginStatus);
                data.put("fullName",etUserNameLabour.getText().toString().trim());
                data.put("email",etUserEmailLabour.getText().toString().trim());
                data.put("password",etPasswordLabour.getText().toString().trim());
                data.put("description",etDescriptionLabour.getText().toString().trim());
                data.put("pph",etMinimumPayPerHour.getText().toString().trim());
                data.put("skillIds",ssk);
                data.put("address",addressObj);
//                data.put("ln",SharedPrefManager.getLangId(Signup.this,Constrants.UserLang));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (addressObj!=null && locationobj!=null && bitmap != null){
                uploadBitmap(bitmap);
            }else if (addressObj!=null && locationobj!=null){
                bitmap= BitmapFactory.decodeResource(Signup.this.getResources(), R.drawable.user);
                uploadBitmap(bitmap);
            }else {
                Toast.makeText(Signup.this,"Address or location may be Null please select address",Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(Signup.this,R.string.atleastone,Toast.LENGTH_SHORT).show();
        }
    }
    private void AdminSigupMethod(){
        if (TextUtils.isEmpty(etNameA.getText().toString())){
            etNameA.setError(getString(R.string.plz_ent_ur_name));
            etNameA.requestFocus();
        }  else if (TextUtils.isEmpty(etUserEmailA.getText().toString())){
            etUserEmailA.setError(getString(R.string.plz_ent_email));
            etUserEmailA.requestFocus();
        } else if (!SharedPrefManager.getInstance(Signup.this).isValidEmail(etUserEmailA.getText().toString())){
            etUserEmailA.setError(getString(R.string.email_not_valid));
            etUserEmailA.requestFocus();
        } else if (etPasswordA.getText().length()<6){
            etPasswordA.setError(getString(R.string.pass_must_6));
            etPasswordA.requestFocus();
        } else if (!SharedPrefManager.getInstance(Signup.this).CheckPassword(etPasswordA.getText().toString())){
            etPasswordA.setError(getString(R.string.pass_must_6));
            etPasswordA.requestFocus();
        } else if (TextUtils.isEmpty(etConfirmPasswordA.getText().toString())){
            etConfirmPasswordA.setError("Please enter the confirm Password");
            etConfirmPasswordA.requestFocus();
        }else if (!etPasswordA.getText().toString().equals(etConfirmPasswordA.getText().toString())){
            etConfirmPasswordA.setError("Password and confirm Password do not match");
            etConfirmPasswordA.requestFocus();
        }else if (TextUtils.isEmpty(etYourLocationA.getText().toString())){
            etYourLocationA.setError("Please enter your location");
            etYourLocationA.requestFocus();
        }else if (TextUtils.isEmpty(etStreetA.getText().toString())){
            etStreetA.setError("Please enter your hours no/street/landmark");
            etStreetA.requestFocus();
        }else if (TextUtils.isEmpty(etDescriptionA.getText().toString())){
            etDescriptionA.setError("Please enter your email");
            etDescriptionA.requestFocus();
        }else {
            data= new JSONObject();
            JSONObject locationobj=null;
            try {
                locationobj= new JSONObject();
                locationobj.put("lat",latLng.latitude);
                locationobj.put("lng", latLng.longitude);
                addressObj.put("value", etYourLocationA.getText().toString().trim());
                addressObj.put("street", etStreetA.getText().toString().trim());
                addressObj.put("location", locationobj);

                data.put("realm",getLoginStatus);
                data.put("title",etTitleA.getText().toString().trim());
                data.put("email",etUserEmailA.getText().toString().trim());
                data.put("password",etPasswordA.getText().toString().trim());
                data.put("fullName",etNameA.getText().toString().trim());
                data.put("description",etDescriptionA.getText().toString().trim());
                data.put("address",addressObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (addressObj!=null && locationobj!=null){
                if (fileDoc.size()>0)
                {
                    if (bitmap==null){
                        bitmap= BitmapFactory.decodeResource(Signup.this.getResources(),
                                R.drawable.user);
                        //create a file to write bitmap data
                        Profile_file = new File(Signup.this.getCacheDir(), System.currentTimeMillis()+"");
                        try {
                            Profile_file.createNewFile();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                            byte[] bitmapdata = bos.toByteArray();

                            FileOutputStream fos = new FileOutputStream(Profile_file);
                            fos.write(bitmapdata);
//                        fos.flush();
//                        fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Uploading, please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    String jsonObjData = String.valueOf(data);
                    RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);

                    List<MultipartBody.Part> photos = new ArrayList<>();
                    MultipartBody.Part filePartmultipleImages,filePartmultipleImagesPro;
                    RequestBody requestBodyProfile = RequestBody.create(MediaType.parse("image/*"), Profile_file);
                    filePartmultipleImagesPro = MultipartBody.Part.createFormData("image", Profile_file.getName(), requestBodyProfile);

                    for (int i = 0; i < fileDoc.size(); i++) {
                        File file = null;
                        file = new File(String.valueOf(fileDoc.get(i)));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                        filePartmultipleImages = MultipartBody.Part.createFormData("docs", file.getName(), requestBody);
                        photos.add(filePartmultipleImages);
                    }

                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                    Call<JsonObject> call = getResponse.PostContract(
                            SharedPrefManager.getLangId(Signup.this, Constrants.UserLang),
                            photos,
                            filePartmultipleImagesPro,
                            objdata);

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                        dismissProgressBar();
                            if (response.isSuccessful()) {
                                JSONObject obj = null;
                                try {
                                    obj = new JSONObject(response.body().toString());
                                    Log.d(TAG,"response_body"+  response.body().toString());
                                    JSONObject successObj = obj.optJSONObject("success");
                                    JSONObject msgObj = successObj.optJSONObject("msg");

                                    if (msgObj.optString("replyCode").compareTo("Success") == 0) {
                                        try {
                                            Intent intent = new Intent(Signup.this, Login.class);
                                            Toast.makeText(Signup.this, msgObj.getString("replyMessage"), Toast.LENGTH_LONG).show();
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                            finish();
                                        } catch (Exception e) {
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {

                                try {
                                    JSONObject responseJ = new JSONObject(response.errorBody().string());
                                    System.out.println("error response " + responseJ.toString());
                                    try {
                                        JSONObject errorObje = responseJ.getJSONObject("error");
                                        String status = errorObje.getString("statusCode");
                                        String message = errorObje.getString("message");
                                        Log.e("Error Status", status);
                                        Log.e("Error Message", message);
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();

                                } catch (Exception e) {

                                }

                            }
                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                        dismissProgressBar();
                            //  TestFragment.imageStringpath.clear();
                            Toast.makeText(Signup.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                            System.out.println("fail response.." + t.toString());
//                        dismissProgressBar();
                        }
                    });
//            } catch (Exception e) {
//                // TestFragment.imageStringpath.clear();
//                System.out.println("Exception..." + e.toString());
//                dismissProgressBar();
//            }
                }else {
                    Toast.makeText(Signup.this,"please select document",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(Signup.this,"Address or location may be Null please select address",Toast.LENGTH_LONG).show();
            }
        }

    }
    private void uploadBitmap(final Bitmap bitmap) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //getting the tag from the edittext
//        final String tags = editTextTags.getText().toString().trim();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrls.BASE_URL+WebUrls.signup_api,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.d("signup_response",response.toString());
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(Signup.this, R.string.suucess_signup, Toast.LENGTH_SHORT).show();
//                            Glide.with(MyProfile.this)
//                                    .load(obj.optString("data"))
//                                    .into(my_profile_user_image);
                            progressDialog.dismiss();
                            finish();
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";
                            } else if (error.getClass().equals(NoConnectionError.class)) {
                                errorMessage = "Failed to connect server";
                            }
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                JSONObject response = new JSONObject(result);
                                JSONObject errorObje = response.getJSONObject("error");
                                String status = errorObje.getString("statusCode");
                                String message = errorObje.getString("message");
                                Log.e("Error Status", status);
                                Log.e("Error Message", message);
                                errorMessage = message;
                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", data+"");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization",SharedPrefManager.getInstance(context).getRegPeopleId());
                header_param.put("ln",SharedPrefManager.getLangId(Signup.this,RequestCode.LangId));
                return header_param;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
