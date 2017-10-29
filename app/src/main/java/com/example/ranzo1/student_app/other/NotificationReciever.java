package com.example.ranzo1.student_app.other;

/**
 * Created by ranzo1 on 3.2.2017..
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.mainActivities.EventsActivity;

/** * Created by starapps on 1/24/2017. */
public class NotificationReciever extends BroadcastReceiver
{

    @Override public void onReceive(Context context, Intent intent){

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context,EventsActivity.class);

        intent1.putExtra("turnOf",true);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //if we want ring on notifcation then uncomment below line//
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.student_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.student_logo))
                .setContentIntent(pendingIntent)
                .setContentText("Tap to see events.")
                . setContentTitle("You have events!")
                ;


        builder.setSound(alarmSound).setAutoCancel(true);
        notificationManager.notify(100,builder.build());
        builder.setAutoCancel(true);



    }
}