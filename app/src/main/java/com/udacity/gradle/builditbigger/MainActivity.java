package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import com.udacity.gradle.builditbigger.flavour.MainActivityFragment;
import com.udacity.jokeviewer.JokeActivity;

import java.io.IOException;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity {
    LinearLayout jokeProgressLayout;
    TextView errorMessageView;
    private static final String TASK_RUNNING_KEY="task_running_key";
    boolean isTaskRunning;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTaskRunning=false;
        jokeProgressLayout=findViewById(R.id.jokeRetriveProgressView);
        errorMessageView=findViewById(R.id.errorMessageView);
        if(getSupportFragmentManager().findFragmentByTag(TAG)==null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment,new MainActivityFragment(),TAG)
                    .commit();
        }
        if(savedInstanceState!=null && savedInstanceState.containsKey(TASK_RUNNING_KEY)) {
            if(savedInstanceState.getBoolean(TASK_RUNNING_KEY)) {
                errorMessageView.setVisibility(GONE);
                new EndpointsAsyncTask().execute(this);
            }
            else {
                errorMessageView.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TASK_RUNNING_KEY,isTaskRunning);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        new EndpointsAsyncTask().execute(this);
    }
    class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
        private MyApi myApiService = null;
        private Context context;

        @Override
        protected void onPreExecute() {
            isTaskRunning=true;
            errorMessageView.setVisibility(GONE);
            jokeProgressLayout.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Context... params) {


            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                myApiService = builder.build();
            }

            context = params[0];

            try {
                return myApiService.tellJoke().execute().getJoke();
            } catch (final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorMessage(e.getMessage());
                    }
                });
                return null;
            }
        }

        @Override
        protected void onPostExecute(String joke) {
            if(joke!=null) {
                jokeProgressLayout.setVisibility(GONE);
                Intent intent=new Intent(context,JokeActivity.class);
                intent.putExtra(JokeActivity.JOKE_INTENT,joke);
                startActivity(intent);
            }
        }
    }

    private void showErrorMessage(String errorMessage) {
        isTaskRunning=false;
        jokeProgressLayout.setVisibility(GONE);
        errorMessageView.setVisibility(View.VISIBLE);
        errorMessageView.setText(errorMessage+"\nTry Again!");
    }
}
