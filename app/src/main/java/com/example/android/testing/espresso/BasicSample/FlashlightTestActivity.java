package com.example.android.testing.espresso.BasicSample;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FlashlightTestActivity extends AppCompatActivity {

    private Button turnOnFlashlightBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight_test);

        turnOnFlashlightBtn = findViewById(R.id.turnOnFlashlightBtn);

        turnOnFlashlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn on the flashlight for testing energy consumption
                turnOnFlashlightForTest();

                // tempo que a lanterna fica ligada
                runTestForDuration(15000); // 2 seconds

                // Turn off the flashlight for the next part of the test
                turnOffFlashlightForTest();

                // tempo que a lanterna fica desligada
                runTestForDuration(15000); // 2 seconds

                // Finish the activity
                finish();
            }
        });
    }

    public void turnOnFlashlightForTest() {
        CameraManager cameraManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        }
        try {
            String cameraId = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cameraId = cameraManager.getCameraIdList()[0];
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true); // Turn on flashlight
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashlightForTest() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false); // Turn off flashlight
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void runTestForDuration(long duration) {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "FlashlightTestActivity:WakeLockTag");

        wakeLock.acquire(); // Acquire wake lock to keep the device awake during the test

        // Sleep for the specified duration
        SystemClock.sleep(duration);

        wakeLock.release(); // Release the wake lock when the test is complete
    }
}
