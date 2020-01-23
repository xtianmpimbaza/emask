package com.autosoftug.emasks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autosoftug.emasks.Globals.CONFIG;
import com.autosoftug.emasks.Globals.GlobalFuns;
import com.autosoftug.emasks.database.DatabaseHelper;
import com.autosoftug.emasks.database.model.Note;
import com.autosoftug.emasks.utils.MyDividerItemDecoration;
import com.autosoftug.emasks.utils.RecyclerTouchListener;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    ProgressDialog progressDialog;
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    //    private TextView noNotesView;
    SharedPreferences.Editor editor;

    private DatabaseHelper db;
    @BindView(R.id.empty_notes_view)
    TextView noNotesView;
    Handler mHandler = new Handler();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        db = new DatabaseHelper(getContext());
//        notesList.clear();
        //------------------------ clear back stack
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

//        Objects.requireNonNull(getActivity()).setTitle("Home");
        mAdapter = new NotesAdapter(getContext(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, final int position) {
                showActionsDialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {
//                showActionsDialog(position);
            }
        }));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connecting....");

        new LongOperation().execute("");


        return view;
    }

    private void showActionsDialog(final int position) {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getActivity())
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Details")
                .setMessage("Time: " + notesList.get(position).getTimestamp() + "\nAmount: " + notesList.get(position).getAmount() + " " + notesList.get(position).getCurrency() + "\n Id: " + notesList.get(position).getId())
                .addButton("Close", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @SuppressLint("StaticFieldLeak")
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

            if (db.getNotesCount() > 0) {
                noNotesView.setVisibility(View.GONE);
            } else {
                noNotesView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            notesList.clear();
            noNotesView.setVisibility(View.GONE);
            db = new DatabaseHelper(getContext());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    void getTransactions() {
//        Log.e("tx_transactions", "fhgjkl");

        StringRequest stringRequest = new StringRequest(Request.Method.GET,CONFIG.GET_TX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("post_response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            db.insertTxs(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                onNetworkFailed();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);
    }


}