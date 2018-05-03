package com.evollu.react.fcm;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.Arguments;

/**
 * Created by xavimorenom on 25/04/2018.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {


    private CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(SendNotificationTask.REPLY_TEXT_LABEL);
        }
        return null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SendNotificationTask.REPLY_ACTION.equals(intent.getAction())) {
            String message = getReplyMessage(intent).toString();
            int notificationID = intent.getIntExtra("notificationID", 0);
            Bundle bundle = intent.getBundleExtra("extras");
            bundle.putString("_userText", message);
            // Redirect the message to the JS thread
            FIRMessagingModule.sendEvent("FCMNotificationReceived", Arguments.fromBundle(bundle));
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationID);
        }
    }

    // Utility just in case we need to debug the bundleID
    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }
}
