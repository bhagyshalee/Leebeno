package leebeno.com.leebeno;

import android.Manifest;
import android.app.Dialog;
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
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.ChatBothSideAdapter;
import leebeno.com.leebeno.Adapters.ChatRoomAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.CustomerPart.UpdateProfileCustomer;
import leebeno.com.leebeno.Model.MsgGetterSetter;
import leebeno.com.leebeno.Services.DatabaseLocal;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Config.getLeeContactIdMsg;
import static leebeno.com.leebeno.Services.Config.getLeeProfileImg;
import static leebeno.com.leebeno.Services.Config.getMessageId;
import static leebeno.com.leebeno.Services.Config.getMessageTimeStamp;
import static leebeno.com.leebeno.Services.Config.getMessageType;
import static leebeno.com.leebeno.Services.Config.getReceiverMsg;
import static leebeno.com.leebeno.Services.Config.getSenderMessage;
import static leebeno.com.leebeno.Services.DatabaseLocal.LEE_CONTACTS;
import static leebeno.com.leebeno.Services.DatabaseLocal.MSG_CHAT;
import static leebeno.com.leebeno.Services.Utility.getCurrentTimeStamp;
import static leebeno.com.leebeno.Services.Utility.getMsgDetail;

public class ChatRoom extends AppCompatActivity implements RecyclerViewItemClickListener{

    private RecyclerView mMessageRecycler;
    private ChatBothSideAdapter mMessageAdapter;

    @Bind(R.id.btnAttachment)
    ImageView btnAttachment;
    @Bind(R.id.btnSend)
    ImageView btnSend;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.imgChatProfile)
    ImageView imgChatProfile;

    @Bind(R.id.contactName)
    TextView contactName;
    @Bind(R.id.onlineStatus)
    TextView onlineStatus;
    @Bind(R.id.deleteImage)
    ImageView deleteImage;

    @Bind(R.id.profileDetail)
    LinearLayout profileDetail;

    List<MsgGetterSetter> textMessageList;

    LinearLayoutManager mLayoutManager;
    EditText edittext_chatbox;
    Uri imageUri;
    private static final int PICK_Camera_IMAGE = 100;
    File compressedImage;
    String contact_id, contact_name;
    int setMsgStatus = 0;
    DatabaseLocal databaseLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        textMessageList = new ArrayList();
        databaseLocal = new DatabaseLocal(ChatRoom.this);

        contact_id = getIntent().getStringExtra("contact_id");
        contact_name = getIntent().getStringExtra("contact_name");

        Log.e("fnnjkdvjkvsv",contact_id+"");
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        /**
         *setStackFromEnd true will fill the content(list item) from the bottom of the view
         */

        if (databaseLocal.getLeeConnections(contact_id).getProfileUrl() != null) {
            Picasso.get()
                    .load(databaseLocal.getLeeConnections(contact_id).getProfileUrl())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(imgChatProfile);

        }
        if (databaseLocal.getLeeConnections(contact_id).getOnlineStatus() != null) {
            if (databaseLocal.getLeeConnections(contact_id).getOnlineStatus().compareTo("true") == 0) {
                onlineStatus.setText("Online");
            } else {
                onlineStatus.setText(databaseLocal.getLeeConnections(contact_id).getLastSeen());
            }

        }

        if (databaseLocal.getLeeConnections(contact_id).getNickname() != null) {
            contactName.setText(databaseLocal.getLeeConnections(contact_id).getNickname());
        }

            deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Log.e("nrgfnfrtngfr", "" + databaseLocal.getLeeConnections(contact_id));
        mLayoutManager.setStackFromEnd(true);
//        mLayoutManager.setReverseLayout(true);
        mMessageRecycler.setLayoutManager(mLayoutManager);
        mMessageRecycler.setItemAnimator(new DefaultItemAnimator());
