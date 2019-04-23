package leebeno.com.leebeno.CustomerPart.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.CustomerPart.Adapter.ChatListAdapter;
import leebeno.com.leebeno.CustomerPart.Adapter.CreateNewJobAdapter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.DatabaseLocal;
import static leebeno.com.leebeno.Services.Utility.getContactDetailTotal;

public class MessageCustomerFragment extends Fragment {

    @Bind(R.id.chatRecycler)
    RecyclerView chatRecycler;
    List<String> listJobItem;
    List<String> listJobItemD;
    List<String> listJobItemT;
    ChatListAdapter chatListAdapter;
    static int valueGett = 0;
    @Bind(R.id.serviceNotMsg)
    TextView serviceNotMsg;
    @Bind(R.id.msgCard)
    CardView msgCard;
    DatabaseLocal databaseLocal;

    List<String> contactId, contactNumber, contactProfile, contactName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_message_customer, container, false);
        ButterKnife.bind(this, v);

        databaseLocal = new DatabaseLocal(getActivity());


        listJobItem = new ArrayList<>();
        listJobItemD = new ArrayList<>();
        listJobItemT = new ArrayList<>();

        contactId = new ArrayList<>();
        contactNumber = new ArrayList<>();
        contactProfile = new ArrayList<>();
        contactName = new ArrayList<>();

        chatRecycler.setHasFixedSize(true);
        chatRecycler.setNestedScrollingEnabled(true);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");


        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 1);
        } else {

/*
            new AsyncTask<Void, Void, Boolean>() {
                protected Boolean doInBackground(Void... params) {
                    getContactList();
                    return null;
                }
                protected void onPostExecute(Boolean result) {


                }
            }.execute();
*/

            Log.e("DB", "PERMISSION GRANTED");
        }
//                    Log.e("getListVlayye", ""+getLeeProfileImg.get(0));


       /* databaseLocal.resetTable(LEE_CONTACTS);
        databaseLocal.resetTable(TABLE_CONTACTS);
        databaseLocal.resetTable(MSG_CHAT);
        databaseLocal.resetTable(GROUP_CHAT);*/

      /*  databaseLocal.addContacts("78343", "784783743745", "Nandu", "are ");
        databaseLocal.addContacts("78344", "784783743746", "Bhagya", "are ");
        databaseLocal.addContacts("78345", "784783743747", "andu", "are ");

        databaseLocal.addLeeContacts("78351", "http://139.59.71.150:3013/api/Containers/labourWorkImg/download/Image10.png",
                "Hey babe", "false", "3 min ago", "false");
        databaseLocal.addLeeContacts("78352", "http://139.59.71.150:3013/api/Containers/labourWorkImg/download/Image20251314260.png",
                "bkvas nhi", "true", "5 min ago", "true");
        databaseLocal.addLeeContacts("78353", "http://139.59.71.150:3013/api/Containers/labourWorkImg/download/IMG_20190211_223041.jpg",
                "Chup re", "false", "6 min ago", "false");*/

        databaseLocal.addContactDetail("78351", "http://139.59.71.150:3013/api/Containers/labourWorkImg/download/Image10.png",
                "Hey babe", "false", "3 min ago", "false","784783743745", "Nandu");

        databaseLocal.addContactDetail("78352", "http://139.59.71.150:3013/api/Containers/labourWorkImg/download/Image20251314260.png",
                "Hey babe", "false", "5 min ago", "true","784783743745", "bhagya");

        databaseLocal.addContactDetail("78353", "http://139.59.71.150:3013/api/Containers/labourWorkImg/download/IMG_20190211_223041.jpg",
                "Hey babe", "false", "6 min ago", "false","784783743745", "Bhuvi");


        getContactDetailTotal(getActivity());
//        getLeeContactDetail(getActivity());

/*
        if (databaseLocal.getlastMsgTime("78352")!=null){
            Log.e("getLastMSSG",""+databaseLocal.getlastMsgTime("78352").getSender_msg()+" "+databaseLocal.getlastMsgTime("78352").getReceiver_msg());
        }
*/


        chatListAdapter = new ChatListAdapter(listJobItem, getActivity(), listJobItemD, listJobItemT);
        chatRecycler.setAdapter(chatListAdapter);
        return v;
    }


    private void getContactList() {
//        showProgress(getActivity());
        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String PHOTOGET = ContactsContract.CommonDataKinds.Photo.PHOTO;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);


        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String image_uri = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

/*


 if (image_uri != null) {
                    image.setImageURI(Uri.parse(image_uri));
                }*/


                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    output.append("\n FirstName:" + name);
                    output.append("\n contactIds :" + contact_id);
                    System.out.println("image_uri " + image_uri);
                    // Query and loop for every phone number of the contact
                    contactId.add(contact_id);
                    contactName.add(name);
                    contactProfile.add(image_uri);
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phonenumber:" + phoneNumber);
                        contactNumber.add(phoneNumber);
                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        output.append("\nEmail:" + email);

                    }

                    emailCursor.close();
                }

                output.append("\n");
            }
//            close();
            Log.e("getOutputStr", "" + output.toString());
//            contactTextView.setText(output.toString());
        }

/* for (int i=0;i<contactNumber.size();i++)
        {
            databaseLocal.addContacts(contactId.get(i),contactNumber.get(i),contactName.get(i),contactProfile.get(i),contactProfile.get(i));
        }*/


    }

}
