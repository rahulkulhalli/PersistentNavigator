package com.semicolons.persitentnavigators.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolons.persitentnavigators.R;
import com.semicolons.persitentnavigators.Utility;
import com.semicolons.persitentnavigators.custom.LocationUpdaterAsync;
import com.semicolons.persitentnavigators.models.CoordinateModel;
import com.semicolons.persitentnavigators.models.EndPointsModel;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
        EditText.OnEditorActionListener, LocationUpdaterAsync.LocationUpdateCallback {

    // Texts.
    private TextInputLayout tilFrom;
    private TextInputLayout tilTo;
    private EditText edFrom;
    private EditText edTo;

    //Switcher.
    private ImageView ivLocationSwitch;

    //Marker.
    private ImageView ivLocationMarker;

    //Root view.
    private CoordinatorLayout rootView;

    //Strings.
    private String strFrom;
    private String strTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views.
        initViews();

        // Set the listeners.
        setListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location_switch:
                if (Utility.isValidString(strFrom)
                        && Utility.isValidString(strTo)) {
                    edFrom.setText(strTo);
                    edTo.setText(strFrom);
                } else {
                    showSnackBar(getString(R.string.snackbar_location_switch_error),
                            Snackbar.LENGTH_LONG);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == R.id.ed_location_from) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                if (edFrom.getText().toString().isEmpty()) {
                    tilFrom.setError(getString(R.string.til_error));
                    edFrom.setText("");

                    // Indicate that the view has consumed the event.
                    return true;
                } else {
                    if (Utility.isValidString(edFrom.getText().toString())) {
                        strFrom = edFrom.getText().toString();

                        //Transfer control to next editText.
                        edFrom.setNextFocusDownId(edTo.getId());
                    }
                }
            }
        } else if (v.getId() == R.id.ed_location_to) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (edTo.getText().toString().isEmpty()) {
                    tilTo.setError(getString(R.string.til_error));
                    edTo.setText("");

                    return true;
                } else {
                    if (Utility.isValidString(edTo.getText().toString())) {
                        strTo = edTo.getText().toString();

                        // Start the tracking procedure.
                        startTracking();
                    }
                }
            }
        }

        return false;
    }

    private void startTracking() {
        EndPointsModel model = new EndPointsModel();
        model.setStrTo(strTo);
        model.setStrFrom(strFrom);

        // Start the Async thread here.
        try {
            (new LocationUpdaterAsync(this)).execute(model);
        } catch (Exception e) {
            // Catching a broad-level exception.
            showSnackBar(e.getMessage() != null ? e.getMessage()
                            : getString(R.string.something_went_wrong),
                    Snackbar.LENGTH_LONG);
        }
    }

    private void showSnackBar(String message, int duration) {
        Snackbar.make(
                rootView,
                message,
                duration
        ).show();
    }

    private void setListeners() {
        ivLocationSwitch.setOnClickListener(this);
        ivLocationMarker.setOnClickListener(this);
        edFrom.setOnEditorActionListener(this);
        edTo.setOnEditorActionListener(this);
    }

    private void initViews() {
        ivLocationSwitch = findViewById(R.id.iv_location_switch);
        ivLocationMarker = findViewById(R.id.iv_location_marker);
        edFrom = findViewById(R.id.ed_location_from);
        edTo = findViewById(R.id.ed_location_to);
        tilTo = findViewById(R.id.til_location_to);
        tilFrom = findViewById(R.id.til_location_from);
        rootView = findViewById(R.id.root_view);
    }

    public void moveMarker() {
        // Core mechanics - Original contributor: @www.github.com/YMOS/
        int i=50;

        while(i<600) {
            moveImageView(ivLocationMarker, i, 0, 50000);
            i=i+10;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void moveImageView(View view, float toX, float toY, int duration) {
        view.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .translationX(toX)
                .translationY(toY)
                .setDuration(duration);
    }

    @Override
    public void onLocationUpdated(CoordinateModel model) {

        //TODO: Update the UI here.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Display general alert.
                showSnackBar(getString(R.string.snackbar_update_location),
                        Snackbar.LENGTH_SHORT);

                // Call the marker update method here
                moveMarker();
            }
        });
    }
}
