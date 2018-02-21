package com.semicolons.persitentnavigators.custom;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semicolons.persitentnavigators.models.CoordinateModel;
import com.semicolons.persitentnavigators.models.EndPointsModel;

public class LocationUpdaterAsync extends AsyncTask<EndPointsModel, Void, CoordinateModel> {

    public interface LocationUpdateCallback {
        void onLocationUpdated(CoordinateModel model);
    }

    private LocationUpdateCallback callBack;

    public LocationUpdaterAsync(AppCompatActivity hActivity) {
        callBack = (LocationUpdateCallback) hActivity;
    }

    @Override
    protected CoordinateModel doInBackground(EndPointsModel... endPointsModels) {

        Gson requestGson = new GsonBuilder().serializeNulls().create();

        try {

            String payloadStr = requestGson.toJson(endPointsModels[0],
                    EndPointsModel.class);

            //TODO: Send API request with above payload here.

            // On n/w response:
            // Simply convert the response parameters to the Serialized model.
            // Supposing response is the following:
            String response = "{\"x_coordinate\": 23.112, \"y_coordinate\": 11.1141}";
            CoordinateModel updatedLocations = requestGson
                    .fromJson(response, CoordinateModel.class);

        } catch (Exception e) {
            e.printStackTrace();
            //TODO handle exception here.
        }

        //TODO: Do a bunch of processing here, probably convert models using Gson.
        return null;
    }

    @Override
    protected void onPostExecute(CoordinateModel coordinateModel) {
        super.onPostExecute(coordinateModel);

        // Issuing a call back to the activity.
        callBack.onLocationUpdated(coordinateModel);
    }
}
