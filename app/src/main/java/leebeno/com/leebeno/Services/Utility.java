package leebeno.com.leebeno.Services;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Services.Config.getChatType;
import static leebeno.com.leebeno.Services.Config.getContactIdLi;
import static leebeno.com.leebeno.Services.Config.getContactNameLi;
import static leebeno.com.leebeno.Services.Config.getContactNumberLi;
import static leebeno.com.leebeno.Services.Config.getContactProfileLi;
import static leebeno.com.leebeno.Services.Config.getGroupId;
import static leebeno.com.leebeno.Services.Config.getLeeBlockStatus;
import static leebeno.com.leebeno.Services.Config.getLeeChatStatus;
import static leebeno.com.leebeno.Services.Config.getLeeContactId;
import static leebeno.com.leebeno.Services.Config.getLeeContactIdMsg;
import static leebeno.com.leebeno.Services.Config.getLeeLastSeen;
import static leebeno.com.leebeno.Services.Config.getLeeOnlineStatus;
import static leebeno.com.leebeno.Services.Config.getLeeProfileImg;
import static leebeno.com.leebeno.Services.Config.getMessageId;
import static leebeno.com.leebeno.Services.Config.getMessageReadStatus;
import static leebeno.com.leebeno.Services.Config.getMessageTimeStamp;
import static leebeno.com.leebeno.Services.Config.getMessageType;
import static leebeno.com.leebeno.Services.Config.getReceiverMsg;
import static leebeno.com.leebeno.Services.Config.getSenderMessage;


public class Utility {

