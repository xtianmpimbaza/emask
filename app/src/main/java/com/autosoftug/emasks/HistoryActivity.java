package com.autosoftug.emasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.autosoftug.emasks.Globals.CONFIG;
import com.autosoftug.emasks.database.DatabaseHelper;
import com.autosoftug.emasks.database.model.Note;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {
//    private NotesAdapter mAdapter;
//    private List<Note> notesList = new ArrayList<>();
//    //    private TextView noNotesView;
//    SharedPreferences.Editor editor;
//    private DatabaseHelper db;
//    private NavigationView navigationView;
//    private DrawerLayout drawerLayout;
//    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.setTitle("Payment history");

        initializeDefaultFragment(savedInstanceState, 0);

    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        }
    }




}
