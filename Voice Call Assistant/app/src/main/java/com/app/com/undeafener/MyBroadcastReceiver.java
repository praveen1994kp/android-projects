package com.app.com.undeafener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by Praveen on 17-Mar-16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    String LOG;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(LOG, "Intent recieved: ");
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            //AutoAnswer Not Working
            /*



            Intent buttonDown=new Intent(Intent.ACTION_MEDIA_BUTTON);
            buttonDown.putExtra(Intent.EXTRA_KEY_EVENT,new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK));
            context.sendOrderedBroadcast(buttonDown,"android.permission.CALL_PRIVILEGED");
*/


            final Intent intent1=new Intent(context,PopUpActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent1.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(intent1);
                }
            }, 2000);

        }
        else if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Log.i(LOG, "Call Going");
            final Intent intent1=new Intent(context,PopUpActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent1.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(intent1);
                }
            }, 2000);

        }

    }
}
