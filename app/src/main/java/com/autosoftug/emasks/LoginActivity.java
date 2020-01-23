package com.autosoftug.emasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.autosoftug.emasks.Globals.PrefManager;
import com.autosoftug.emasks.ui.login.LoginViewModel;
import com.github.nikartm.button.FitButton;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    @BindView(R.id.momo_txt_field)
    EditText momo_number;
    private PrefManager prefManager;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        prefManager = new PrefManager(this);

//        if (!prefManager.isNotLoggedIn()) {
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i);
//            finish();
//        }
//        final EditText usernameEditText = findViewById(R.id.username);
//        final EditText passwordEditText = findViewById(R.id.password);
        final FitButton loginButton = findViewById(R.id.login);
//        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//                String mm = momo_number.getText().toString();
////                if (!mm.isEmpty()){
//                    editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
//                    editor.putString("phone_number", mm);
//                    editor.apply();
//                    prefManager.setIsNotLoggedIn(false);

//                    finish();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
//                }else Toast.makeText(LoginActivity.this, "Initialising a call", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
