package com.mobile.findfreinds;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MySMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String messageBody,phoneNumber;
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle =intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    messageBody = messages[0].getMessageBody();
                    phoneNumber = messages[0].getDisplayOriginatingAddress();
                    Toast.makeText(context, "Message : "+messageBody+"Reçu de la part de;"+ phoneNumber, Toast.LENGTH_LONG ) .show();
                    if (messageBody.contains("findFreinds: envoyer moi votre position")) {
                        //lancement service
                        Intent x = new Intent(context, MyLocationService.class);
                        x.putExtra("num",phoneNumber);
                        context.startService(x);
                    }
                    if (messageBody.contains("findfriends: ma position est #")){
                        // lancer une notification dans le canal myapplication_channel

                        String t[]=messageBody.split("#");
                        String latitude=t[1];
                        String longitude=t[2];


                        NotificationCompat.Builder mynotif = new NotificationCompat.Builder(context.getApplicationContext(), "myapplication_channel");
                        mynotif.setContentTitle("titre içi!");
                        mynotif.setContentText("votre message.");
                        mynotif.setSmallIcon(android.R.drawable.ic_dialog_map);
                        mynotif.setAutoCancel(true);
                        mynotif.setVibrate(new long[]{ 500,1000,200,2000});
                        Uri son= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        mynotif.setSound(son);

                        Intent i = new Intent(context,MapsActivity.class);
                        i.putExtra("longitude",longitude);
                        i.putExtra("latitude",latitude);
                        PendingIntent pi = PendingIntent.getActivity(context,1,i,PendingIntent.FLAG_MUTABLE);
                        mynotif.setContentIntent(pi);

                        NotificationManagerCompat manager= NotificationManagerCompat.from(context.getApplicationContext());
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            /* creation du canal si la version android de l'appareil est supérieur à Oreo */
                            NotificationChannel canal=new NotificationChannel("myapplication_channel",
                                    // l'ID exacte du canal "canal pour lapplication find me"
                                    "myapplication_channel",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            AudioAttributes attr=new AudioAttributes.Builder() .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) .setUsage(AudioAttributes.USAGE_ALARM) .build();
                            // ajouter du son pour le canal
                            canal.setSound(son,attr);
                            // creation du canal dans l'appareil
                            manager.createNotificationChannel(canal); }
                        //Lancement de la notification
                        manager.notify(0,mynotif.build());
                    }
                }
            }
        }

    }
}
