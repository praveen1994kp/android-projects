package com.app.com.undeafener;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;

public class PopUpActivity extends AppCompatActivity {
    Window win;
    Button caption,dismiss;
    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            requestWindowFeature(Window.FEATURE_NO_TITLE);


            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_pop_up);
            win=this.getWindow();


            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            win.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            //win.addFlags(WindowManager.LayoutParams.TYPE_PHONE);
            //win.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
           // win.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            //win.addFlags(WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY);
            win.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            win.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            DisplayMetrics dm=new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width=dm.widthPixels;
            int height=dm.heightPixels;
            getWindow().setLayout(width,(int)(height*0.4));
        }
        catch (Exception e){
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
        am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

        caption=(Button)findViewById(R.id.CaptionLaunch);
        dismiss=(Button)findViewById(R.id.DismissButton);
        caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Answer Call Method 1(Requires Root Access,Doesn't invoke recognizer thread
                /*
                try{
                    Thread.sleep(800);
                    Process process=Runtime.getRuntime().exec(new String[]{ "su","-c","input keyevent 5"});
                    process.waitFor();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
                //Method 2:Simulate BT Headset (Not Working)
                /*
                Intent buttonUp=new Intent(Intent.ACTION_MEDIA_BUTTON);
                buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK));
                getBaseContext().sendOrderedBroadcast(buttonUp,"android.permission.CALL_PRIVILEGED");
*/
                        //setSpeakerOn
                        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                am.setMode(AudioManager.MODE_IN_CALL);
                am.setSpeakerphoneOn(true);
                Intent intent=new Intent("com.app.com.SpeechRecognitionActivity");
                startActivity(intent);
                finish();

            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
