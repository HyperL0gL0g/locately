package com.example.fire;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class background_locationScheduler extends JobService {
    String TAG = "jobScehuler_loaction";
    boolean jobCancelled;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        updateBackgroundLocation(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobCancelled = true;
        return false;
    }

    private void updateLocation(JobParameters params) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.getFusedLocationProviderClient(getApplicationContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                String uid = FirebaseAuth.getInstance().getUid();
                Map<String,Object> map = new HashMap<>();
                map.put("lat", String.valueOf(location.getLatitude()));
                map.put("lng", String.valueOf(location.getLongitude()));
                FirebaseFirestore.getInstance().collection("user-profiles").document(uid).update(map);
                Log.d(TAG,"current lat,lng updated from background");
            }
        });
        jobFinished(params, true);
    }

    private void updateBackgroundLocation(final JobParameters params) {

        if (jobCancelled) { return; }
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateLocation(params);
            }
        }).start();

    }
}
