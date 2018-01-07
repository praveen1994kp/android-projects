package com.app.com.undeafener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;

public class MainActivity extends AppIntro2 {


    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.intro_1));
        addSlide(SampleSlide.newInstance(R.layout.intro_2));
        addSlide(SampleSlide.newInstance(R.layout.intro_3));
        addSlide(SampleSlide.newInstance(R.layout.intro_4));

    }

    @Override
    public void onDonePressed() {
        Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
