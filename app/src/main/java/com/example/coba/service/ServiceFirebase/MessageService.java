package com.example.coba.service.ServiceFirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.coba.MainActivity;
import com.example.coba.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessageService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    public static final String ACTION_NEED_INFO = "com.example.coba.INFO";
    public static final String ACTION_DETAIL_DONOR = "com.bodhidwi.donordarah.DETAIL_DONOR";
    public Bundle messageBody;
    String id;
    private NotificationChannel mChannel;
    NotificationManager notificationManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.e(TAG, "From: " + remoteMessage.getFrom());



        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
        }
        String message = remoteMessage.getData().get("message");
        Log.d(TAG,"Message: "+message);
        String title=remoteMessage.getNotification().getTitle();
        if (title.contains("INFO")){
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
                messageBody = new Bundle();
            }
            String bla = remoteMessage.getNotification().getBody();
           sendNotification1( bla,title);
        }
        if (title.contains("Donor dibatalkan oleh penerima")){
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
                messageBody = new Bundle();
                Map<String,String> map  = remoteMessage.getData();
                messageBody.putString("ids",map.get("id"));
                id=map.get("id");
                Log.e(TAG, "id from servise : "+id);
                Log.e(TAG, "id from servise : "+messageBody.toString());
            }
            String bla = remoteMessage.getNotification().getBody();
            sendNotification4(id, bla,title);
        }
        if (title.contains("Donor dibatalkan oleh user donor")){
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
                messageBody = new Bundle();
                Map<String,String> map  = remoteMessage.getData();
                messageBody.putString("ids",map.get("id"));
                id=map.get("id");
                Log.e(TAG, "id from servise : "+id);
                Log.e(TAG, "id from servise : "+messageBody.toString());
            }
            String bla = remoteMessage.getNotification().getBody();
            sendNotification5(id, bla,title);
        }
        if (title.contains("Donor disetujui")){
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
                messageBody = new Bundle();
                Map<String,String> map  = remoteMessage.getData();
                messageBody.putString("ids",map.get("id"));
                id=map.get("id");
                Log.e(TAG, "id from servise : "+id);
                Log.e(TAG, "id from servise : "+messageBody.toString());
            }
            String bla = remoteMessage.getNotification().getBody();
            sendNotification2(id, bla,title);
        }
        if (title.contains("Sudah donor")){
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
                messageBody = new Bundle();
                Map<String,String> map  = remoteMessage.getData();
                messageBody.putString("ids",map.get("id"));
                id=map.get("id");
                Log.e(TAG, "id from servise : "+id);
                Log.e(TAG, "id from servise : "+messageBody.toString());
            }
            String bla = remoteMessage.getNotification().getBody();
            sendNotification3(id, bla,title);
        }


    }
