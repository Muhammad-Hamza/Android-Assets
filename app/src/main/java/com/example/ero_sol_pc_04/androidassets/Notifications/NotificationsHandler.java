package com.erosol.voip.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.erosol.voip.R;
import com.erosol.voip.activities.IncomingCallScreenActivity;
import com.erosol.voip.activities.MessagingActivity;
import com.erosol.voip.activities.TabActivity;
import com.erosol.voip.activities.VoipApplication;
import com.erosol.voip.custom.CircleTransform;
import com.erosol.voip.http.URLIdentifiers;
import com.erosol.voip.model.DeviceSpec;
import com.erosol.voip.model.VoipChat;
import com.erosol.voip.model.VoipMessage;
import com.erosol.voip.model.VoipContact;
import com.erosol.voip.util.Utils;
import com.erosol.voip.util.VoipConstants;
import com.sinch.android.rtc.calling.Call;

import java.text.SimpleDateFormat;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;

public class NotificationHandler {

    public static final int NOTIFICATION_ID = 101;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    public static NotificationHandler singleTon = null;
    private NotificationTarget notificationTarget;

    /**
     * Creates an instance of Notification handler
     * @return
     */
    public static NotificationHandler getInstance() {
        if (singleTon == null) {
            return new NotificationHandler();
        }
        return singleTon;
    }

    /**
     * Creates a notification on incomming message
     * @param context
     * @param voipContact takes a voip contact from which the message is comming
     * @param message incomming message
     */

    public void createIncomingMessageNotification(Context context, VoipContact voipContact, VoipMessage message) {
        String replyLabel = context.getString(R.string.txt_reply);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();
        Intent resultIntent = new Intent(context, MessagingActivity.class);
        resultIntent.putExtra(VoipConstants.TYPE, VoipChat.ONE_TO_ONE);
        resultIntent.putExtra(VoipConstants.VOIP_CONTACT, voipContact);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MessagingActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(NOTIFICATION_ID,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_send, replyLabel, pendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();
        final  NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .addAction(action)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_message)
                //0.setContentTitle(voipContact.getName())  FIXME Causing null pointer exception check voip contact login
                .setContentText(message.getVoipMessageBody().getTextBody())
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH);

        Glide.with(context)
                .load(getUrl(context, voipContact))
                .asBitmap()
                .transform(new CircleTransform(context))
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(new SimpleTarget<Bitmap>(Utils.PROFILE_IMAGE_SIZE_WIDTH,Utils.PROFILE_IMAGE_SIZE_HEIGHT) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        builder.setLargeIcon(resource);
                    }
                });

        builder.setContentIntent(pendingIntent);
        builder.setTicker(context.getString(R.string.app_name));
        builder.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Creates a notification on incomming call
     * @param context
     * @param string
     */

    public void createIncomingCallNotification(Context context, String string) {
        Intent i = new Intent(context, IncomingCallScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_send)
                .setContentTitle(context.getResources().getString(R.string.txt_incomming_call))
                .setContentText(string)
                .addAction(R.drawable.ic_app_logo, context.getResources().getString(R.string.txt_dismiss), intent)
                .addAction(R.drawable.ic_block, context.getString(R.string.txt_action), intent);

        builder.setContentIntent(intent);
        builder.setTicker(context.getString(R.string.app_name));
        builder.setSmallIcon(R.drawable.ic_app_logo);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setPriority(PRIORITY_HIGH);
        builder.setOngoing(true);
        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    /**
     * create a notification if the call is in progress
     * @param context
     * @param call
     */
    public void createCallInProgressNotification(Context context, Call call) {
        Intent i = new Intent(context, MessagingActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_send)
                .setContentTitle(context.getResources().getString(R.string.txt_call_in_progress))
                .setContentText(call.getRemoteUserId())
                .addAction(R.drawable.ic_app_logo,context.getResources().getString(R.string.txt_dismiss), intent)
                .addAction(R.drawable.ic_block,context.getResources().getString(R.string.txt_action), intent);
        ;
        builder.setContentIntent(intent);
        builder.setTicker(context.getResources().getString(R.string.txt_established_call));
        builder.setSmallIcon(R.drawable.ic_app_logo);
        builder.setAutoCancel(false);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        android.app.NotificationManager nm = (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, notification);
    }

    /**
     * creates a notification if a call is missed
     * @param context
     * @param callerName name of the caller
     */
    public void createMissedCallNotification(Context context,String callerName) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        Intent i = new Intent(context, TabActivity.class);
        // i.setData(voipContact.getContactUri());
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_send)
                .setContentTitle(context.getResources().getString(R.string.txt_missed_call))
                .setContentText(callerName)
                // .addAction(R.drawable.ic_app_logo, "Dismiss", NotificationActivity.getDismissIntent())
                .addAction(R.drawable.ic_call_out_red, context.getResources().getString(R.string.txt_action), intent);
        ;
        builder.setContentIntent(intent);
        builder.setTicker(context.getString(R.string.app_name));
        builder.setSmallIcon(R.drawable.ic_app_logo);
        builder.setAutoCancel(true);
        builder.setPriority(PRIORITY_HIGH);
        Notification notification = builder.build();

        android.app.NotificationManager nm = (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, notification);
    }

    private GlideUrl getUrl(Context context, VoipContact contact){
        StringBuilder profileUriBuilder = new StringBuilder();
        profileUriBuilder.append(VoipConstants.VOIP_URL)
                .append("/")
                .append(URLIdentifiers.CONTACTS)
                .append("/")
                .append(contact.getId())
                .append("/")
                .append(URLIdentifiers.PICTURE);
        String basicAuth = VoipApplication.getInstance(context).getApiToken();
        DeviceSpec deviceSpec = VoipApplication.getInstance(context).getDeviceSpec();
        GlideUrl glideProfileUrl = new GlideUrl(profileUriBuilder.toString(), new LazyHeaders.Builder()
                .addHeader(URLIdentifiers.API_TOKEN, basicAuth)
                .addHeader(URLIdentifiers.APP_ID, deviceSpec.getAppId())
                .addHeader(URLIdentifiers.DEVICE_ID, deviceSpec.getDeviceId())
                .addHeader(URLIdentifiers.DEVICE_SERIAL_NUMBER, deviceSpec.getDeviceSerialNum())
                .addHeader(URLIdentifiers.IMEI, deviceSpec.getImei())
                .build());
        return glideProfileUrl;
    }

}


