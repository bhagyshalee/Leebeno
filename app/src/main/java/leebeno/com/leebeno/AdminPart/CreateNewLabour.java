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
import android.graphics.BitmapFactory;
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
import android.text.TextUtils;
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
import com.iceteck.silicompressorr.SiliCompressor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.AddSkillAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.DataPart;
import leebeno.com.leebeno.Common.MySpinner;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Common.VolleyMultipartRequest;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;
import leebeno.com.leebeno.Signup;

import static leebeno.com.leebeno.Services.Utility.showToast;

public class CreateNewLabour extends AppCompatActivity implements WebCompleteTask {

    private static final int PICK_Camera_IMAGE = 100;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.profilePicLabour)
    ImageView profilePicLabour;
    @Bind(R.id.etUserName)
    EditText etUserName;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.etSkills)
    MySpinner etSkills;
    @Bind(R.id.recyclerSkill)
    RecyclerView recyclerSkill;
    @Bind(R.id.etUserEmail)
    EditText etUserEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @Bind(R.id.btnCreateLabour)
    Button btnCreateLabour;
    List<String> arrayListSkill;
    List<String> listSelectSkill;
    AddSkillAdapter addSkillAdapter;
    Uri imageUri, imageUriIDProof;
    int REQUEST_CAMERA_PERMISSION = 1;
    String labourMode;
    @Bind(R.id.titleCreateLabour)
    TextView titleCreateLabour;
    JSONObject data;
    List<String> skillsaArray;
    List<String> skillsaArrayID ;
    ArrayList<String> listSelectedSkillsid;
    List<String> listSkillsId = new ArrayList<>();
    public static List<String> listSkillsName;
    Bitmap bitmap;

    public static String createlabour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_create_new_labour);
        ButterKnife.bind(this);

        createlabour = "createLab";
        labourMode = getIntent().getStringExtra("labourMode");
        skillsaArray = new ArrayList<>();
        skillsaArrayID = new ArrayList<>();
        listSelectedSkillsid = new ArrayList<>();
        btnCreateLabour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LabourCreateMethod();
            }
        });
        if (labourMode.equals("edit"))
        {
            titleCreateLabour.setText(getResources().getString(R.string.editLabour));
            btnCreateLabour.setText(getResources().getString(R.string.editLabour));
            etUserName.setFocusable(false);
            etUserEmail.setFocusable(false);
            etPassword.setVisibility(View.GONE);
            etConfirmPassword.setVisibility(View.GONE);
        }
        arrayListSkill = new ArrayList<>();
        listSelectSkill = new ArrayList<>();
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profilePicLabour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Dialog dialog = new Dialog(CreateNewLabour.this);
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
                                if (ActivityCompat.checkSelfPermission(CreateNewLabour.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(CreateNewLabour.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                                if (ActivityCompat.checkSelfPermission(CreateNewLabour.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(CreateNewLabour.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
        });


        recyclerSkill.setHasFixedSize(true);
        recyclerSkill.setNestedScrollingEnabled(true);
        recyclerSkill.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        GetSkillsMethod();
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

                    new ImageCompressionAsyncTask(CreateNewLabour.this).execute(data.getData().toString(),
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

                    new ImageCompressionAsyncTask(CreateNewLabour.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }
        }
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
    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(CreateNewLabour.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(CreateNewLabour.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                String strCompressedFileImage = s;
                File compressedImage = new File(s);
                Uri compressUri = Uri.fromFile(compressedImage);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(CreateNewLabour.this.getContentResolver(), compressUri);

                    profilePicLabour.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }
    private void GetSkillsMethod(){
        HashMap objectNew = new HashMap();
        new WebTask(CreateNewLabour.this, WebUrls.BASE_URL+WebUrls.getAllSkills_api,objectNew,CreateNewLabour.this, RequestCode.CODE_getskills,0);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode==RequestCode.CODE_getskills){
            Log.d("getskills_response",response);

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
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateNewLabour.this, R.layout.design_spinner, R.id.textSpinner, listSkillsName) {
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
                    etSkills.setAdapter(spinnerArrayAdapter);
                    spinnerclick();

                }
            } catch (Exception e) {

            }
        }
    }
    private void spinnerclick(){
        etSkills.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //  String selectedItem = adapterView.getItemAtPosition(position).toString();
                String selectedItemS = etSkills.getSelectedItem().toString();

                if (selectedItemS.equals(getResources().getString(R.string.select_skills))) {
                    // do your stuff
                } else {
                    if (listSelectSkill.size() == 0) {
                        listSelectSkill.add(selectedItemS);
                        listSelectedSkillsid.add(listSkillsId.get(position));
                        addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid,listSelectSkill,  CreateNewLabour.this, recyclerSkill,etSkills);
                        recyclerSkill.setAdapter(addSkillAdapter);
                        recyclerSkill.setVisibility(View.VISIBLE);
                    } else {
                        if (listSelectSkill.contains(selectedItemS)) {
                            if (Signup.cl==false){
                                showToast(CreateNewLabour.this, getResources().getString(R.string.select_already_select_skills));

                            }

                        } else{
                            listSelectSkill.add(selectedItemS);
                            listSelectedSkillsid.add(listSkillsId.get(position));
                            addSkillAdapter = new AddSkillAdapter(listSelectedSkillsid,listSelectSkill,  CreateNewLabour.this, recyclerSkill,etSkills);
                            recyclerSkill.setAdapter(addSkillAdapter);
                            recyclerSkill.setVisibility(View.VISIBLE);
                        }
                        Signup.cl=false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void LabourCreateMethod(){
        if (TextUtils.isEmpty(etUserName.getText().toString())){
            etUserName.setError(getString(R.string.plz_ent_ur_name));
            etUserName.requestFocus();
        }else if (TextUtils.isEmpty(etUserEmail.getText().toString())){
            etUserEmail.setError(getString(R.string.plz_ent_email));
            etUserEmail.requestFocus();
        } else if (!SharedPrefManager.getInstance(CreateNewLabour.this).isValidEmail(etUserEmail.getText().toString())){
            etUserEmail.setError(getString(R.string.email_not_valid));
            etUserEmail.requestFocus();
        } else if (etPassword.getText().length()<6){
            etPassword.setError(getString(R.string.pass_must_6));
            etPassword.requestFocus();
        } else if (!SharedPrefManager.getInstance(CreateNewLabour.this).CheckPassword(etPassword.getText().toString())){
            etPassword.setError(getString(R.string.pass_must_6));
            etPassword.requestFocus();
        }  else if (TextUtils.isEmpty(etConfirmPassword.getText().toString())){
            etConfirmPassword.setError(getString(R.string.plz_ent_cnf_pass));
            etConfirmPassword.requestFocus();
        }else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            etConfirmPassword.setError(getString(R.string.pass_nd_cnf_pass_not));
            etConfirmPassword.requestFocus();
        }else if (TextUtils.isEmpty(etDescription.getText().toString().trim())){
            etDescription.setError(getString(R.string.plz_ent_des));
            etDescription.requestFocus();
        } else if (listSelectSkill!=null && listSelectSkill.size()>0){
            data= new JSONObject();
            JSONObject locationobj=null;
            try {
                JSONArray ssk= new JSONArray();
                for (int i=0;i<listSelectedSkillsid.size();i++){
                    ssk.put(listSelectedSkillsid.get(i));
                }

                data.put("realm","labour");
                data.put("fullName",etUserName.getText().toString().trim());
                data.put("email",etUserEmail.getText().toString().trim());
                data.put("password",etPassword.getText().toString().trim());
                data.put("description",etDescription.getText().toString().trim());
                data.put("groupAdminId",SharedPrefManager.getID(CreateNewLabour.this, Constrants.UserID));
//                data.put("pph",etMinimumPayPerHour.getText().toString().trim());
                data.put("skillIds",ssk);
//                data.put("address",addressObj);
//                data.put("ln",SharedPrefManager.getLangId(Signup.this,Constrants.UserLang));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (bitmap != null){
                uploadBitmap(bitmap);
            }else {
                bitmap= BitmapFactory.decodeResource(CreateNewLabour.this.getResources(), R.drawable.user);
                uploadBitmap(bitmap);
            }

        } else {
            Toast.makeText(CreateNewLabour.this,R.string.atleastone,Toast.LENGTH_SHORT).show();
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
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrls.BASE_URL+WebUrls.createNewLabour,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.d("signup_response",response.toString());
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(CreateNewLabour.this, "Successfully Create", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
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
                header_param.put("Authorization",SharedPrefManager.getInstance(CreateNewLabour.this).getAccessToken());
                header_param.put("ln",SharedPrefManager.getLangId(CreateNewLabour.this,RequestCode.LangId));
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
