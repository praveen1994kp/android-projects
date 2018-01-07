package com.app.com.undeafener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpeechRecognitionActivity extends AppCompatActivity implements RecognitionListener {
    private TextView tv,tvbig;
    private ProgressBar progressBar;
    private ScrollView mScrollView;
    private String lang;
    private Intent recognizerIntent;
    private SpeechRecognizer speech;
    private String LOG_TAG="Speech Recognition Activity";
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    Chronometer myChro;
    long timeElapsed;
    int hours,min,sec;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_speech_recognition);
        myChro=(Chronometer)findViewById(R.id.chronometer);
        myChro.start();
//MuteBeepSound
        /*
        AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
        */

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SpeechRecognitionActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
        lang=SettingsActivity.itemVal;
        ConnectivityManager conMang=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInf=(NetworkInfo) conMang.getActiveNetworkInfo();

        mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
        tv=(TextView) findViewById(R.id.speechText);
        tvbig=(TextView) findViewById(R.id.speechTextLarge);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        speech=SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        if( netInf != null && netInf.getType() == ConnectivityManager.TYPE_WIFI )
        {
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,lang);



        }
        else {
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        }


        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,50);
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        //recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE",true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,50);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        startfn();

        final Button stop=(Button)findViewById(R.id.button);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopfn();

                finish();
            }
        });
    }

    private void scrollToBottom()
    {
        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.smoothScrollTo(0, tv.getBottom());
            }
        });
    }


    public void restartRecognizer(){
        speech.stopListening();
        speech.destroy();
        speech=SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        speech.startListening(recognizerIntent);
    }


    public void startfn(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        speech=SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        speech.startListening(recognizerIntent);
    }


    public void stopfn(){
        myChro.stop();
        speech.stopListening();
        speech.destroy();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(LOG_TAG, "onReadyForSpeech");

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG,"OnRmsChanged"+rmsdB);
        progressBar.setProgress((int) rmsdB);

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG,"OnBufferReceived:"+ buffer);

    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG,"onEndOfSpeech");
        progressBar.setIndeterminate(true);

    }

    @Override
    public void onError(int error) {
        String errorMessage=getErrorText(error);
        if(errorMessage.equals("No match")){
            restartRecognizer();
        }
        if((errorMessage!="Client side error")&&errorMessage!="No match") {
            Log.d(LOG_TAG, "FAILED  : " + errorMessage);
            String text = (String) tv.getText();
            text += "\n FAILED" + errorMessage;
            tvbig.setText(errorMessage);
            tv.setText(text);
            scrollToBottom();
        }

    }

    @Override
    public void onResults(Bundle results) {

        timeElapsed= SystemClock.elapsedRealtime() - myChro.getBase();
        hours=(int) (timeElapsed / 3600000);
        min=(int) (timeElapsed - hours * 3600000) / 60000;
        sec=(int) (timeElapsed - hours * 3600000 - min * 60000) / 1000;

        Log.i(LOG_TAG,"onResults");
        ArrayList<String> match=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text= (String) tv.getText();
        //for(String result:match)
        if (match != null) {
            text+="\n"+min+":"+sec+"  : "+match.get(0);
        }
        tvbig.setText(match.get(0));
        tv.setText(text);
        scrollToBottom();
        restartRecognizer();

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        timeElapsed= SystemClock.elapsedRealtime() - myChro.getBase();
        hours=(int) (timeElapsed / 3600000);
        min=(int) (timeElapsed - hours * 3600000) / 60000;
        sec=(int) (timeElapsed - hours * 3600000 - min * 60000) / 1000;
        Log.i(LOG_TAG,"onPartialResults");
        ArrayList<String> match=partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text= (String) tv.getText();
        //for(String result:match)
        if (match != null) {
            text+="\n"+min+":"+sec+" : "+match.get(0);
        }
        tvbig.setText(match.get(0));
        tv.setText(text);
        scrollToBottom();
        restartRecognizer();

    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(LOG_TAG,"onEvent");

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


}
