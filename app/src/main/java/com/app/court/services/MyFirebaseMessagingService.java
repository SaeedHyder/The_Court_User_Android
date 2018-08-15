package com.app.court.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.court.R;
import com.app.court.activities.MainActivity;
import com.app.court.global.AppConstants;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.NotificationHelper;
import com.app.court.retrofit.WebService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private WebService webservice;
    private BasePreferenceHelper preferenceHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferenceHelper = new BasePreferenceHelper(getApplicationContext());
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            buildNotification(remoteMessage);

        }else if(remoteMessage.getNotification()!=null){
            String title = getString(R.string.app_name);
            String message = remoteMessage.getNotification().getBody().toString();
            Log.e(TAG, "message: " + message);

            Intent resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("tapped", true);


            Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
            showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);

        }
    }

    private void buildNotification(RemoteMessage messageBody) {
        String title = getString(R.string.app_name);
        String message = messageBody.getData().get("message");
        String actionId = messageBody.getData().get("action_id");
        String actionType = messageBody.getData().get("action_type");
        Log.e(TAG, "message: " + message);
        Intent resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("message", message);
        resultIntent.putExtra("tapped", true);
        resultIntent.putExtra("action_id", actionId);
        resultIntent.putExtra("action_type", actionType);


        Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("action_id", actionId);
        pushNotification.putExtra("action_type", actionType);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
        showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);
    }

    /*private void getNotificaitonCount() {
        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(this, WebServiceConstants.Local_SERVICE_URL);
        preferenceHelper = new BasePreferenceHelper(this);
        Call<ResponseWrapper<countEnt>> call = webservice.getNotificationCount(preferenceHelper.getMerchantId());
        call.enqueue(new Callback<ResponseWrapper<countEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<countEnt>> call, Response<ResponseWrapper<countEnt>> response) {
                preferenceHelper.setNotificationCount(response.body().getResult().getCount());
            }

            @Override
            public void onFailure(Call<ResponseWrapper<countEnt>> call, Throwable t) {

            }
        });
    }*/

    private void SendNotification(int count, JSONObject json) {

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationHelper.getInstance().showNotification(context,
                R.drawable.logowithcircle,
                title,
                message,
                timeStamp,
                intent);
    }


}
