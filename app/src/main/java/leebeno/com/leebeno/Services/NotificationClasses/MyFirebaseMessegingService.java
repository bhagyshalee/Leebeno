package leebeno.com.leebeno.Services.NotificationClasses;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Services.Utility.showToast;

public class MyFirebaseMessegingService extends FirebaseMessagingService implements WebCompleteTask {
    private static final String TAG = "MyFirebaseMsgService";
    String Id;
    String setSatus = "";
    Intent gotoIntent;
    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData()!=null && remoteMessage.getNotification()!=null) {
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "mghxdxfgbhdhfgh" + remoteMessage.getData().toString());
                Log.d(TAG, "background" + remoteMessage.getNotification().getBody().toString() + " " + remoteMessage.getNotification().getTitle().toString());

                getJobDetail(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), remoteMessage.getData().get("jobId"),remoteMessage.getData().get("requestId"), remoteMessage.getData().get("groupId"), remoteMessage.getData().get("walletId"));
            }
            int badge_count = 0;
        }

    }

    private void getJobDetail(final String body, final String title, final String jobId, final String requestId, final String groupId, final String walletId) {
        gotoIntent = new Intent();
//        Log.e("MyFirebaseService ", "" + body + " " + jobId + " " + title);

        if (jobId!=null){
            ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<JsonObject> call = apiInterface.getJobDetail(SharedPrefManager.getAccessToken(MyFirebaseMessegingService.this, Constrants.AccessToken), SharedPrefManager.getLangId(MyFirebaseMessegingService.this
                    , Constrants.UserLang), jobId, SharedPrefManager.getUserID(MyFirebaseMessegingService.this, Constrants.UserID));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    int addtionalChar = 0;
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                            JSONObject jsonObjData = jsonObjSuccess.getJSONObject("data");
                            Log.e("MyFirebaseService ", "" + response.body().toString());
                            if (SharedPrefManager.getUserStatus(MyFirebaseMessegingService.this, Constrants.UserStatus).compareTo("customer") == 0) {
                          /*  if (jsonObjData.getString("status").compareTo(getResources().getString(R.string.amountpendingStatus)) == 0) {
                                gotoIntent.putExtra("jobId", jobId);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.NotificationActivity");

                            }else
                            {*/
                                gotoIntent.putExtra("jobId", jobId);
                                gotoIntent.putExtra("noti", true);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.CustomerPart.CustomerJobDetail");
//                            }
                            }
                            if (SharedPrefManager.getUserStatus(MyFirebaseMessegingService.this, Constrants.UserStatus).compareTo("labour") == 0) {

                                gotoIntent.putExtra("jobId", jobId);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.BidPlace");

                            }
                            if (SharedPrefManager.getUserStatus(MyFirebaseMessegingService.this, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                                gotoIntent.putExtra("jobId", jobId);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.BidPlace");
                            }

                            sendNotification(body, title, jobId, gotoIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        sendNotification(body, title, jobId, gotoIntent);
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            JSONObject jsonObjError = jsonObject.getJSONObject("error");
                            // showToast(MyFirebaseMessegingService.this, "" + jsonObjError.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    // close();
                }
            });


        }if (requestId!=null || groupId!=null)
        {
            if (SharedPrefManager.getUserStatus(MyFirebaseMessegingService.this, Constrants.UserStatus).compareTo("labour") == 0) {

//                gotoIntent.putExtra("jobId", requestId);
                gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.LabourPart.ConnectionsLabour");

            }
            if (SharedPrefManager.getUserStatus(MyFirebaseMessegingService.this, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
//                gotoIntent.putExtra("jobId", jobId);
                gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.AdminPart.ConnectionsAdmin");
            }

            sendNotification(body, title, jobId, gotoIntent);
        }else if (walletId!=null)
        {
            gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.Wallet");
            sendNotification(body, title, jobId, gotoIntent);
        }
    }


    private void sendNotification(String message, String title, String jobId, Intent intent) {

        Intent intentNew = null;
        NotificationManager notificationManager = null;
        Notification notification = null;
        try {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = null;
            contentIntent = PendingIntent.getActivity(this,
                    (int) (Math.random() * 100), gotoIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);
            notification = mBuilder.setSmallIcon(R.drawable.logo).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(contentIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(message)
                    .build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            count++;
            if (notificationManager != null) {
                notificationManager.notify(count, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_getDetailNotification) {
//                getPostedJobs.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonArrayData = jsonObjSuccess.getJSONObject("data");


                Log.e("NotiDetail", "" + jsonObject);
                if (jsonArrayData.optString("realm").compareTo("customer") == 0) {
                    if (jsonArrayData.getString("status").compareTo(getResources().getString(R.string.postedStatus)) == 0) {
                        gotoIntent.putExtra("jobStatus", "pending");
                        gotoIntent.putExtra("jobId", jsonArrayData.optString("id"));

                    }
                    if (jsonArrayData.getString("status").compareTo(getResources().getString(R.string.ongoing)) == 0) {
                        gotoIntent.putExtra("jobStatus", "pending");
                        gotoIntent.putExtra("jobId", jsonArrayData.optString("id"));
                        gotoIntent.setClassName(MyFirebaseMessegingService.this, "leebeno.com.leebeno.BidPlace");


                    }
                    if (jsonArrayData.getString("status").compareTo(getResources().getString(R.string.amountpendingStatus)) == 0) {

                    }

                }
                if (jsonArrayData.optString("realm").compareTo("labour") == 0) {
                }
                if (jsonArrayData.optString("realm").compareTo("groupAdmin") == 0) {

                }
            }

        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }

  /*  @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }*/


}
