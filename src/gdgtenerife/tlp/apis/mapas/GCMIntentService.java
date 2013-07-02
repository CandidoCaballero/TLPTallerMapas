/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gdgtenerife.tlp.apis.mapas;

import org.json.JSONObject;

import gdgtenerife.tlp.apis.mapas.servidor.ServerUtilities;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * IntentService responsible for handling GCM messages.
 */

// TODO: Explicar Esto

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(ServerUtilities.idClient);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        ServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        generateNotification(context, intent.getExtras().getString("message"));
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        super.onDeletedMessages(context, total);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    public static void generateNotification(Context context, String message) {
    	try {
    		JSONObject data = new JSONObject(message);
			int icon = R.drawable.icon;
	        long when = System.currentTimeMillis();
	        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	        String title = context.getString(R.string.app_name);
	        Intent notificationIntent = new Intent(context, MainActivity.class);
	        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	        Notification notification = new Notification(icon, "Lugar: "+data.get("latitud")+"|"+data.get("longitud"), when);
	        notification.setLatestEventInfo(context, title, "Lugar: "+data.get("latitud")+"|"+data.get("longitud"), intent);
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	        notificationManager.notify(0, notification);
	        try {
	            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notificationUri);
	            r.play();
	        } catch (Exception e) {}

		} catch (Exception e) {
			return;
		}
    }
   

}
