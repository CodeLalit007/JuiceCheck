package com.example.juicecheck;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.BatteryManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity {

    private BatteryManager batteryManager;
    private IntentFilter intentFilter;
    private TextView batteryLevelTextView;
    private TextView batteryStatusTextView;
    private TextView batteryUsageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryLevelTextView = findViewById(R.id.battery_level_text_view);
        batteryStatusTextView = findViewById(R.id.battery_status_text_view);
        batteryUsageTextView = findViewById(R.id.battery_usage_text_view);

        registerReceiver(batteryInfoReceiver, intentFilter);
    }



    private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String batteryStatusString = "";

            switch (batteryStatus) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    batteryStatusString = "Charging";

                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    batteryStatusString = "Discharging";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    batteryStatusString = "Full";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    batteryStatusString = "Not charging";
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    batteryStatusString = "Unknown";
                    break;
            }

            long batteryUsage = 0;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                BatteryUsageStats batteryUsageStats = new BatteryUsageStats(context);
                batteryUsage = batteryUsageStats.getBatteryUsage();
            }

            batteryLevelTextView.setText(String.format("Battery level: %d%%", batteryLevel));
            batteryStatusTextView.setText(String.format("Battery status: %s", batteryStatusString));
            batteryUsageTextView.setText(String.format("Battery Charge Time Remaining: %d minutes", batteryManager.computeChargeTimeRemaining()/(1000*60)));
        }
    };



}