//        databaseLocal.resetTable(MSG_CHAT);
//        mMessageRecycler.setHasFixedSize(true);
        getMsgDetail(ChatRoom.this);

        for (int i = 0; i < getLeeContactIdMsg.size(); i++) {
            Log.e("differththrth", getLeeContactIdMsg.get(i) + " " + contact_id);
            if (contact_id.compareTo(getLeeContactIdMsg.get(i))==0) {
                MsgGetterSetter userMessage = new MsgGetterSetter();
                userMessage.setMessage(getSenderMessage.get(i));
                userMessage.setMessageR(getReceiverMsg.get(i));
                userMessage.setMsgType(getMessageType.get(i));
                userMessage.setCreatedAt(getMessageTimeStamp.get(i));
                userMessage.setMessage_id(getMessageId.get(i));
                textMessageList.add(userMessage);
            }
        }

        mMessageAdapter = new ChatBothSideAdapter(ChatRoom.this, textMessageList,deleteImage,profileDetail,this);
        mMessageRecycler.setAdapter(mMessageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("setMsgStatus", "" + contact_id);
//                databaseLocal.resetTable(MSG_CHAT);
//                if (setMsgStatus % 2 == 0) {

                if (!edittext_chatbox.getText().toString().trim().isEmpty()) {
                    MsgGetterSetter userMessage = new MsgGetterSetter();
                    userMessage.setMessage(edittext_chatbox.getText().toString());

                    DateFormat df = new SimpleDateFormat("hh:mm a");
                    String getCurrentTime = df.format(Calendar.getInstance().getTime());
                    userMessage.setCreatedAt(String.valueOf(getCurrentTime));
                    userMessage.setMessage(edittext_chatbox.getText().toString());
//                        edittext_chatbox.setText("");

//                        setMsgStatus++;

                    textMessageList.add(userMessage);
                    Log.e("klvdjkld", "" + edittext_chatbox.getText().toString());
//                        databaseLocal.resetTable(MSG_CHAT);
                    mMessageAdapter.notifyDataSetChanged();
                    databaseLocal.addMsgChat(getCurrentTimeStamp(), "single", getCurrentTimeStamp(), edittext_chatbox.getText().toString()
                            , "", "text", "true", getCurrentTime, contact_id);


                    mMessageRecycler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Call smooth scroll
                            mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                        }
                    });
                    edittext_chatbox.setText("");
                }

               /* } else {
                    if (!edittext_chatbox.getText().toString().trim().isEmpty()) {

                        MsgGetterSetter userMessage = new MsgGetterSetter();
                        userMessage.setMessage(edittext_chatbox.getText().toString().trim());

                        DateFormat df = new SimpleDateFormat("hh:mm a");
                        String getCurrentTime = df.format(Calendar.getInstance().getTime());
                        userMessage.setCreatedAt(String.valueOf(getCurrentTime));
                        userMessage.setMessageR(edittext_chatbox.getText().toString().trim());
                        edittext_chatbox.setText("");

                        setMsgStatus++;
                        textMessageList.add(userMessage);
                        mMessageAdapter.notifyDataSetChanged();
                        Log.e("klvdjkld", contact_id);
                        databaseLocal.addMsgChat(getCurrentTimeStamp(), "single", getCurrentTimeStamp(), ""
                                , edittext_chatbox.getText().toString().trim(), "text", "true", getCurrentTime
                                , contact_id);

                        mMessageAdapter = new ChatBothSideAdapter(ChatRoom.this, textMessageList, getSentValue);
                        mMessageRecycler.setAdapter(mMessageAdapter);

                        mMessageRecycler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Call smooth scroll
                                mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                            }
                        });
                    }
                }*/
            }
        });


        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog mBottomSheetDialog = new Dialog(ChatRoom.this);
                mBottomSheetDialog.setContentView(R.layout.popuup_camragallary_design); // your custom view.
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
              /*
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//              */
                mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                mBottomSheetDialog.setCanceledOnTouchOutside(true);
                LinearLayout layoutGallery = mBottomSheetDialog.findViewById(R.id.layoutGallery);
                LinearLayout layoutCamera = mBottomSheetDialog.findViewById(R.id.layoutCamera);
                layoutCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(ChatRoom.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ChatRoom.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                            } else {

                                selectCameraImage();
                                mBottomSheetDialog.dismiss();
                            }
                        } else {
                            selectCameraImage();
                            mBottomSheetDialog.dismiss();
                        }
                    }
                });

                layoutGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(ChatRoom.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ChatRoom.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                            } else {
                                mBottomSheetDialog.dismiss();
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
                            mBottomSheetDialog.dismiss();
                        }
                    }
                });

                mBottomSheetDialog.show();
            }
        });
    }

    @Override
    public void recyclerViewClicked(View v, int position) {

//set up adapter and pass clicked listener this
//        mMessageAdapter.notifyDataSetChanged();
        mMessageAdapter.notifyDataSetChanged();

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
                    File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/leebeno/sent");

                    new ImageCompressionAsyncTask(ChatRoom.this).execute(data.getData().toString(),
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

            File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/leebeno/sent");
            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTask(ChatRoom.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
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


    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(ChatRoom.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(ChatRoom.this).compress(params[0], new File(params[1]));

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ChatRoom.this.getContentResolver(), compressUri);

                    MsgGetterSetter userMessage = new MsgGetterSetter();
//                    userMessage.setProfileUrl(bitmap);
                    DateFormat df = new SimpleDateFormat("hh:mm a");
                    String getCurrentTime = df.format(Calendar.getInstance().getTime());
                    userMessage.setNickname(String.valueOf(getCurrentTime));
                    edittext_chatbox.setText("");

                    textMessageList.add(userMessage);
                    mMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Call smooth scroll
                            mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }


}
