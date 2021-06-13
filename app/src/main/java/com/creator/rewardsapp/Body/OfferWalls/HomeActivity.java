package com.creator.rewardsapp.Body.OfferWalls;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.creator.rewardsapp.Body.Authentication.AuthTypeActivity;
import com.creator.rewardsapp.R;
import com.creator.rewardsapp.Service.NotificationJobService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth fAuth;

    FloatingActionButton fabtn;
    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener dialogClickListener;
    private String UID;

    // Shared Preferences
    SharedPreferences settingsShrepref;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "MyServicePref";

    public static final String KEY_USED_DATE = "UsedDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            UID = fAuth.getCurrentUser().getUid();
            FirebaseMessaging.getInstance().subscribeToTopic(UID);
            Intent serviceIntent = new Intent(this, NotificationJobService.class);

            serviceIntent.putExtra("inputExtra", "Now , work on notification setup");

//            NotificationJobService.enqueueWork(this, serviceIntent);
            settingsShrepref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String prevDate = settingsShrepref.getString(KEY_USED_DATE, "");
            editor = settingsShrepref.edit();


            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
            Date c = Calendar.getInstance().getTime();
            String formattedDate = sdf.format(c);
            Log.d(TAG + "Data", "scheduleMyNotificationService: Today's Date=>  " + formattedDate);
            Log.d(TAG + "Data", "scheduleMyNotificationService: Previous Date=>  " + prevDate);
            if (!prevDate.equals(formattedDate)) {

                editor.putString(KEY_USED_DATE, formattedDate);
                editor.commit();

                scheduleMyNotificationService(serviceIntent);

            }


//                scheduleJob();

        }

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabtn = findViewById(R.id.fab);


        fabtn.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action",
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        setAlertDialog();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Here, it is used to perform simple menu item operations like logout,Contact us, etc.
        performSimpleMenuOperations(navigationView);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nameHeaderView);
        if (fAuth.getCurrentUser() != null) {
            navUsername.setText(fAuth.getCurrentUser().getDisplayName());
            TextView navEmail = headerView.findViewById(R.id.emailHeaderView);
            String s = fAuth.getCurrentUser().getEmail();
            if (s != null) {
                navEmail.setText(s);
            }
        }
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.create_rewards_events_for_cust, R.id.nav_event_created_history, R.id.nav_logoutbutton)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_home);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, NotificationJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(4 * 60 * 1000)

                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("scheduler", "Job scheduled");
        } else {
            Log.d("scheduler", "Job scheduling failed");
        }
    }

    private void scheduleMyNotificationService(Intent serviceIntent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);


        PendingIntent slPendingIntent = PendingIntent.getService(this, 1, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                24 * 60 * 60 * 1000, slPendingIntent);
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void performSimpleMenuOperations(NavigationView navigationView) {
        navigationView.getMenu().findItem(R.id.nav_logoutbutton).setOnMenuItemClickListener(menuItem -> {
            fAuth.signOut();
            Intent intent = new Intent(this, AuthTypeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_use_for_customer).setOnMenuItemClickListener(menuItem -> {
            watchYoutubeVideo("2KL3pVkLUXs");
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_use_for_seller).setOnMenuItemClickListener(menuItem -> {
            watchYoutubeVideo("2KL3pVkLUXs");
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_insta).setOnMenuItemClickListener(menuItem -> {
            Toast.makeText(this, "Follow on instagram", Toast.LENGTH_SHORT).show();
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_gmail).setOnMenuItemClickListener(menuItem -> {
            Toast.makeText(this, "Email us", Toast.LENGTH_SHORT).show();
            return true;
        });


    }

    public FloatingActionButton getFloatingActionButton() {
        return fabtn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return false;
    }

    private void setAlertDialog() {
        dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    fAuth.signOut();
                    Intent intent = new Intent(this, AuthTypeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;

            }
        };

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("You won't be able to revert back, once receipt starts uploading.  Are you sure want to upload ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.act_logout) {
            Log.d(TAG, "logout: Done");
//            SharedPreferences sharedPreferences = getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(PrefVariables.ISLOGIN, false);
//            editor.putBoolean(PrefVariables.IS_REGISTERED, false);
//            editor.commit();

            fAuth.signOut();
            Intent intent = new Intent(this, AuthTypeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}