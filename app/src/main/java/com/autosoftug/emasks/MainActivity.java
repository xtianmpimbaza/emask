package com.autosoftug.emasks;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.autosoftug.emasks.Globals.CONFIG;
import com.autosoftug.emasks.database.DatabaseHelper;
import com.autosoftug.emasks.database.model.Note;
import com.autosoftug.emasks.utils.MyDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    //    @BindView(R.id.lnr_btc)
//    LinearLayout etqr;
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    //    private TextView noNotesView;
    SharedPreferences.Editor editor;
    private DatabaseHelper db;

    @OnClick(R.id.lnr_btc)
    void payToBtc() {
        Intent i = new Intent(MainActivity.this, BitcoinActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lnr_ethereum)
    void payToEth() {
        Intent i = new Intent(MainActivity.this, EthereumActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lnr_libra)
    void payToLibra() {
        Intent i = new Intent(MainActivity.this, LibraActivity.class);
        startActivity(i);
    }


    @OnClick(R.id.lnr_visa)
    void payVisa() {
        Intent i = new Intent(MainActivity.this, VisaActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lnr_momo)
    void payMomo() {
        Intent i = new Intent(MainActivity.this, MomoActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lnr_paypal)
    void payPayPal() {
        Intent i = new Intent(MainActivity.this, PaypalActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Home");
        ButterKnife.bind(this);

        db = new DatabaseHelper(this);
        notesList.clear();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mAdapter = new NotesAdapter(this, notesList);
    }

    private class LongOperation extends AsyncTask<String, Void, List<Note>> {

        @Override
        protected List<Note> doInBackground(String... params) {
            getTransactions();
            return db.getAllNotes();
        }

        @Override
        protected void onPostExecute(List<Note> result) {
            notesList.addAll(result);
            mAdapter.notifyDataSetChanged();
//            if (db.getNotesCount() > 0) {
//                noNotesView.setVisibility(View.GONE);
//            } else {
//                noNotesView.setVisibility(View.VISIBLE);
//            }
        }

        @Override
        protected void onPreExecute() {
            notesList.clear();
//            noNotesView.setVisibility(View.GONE);
//            db = new DatabaseHelper(MainActivity.this);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    void getTransactions() throws NullPointerException {
        RequestQueue queue = Volley.newRequestQueue(this);
        final JSONObject params = new JSONObject();

        try {
//            params.put("method", "transactions");
            params.put("user", getPhone());
//            params.put("number", 10);
        } catch (JSONException e) {
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, CONFIG.BASE_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String stat = response.toString();
                        JSONObject jsnobject = null;
                        try {
                            jsnobject = new JSONObject(stat);
                            JSONArray jsonArray = jsnobject.getJSONArray("response");
                            db.insertTxs(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(req);
    }

    private String getPhone() {
        SharedPreferences user_pref = getSharedPreferences("USER", MODE_PRIVATE);
        return user_pref.getString("phone_number", null);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Stay home after click
        } else if (id == R.id.nav_istory) {
            Intent i = new Intent(this, HistoryActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_call) {
            Toast.makeText(this, "Initialising a call", Toast.LENGTH_SHORT).show();
            String posted_by = "256787344529";
            String uri = "tel:" + posted_by.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
