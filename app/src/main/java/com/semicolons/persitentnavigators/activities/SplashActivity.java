package com.semicolons.persitentnavigators.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.semicolons.persitentnavigators.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        (new SplashAsync(this)).execute();
    }

    public void navigateToHomeScreen() {
        Intent navigationIntent = new Intent(this, HomeActivity.class);
        startActivity(navigationIntent);
        finish();
    }

    private static class SplashAsync extends AsyncTask<Void, Void, Void> {

        private WeakReference<SplashActivity> innerInstance;

        SplashAsync(SplashActivity referenceActivity) {
            this.innerInstance = new WeakReference<>(referenceActivity);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (innerInstance != null && innerInstance.get() != null) {
                innerInstance.get().navigateToHomeScreen();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                if (innerInstance != null && innerInstance.get() != null) {
                    innerInstance.get().navigateToHomeScreen();
                }
            }
            return null;
        }
    }
}
