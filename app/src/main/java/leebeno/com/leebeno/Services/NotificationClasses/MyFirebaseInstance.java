package leebeno.com.leebeno.Services.NotificationClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Services.Config.GCM_TOKEN;

/**
 * Created by bhagyshalee on 4/28/2017.
 */

public class MyFirebaseInstance extends FirebaseInstanceIdService {

    Context context=MyFirebaseInstance.this;
        private static final String REG_TOKEN = "REG_TOKEN";
        @Override
        public void onTokenRefresh() {
            GCM_TOKEN= FirebaseInstanceId.getInstance().getToken();
            Log.d(REG_TOKEN,GCM_TOKEN);

            if(GCM_TOKEN.equals(""))
            {

            }
            else {
                SharedPreferences.Editor editor = getSharedPreferences(REG_TOKEN, MODE_PRIVATE).edit();
                editor.putString(getResources().getString(R.string.firebase_token), GCM_TOKEN);
                editor.apply();
                editor.commit();
            }
        }

}
