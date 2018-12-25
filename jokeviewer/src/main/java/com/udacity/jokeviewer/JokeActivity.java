package com.udacity.jokeviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class JokeActivity extends AppCompatActivity {
    public static final String JOKE_INTENT="joke_intent";
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        if(getSupportFragmentManager().findFragmentByTag(TAG)==null){
            Intent intent=getIntent();
            if(intent!=null) {
                if(intent.hasExtra(JOKE_INTENT)) {
                    String joke = intent.getStringExtra(JOKE_INTENT);
                    JokeFragment jokeFragment = JokeFragment.newInstance(joke);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.jokeFragmentLayout, jokeFragment,TAG)
                            .commit();
                }
            }
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
