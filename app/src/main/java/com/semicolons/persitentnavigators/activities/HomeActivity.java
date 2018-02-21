package com.semicolons.persitentnavigators.activities;

import android.os.Bundle;
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

    //Strings.
    private String strFrom;
    private String strTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        //moveMarker();
        setListeners();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == R.id.ed_location_from) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                if (edFrom.getText().toString().isEmpty()) {
                    tilFrom.setError(getString(R.string.til_error));
                    edFrom.setText("");

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
        (new LocationUpdaterAsync(this)).execute(model);
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
    }

    public void moveMarker() {
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

        // Call the marker update method here
        moveMarker();
    }
}