//    private void sendNotification(Bundle messageBody, String bla,String title) {
//
//        int requestID = (int) System.currentTimeMillis();
//
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.setAction("needblood");
//        intent.putExtra("data",messageBody);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //String channelId = getString(R.string.default_notification_channel_id);
//        Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("hahaha")
//                        .setContentText(bla)
//                        .setAutoCancel(true)
//                        .setSound(alarmSound)
//                        .setContentIntent(pendingIntent);
//
//        notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
//    }
    private void sendNotification1(String bla,String title) {
        if(notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder;
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(ACTION_NEED_INFO);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            int importance=NotificationManager.IMPORTANCE_HIGH;
            if (mChannel==null){
                mChannel=new NotificationChannel("0",title,importance);
                mChannel.setDescription(bla);
                mChannel.enableVibration(true);
                notificationManager.createNotificationChannel(mChannel);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this,1251,intent,PendingIntent.FLAG_ONE_SHOT);
            builder =
                    new NotificationCompat.Builder(this,"0")
                            .setSmallIcon(R.drawable.elektro)
                            .setContentTitle("INFO")
                            .setContentText(bla)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setSound(alarmSound)
                            .setContentIntent(pendingIntent);
            Notification notification=builder.build();
            notificationManager.notify(100,notification);
        }else{


            int requestID = (int) System.currentTimeMillis();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(ACTION_NEED_INFO);
            intent.putExtra("data",id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,requestID,intent,PendingIntent.FLAG_ONE_SHOT);

            //String channelId = getString(R.string.default_notification_channel_id);
            Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.elektro)
                            .setContentTitle("INFO")
                            .setContentText(bla)
                            .setAutoCancel(true)
                            .setSound(alarmSound)
                            .setContentIntent(pendingIntent);

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(100 /* ID of notification */, notificationBuilder.build());

        }

    }
    private void sendNotification2(String ids, String bla,String title) {
        if(notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder;
//            Intent intent = new Intent(this, HistoryActivity.class);
            //intent.setAction(ACTION_DETAIL_DONOR);
//            intent.setAction("Terima");
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent;
//            int importance=NotificationManager.IMPORTANCE_HIGH;
//            if (mChannel==null){
//                mChannel=new NotificationChannel("0",title,importance);
//                mChannel.setDescription(bla);
//                mChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(mChannel);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            pendingIntent = PendingIntent.getActivity(this,1251,intent,PendingIntent.FLAG_ONE_SHOT);
//            builder =
//                    new NotificationCompat.Builder(this,"0")
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setDefaults(Notification.DEFAULT_ALL)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//            Notification notification=builder.build();
//            notificationManager.notify(100,notification);
        }else {
            Log.e(TAG, "id in notification : " + ids);
            int requestID = (int) System.currentTimeMillis();

//            Intent intent = new Intent(this, HistoryActivity.class);
//            //intent.setAction(ACTION_DETAIL_DONOR);
//            intent.setAction("Terima");
//            intent.putExtra("data", id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_ONE_SHOT);
//
//            //String channelId = getString(R.string.default_notification_channel_id);
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//
//            notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(100 /* ID of notification */, notificationBuilder.build());
        }

    }
    private void sendNotification3(String ids, String bla,String title) {
        if(notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder;
//            Intent intent = new Intent(this, HistoryActivity.class);
//            //intent.setAction(ACTION_NEED_BLOOD);
//            intent.setAction("Terima");
//            intent.putExtra("data",id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent;
//            int importance=NotificationManager.IMPORTANCE_HIGH;
//            if (mChannel==null){
//                mChannel=new NotificationChannel("0",title,importance);
//                mChannel.setDescription(bla);
//                mChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(mChannel);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            pendingIntent = PendingIntent.getActivity(this,1251,intent,PendingIntent.FLAG_ONE_SHOT);
//            builder =
//                    new NotificationCompat.Builder(this,"0")
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setDefaults(Notification.DEFAULT_ALL)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//            Notification notification=builder.build();
//            notificationManager.notify(100,notification);
        }else {
            Log.e(TAG, "id in notification : " + ids);
            int requestID = (int) System.currentTimeMillis();

//            Intent intent = new Intent(this, HistoryActivity.class);
//            //intent.setAction(ACTION_DETAIL_DONOR);
//            intent.setAction("Terima");
//            intent.putExtra("data", id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_ONE_SHOT);
//
//            //String channelId = getString(R.string.default_notification_channel_id);
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//
//            notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(100 /* ID of notification */, notificationBuilder.build());
        }

    }
    private void sendNotification4(String ids, String bla,String title) {
        if(notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder;
//            Intent intent = new Intent(this, HistoryActivity.class);
//            //intent.setAction(ACTION_NEED_BLOOD);
//            intent.setAction("Donor");
//            intent.putExtra("data",id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent;
//            int importance=NotificationManager.IMPORTANCE_HIGH;
//            if (mChannel==null){
//                mChannel=new NotificationChannel("0",title,importance);
//                mChannel.setDescription(bla);
//                mChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(mChannel);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            pendingIntent = PendingIntent.getActivity(this,1251,intent,PendingIntent.FLAG_ONE_SHOT);
//            builder =
//                    new NotificationCompat.Builder(this,"0")
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setDefaults(Notification.DEFAULT_ALL)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//            Notification notification=builder.build();
//            notificationManager.notify(100,notification);
        }else {
            Log.e(TAG, "id in notification : " + ids);
            int requestID = (int) System.currentTimeMillis();

//            Intent intent = new Intent(this, HistoryActivity.class);
//            //intent.setAction(ACTION_DETAIL_DONOR);
//            intent.setAction("Donor");
//            intent.putExtra("data", id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_ONE_SHOT);

            //String channelId = getString(R.string.default_notification_channel_id);
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//
//            notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(100 /* ID of notification */, notificationBuilder.build());
        }

    }
    private void sendNotification5(String ids, String bla,String title) {
        if(notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder;
//            Intent intent = new Intent(this, HistoryActivity.class);
//            //intent.setAction(ACTION_NEED_BLOOD);
//            intent.setAction("Terima");
//            intent.putExtra("data",id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent;
//            int importance=NotificationManager.IMPORTANCE_HIGH;
//            if (mChannel==null){
//                mChannel=new NotificationChannel("0",title,importance);
//                mChannel.setDescription(bla);
//                mChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(mChannel);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            pendingIntent = PendingIntent.getActivity(this,1251,intent,PendingIntent.FLAG_ONE_SHOT);
//            builder =
//                    new NotificationCompat.Builder(this,"0")
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setDefaults(Notification.DEFAULT_ALL)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//            Notification notification=builder.build();
//            notificationManager.notify(100,notification);
        }else {
            Log.e(TAG, "id in notification : " + ids);
            int requestID = (int) System.currentTimeMillis();

//            Intent intent = new Intent(this, HistoryActivity.class);
//            //intent.setAction(ACTION_DETAIL_DONOR);
//            intent.setAction("Terima");
//            intent.putExtra("data", id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_ONE_SHOT);

            //String channelId = getString(R.string.default_notification_channel_id);
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(title)
//                            .setContentText(bla)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//            notificationManager.notify(100 /* ID of notification */, notificationBuilder.build());
        }

    }
}