    public static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);


    public static Dialog progressDialog;


    public static void showProgress(Context context) {
        try {

            progressDialog = new Dialog(context);
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().getDecorView().setBackgroundResource(R.color.transparentW);
            progressDialog.setContentView(R.layout.progress_design);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //ImageView img_gallery = (ImageView) dialog.findViewById(R.id.img_logo);
            progressDialog.show();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


    }

    public static void close() {
        progressDialog.dismiss();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
    }

  /*  public static void getContactDetail(Context context) {
        if (context != null) {
            DatabaseLocal databaseLocal = new DatabaseLocal(context);
            Cursor cursor = databaseLocal.getContects();
            getContactIdLi = new ArrayList<>();
            getContactNumberLi = new ArrayList<>();
            getContactNameLi = new ArrayList<>();
            getContactProfileLi = new ArrayList<>();


            if (cursor != null) {
                cursor.moveToFirst();
                for (int j = 0; j < cursor.getCount(); j++) {
                    getContactIdLi.add(cursor.getString(1));
                    getContactNumberLi.add(cursor.getString(2));
                    getContactNameLi.add(cursor.getString(3));
                    getContactProfileLi.add(cursor.getString(4));
                    Log.e("getAddedContacts", "" + cursor.getString(2));
                    cursor.moveToNext();

                }
            }

            cursor.close();
        }

    }

    public static void getLeeContactDetail(Context context) {
        if (context != null) {
            try{
                DatabaseLocal databaseLocal = new DatabaseLocal(context);
                Cursor cursor = databaseLocal.getLeeContects();

                getLeeProfileImg = new ArrayList<>();
                getLeeContactId = new ArrayList<>();
                getLeeChatStatus = new ArrayList<>();
                getLeeBlockStatus = new ArrayList<>();
                getLeeLastSeen = new ArrayList<>();
                getLeeOnlineStatus = new ArrayList<>();

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int j = 0; j < cursor.getCount(); j++) {
//                while (!cursor.isAfterLast()) {
                        getLeeContactId.add(cursor.getString(1));
                        getLeeProfileImg.add(cursor.getString(2));
                        getLeeChatStatus.add(cursor.getString(3));
                        getLeeBlockStatus.add(cursor.getString(4));
                        getLeeLastSeen.add(cursor.getString(5));
                        getLeeOnlineStatus.add(cursor.getString(6));
                        Log.e("getAddedContactsLeee", "" + cursor.getString(2));
                        cursor.moveToNext();
                    }


//                }
                }

                cursor.close();
            }catch (Exception e)
            {
                Log.e("getChatMsgException",""+e);
            }

        }

    }
*/
    public static void getContactDetailTotal(Context context) {
        if (context != null) {
            try{
                DatabaseLocal databaseLocal = new DatabaseLocal(context);
                Cursor cursor = databaseLocal.getContactDetail();

                getLeeProfileImg = new ArrayList<>();
                getLeeContactId = new ArrayList<>();
                getLeeChatStatus = new ArrayList<>();
                getLeeBlockStatus = new ArrayList<>();
                getLeeLastSeen = new ArrayList<>();
                getLeeOnlineStatus = new ArrayList<>();
                getContactNumberLi = new ArrayList<>();
                getContactNameLi = new ArrayList<>();

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int j = 0; j < cursor.getCount(); j++) {
//                while (!cursor.isAfterLast()) {
                        getLeeContactId.add(cursor.getString(1));
                        getLeeProfileImg.add(cursor.getString(2));
                        getLeeChatStatus.add(cursor.getString(3));
                        getLeeBlockStatus.add(cursor.getString(4));
                        getLeeLastSeen.add(cursor.getString(5));
                        getLeeOnlineStatus.add(cursor.getString(6));
                        getContactNumberLi.add(cursor.getString(7));
                        getContactNameLi.add(cursor.getString(8));
                        Log.e("getAddedContactsLeee", "" + cursor.getString(2));
                        cursor.moveToNext();
                    }


//                }
                }

                cursor.close();
            }catch (Exception e)
            {
                Log.e("getChatMsgException",""+e);
            }

        }

    }


    public static String getCurrentTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void getMsgDetail(Context context) {
        if (context != null) {
            DatabaseLocal databaseLocal = new DatabaseLocal(context);
            Cursor cursor = databaseLocal.getMsgChat();
            getMessageId = new ArrayList<>();
            getChatType = new ArrayList<>();
            getGroupId = new ArrayList<>();
            getSenderMessage = new ArrayList<>();
            getReceiverMsg = new ArrayList<>();
            getMessageType = new ArrayList<>();
            getMessageReadStatus = new ArrayList<>();
            getMessageTimeStamp = new ArrayList<>();
            getLeeContactIdMsg = new ArrayList<>();

            if (cursor != null) {
                cursor.moveToFirst();
                for (int j = 0; j < cursor.getCount(); j++) {
                    getMessageId.add(cursor.getString(1));
                    getChatType.add(cursor.getString(2));
                    getGroupId.add(cursor.getString(3));
                    getSenderMessage.add(cursor.getString(4));
                    getReceiverMsg.add(cursor.getString(5));
                    getMessageType.add(cursor.getString(6));
                    getMessageReadStatus.add(cursor.getString(7));
                    getMessageTimeStamp.add(cursor.getString(8));
                    getLeeContactIdMsg.add(cursor.getString(9));
                    Log.e("getChatDetail", "" + cursor.getString(9)+" " + cursor.getString(1));
                    Log.e("cursorsdsvd ", "" + cursor.getCount());
                    cursor.moveToNext();
                }
            }

        }
    }

    public static final void setDatePickerIn(Context context, final TextView et) {
        int mYear;
        int mMonth;
        int mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }

                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
        // dialog.DatePicker.MinDate = Java.Lang.JavaSystem.CurrentTimeMillis();
    }

    public static String getDateFromUtc(String utcDate) {

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        String formattedDatee = "";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = inputFormat.parse(utcDate);
            inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            String srDate = "";
            srDate = inputFormat.format(myDate);
            Date datee = null;
            try {
                datee = inputFormat.parse(srDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = outputFormat.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDatee;
    }


    public static String printDifference(String startDateStr) {
        //milliseconds


        TimeZone utcZone = TimeZone.getTimeZone("IST");
        inputFormat.setTimeZone(utcZone);// Set UTC time zone
        Date startDate = null;
        Date endDate = new Date();
        try {
            startDate = inputFormat.parse(startDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone

//        Utility.printDifference(myDate, new Date());
        Log.e("both date", "" + new Date() + " " + startDate);


        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        if (elapsedDays != 0) {
            if (elapsedDays == 1)
                return elapsedDays + " days ago";
            else
                return elapsedDays + " days ago";
        } else if (elapsedHours != 0 && elapsedDays == 0) {
            if (elapsedHours == 1)
                return elapsedHours + " Hour ago";
            else
                return elapsedHours + " Hours ago";

        } else if (elapsedMinutes != 0 && elapsedHours == 0 && elapsedDays == 0) {
            if (elapsedMinutes == 1)
                return elapsedMinutes + " Minute ago";
            else
                return elapsedMinutes + " Minutes ago";
        } else if (elapsedSeconds != 0 && elapsedMinutes == 0 && elapsedHours == 0 && elapsedDays == 0) {
            return "Just Now";
        }
        return "Just Now";
    }


    public static String getDateDifference(Date thenDate) {

        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(thenDate);


        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        Log.i("DateTIME :", diffDays + ":" + diffHours + ":" + diffMinutes);

        if (diffMinutes < 60) {
            if (diffMinutes == 1)
                return diffMinutes + " minute ago";
            else if (diffMinutes == 0) {
                return "just Now";
            } else {
                return diffMinutes + " minutes ago";
            }
        } else if (diffHours < 24) {
            if (diffHours == 1)
                return diffHours + " hour ago";
            else
                return diffHours + " hours ago";
        } else if (diffDays < 30) {
            if (diffDays == 1)
                return diffDays + " day ago";
            else
                return diffDays + " days ago";
        } else {
            return "a long time ago..";
        }
    }

    public static void setDatePickerInEditView(Context context, final EditText et) {
        int mYear;
        int mMonth;
        int mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }

                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
        // dialog.DatePicker.MinDate = Java.Lang.JavaSystem.CurrentTimeMillis();
    }

    public static final void setDatePickerHidePreviousET(Context context, final TextView et) {
        int mYear;
        int mMonth;
        int mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        // dialog.DatePicker.MinDate = Java.Lang.JavaSystem.CurrentTimeMillis();
    }

    public static String getTimeFromUtc(String utcDate) {

        SimpleDateFormat outputFormatt = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String formattedDatee = "";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = inputFormat.parse(utcDate);
            // inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            String strDate = "";
            strDate = inputFormat.format(myDate);
            Log.e("fdvdvdffdfgreg", "" + strDate);
            Date datee = null;
            try {
                datee = inputFormat.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = outputFormatt.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDatee;
    }

    public static String getDateToUtc(String utcDate) {
        SimpleDateFormat outputFormatt = new SimpleDateFormat("d/M/yyyy", Locale.ENGLISH);
        String formattedDatee = "";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            //outputFormatt.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = outputFormatt.parse(utcDate);
            outputFormatt.setTimeZone(TimeZone.getDefault());// Set device time zone
            String strDate = "";
            strDate = outputFormatt.format(myDate);
            Log.e("fdvdvdffdfgreg", "" + strDate);
            Date datee = null;
            try {
                datee = outputFormatt.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = inputFormat.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sgerthrthg", "" + e);
        }
        return formattedDatee;
    }
}
