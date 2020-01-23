package com.autosoftug.emasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.autosoftug.emasks.Globals.CONFIG;
import com.autosoftug.emasks.Globals.GlobalFuns;
import com.autosoftug.emasks.Globals.PrefManager;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisaActivity extends AppCompatActivity {
    PrefManager prefManager;
    ProgressDialog progressDialog;
    String payid = "";
    GlobalFuns globalfun;

    @BindView(R.id.input_address)
    EditText inputAddress;
    @BindView(R.id.amount)
    EditText inputAmount;
    @OnClick(R.id.btn_send)
    void callSend() {
        this.send();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        this.setTitle("Pay to visa");
        ButterKnife.bind(this);

        prefManager = new PrefManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending .....");

    }

    void send() {
        RequestQueue queue = Volley.newRequestQueue(this);
        progressDialog.show();
        final JSONObject params = new JSONObject();

        String address = inputAddress.getText().toString();
        String amount = inputAmount.getText().toString();

        try {
            params.put("phone", "0787344529");
            params.put("method", "nisa");
            params.put("receipient", address);
            params.put("amount", amount);
            params.put("currency", "usd");

        } catch (JSONException e) {
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, CONFIG.SAVE_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onNetworkSuccessful(params);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onNetworkFailed();
            }
        });
        queue.add(req);
    }

    void onNetworkSuccessful(final JSONObject params) {
        progressDialog.dismiss();
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                .setTitle("Sent")
                .setIcon(getResources().getDrawable(R.drawable.ic_complete))
                .setMessage("Sent Successfully");

        builder.onDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent i = new Intent(VisaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        builder.show();
    }

    void onNetworkFailed() {
        progressDialog.dismiss();
        final CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                .setTitle("Error")
                .setIcon(getResources().getDrawable(R.drawable.ic_failed))
                .setMessage("Failed, check your internet connection and try again")
                .addButton("Retry", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        send();
                    }
                })
                .addButton("Cancel", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
