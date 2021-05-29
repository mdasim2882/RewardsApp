package com.creator.rewardsapp.Body.OfferWalls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity  {
    private final String TAG = getClass().getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth fAuth;

    FloatingActionButton fabtn;
    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener dialogClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabtn = findViewById(R.id.fab);
        fAuth = FirebaseAuth.getInstance();


        fabtn.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action",
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        setAlertDialog();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

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