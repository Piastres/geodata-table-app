package com.piastres.geodatatableapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.piastres.geodatatableapp.R;
import com.piastres.geodatatableapp.controllers.ConnectionController;
import com.piastres.geodatatableapp.errors.ErrorDescriptor;
import com.piastres.geodatatableapp.fragments.LoginErrorFragment;
import com.piastres.geodatatableapp.fragments.RequestErrorFragment;
import com.piastres.geodatatableapp.models.LoginResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface font = Typeface.createFromAsset(getAssets(), "fa_solid.ttf" );

        EditText textUsername = findViewById(R.id.loginActivityUsername);
        EditText textPassword = findViewById(R.id.loginActivityPassword);
        Button buttonLogin = findViewById(R.id.loginActivityButton);
        TextView textIconUser = findViewById(R.id.loginActivityUserIcon);
        TextView textIconKey = findViewById(R.id.loginActivityKeyIcon);

        textIconKey.setTypeface(font);
        textIconUser.setTypeface(font);

        buttonLogin.setOnClickListener(v -> {
            String username = String.valueOf(textUsername.getText());
            String password = String.valueOf(textPassword.getText());
            if (isLoginFromEmpty(username, password)) {
                String emptyForm = getResources().getString(R.string.login_empty_form);
                showErrorDialogFragment(emptyForm);
            } else {
                getAuth(username, password);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getAuth(String username, String password) {
        ConnectionController.getApi().getAuth(username, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(__ -> setProgressBarVisible(true))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveData
                        ,
                    error -> {
                        ErrorDescriptor errorDescriptor = new ErrorDescriptor();
                        showErrorDialogFragment(errorDescriptor.checkError(error));
                    }
                );
    }

    /**
     * TODO: change method name
     * @param loginResponse
     */

    private void saveData(LoginResponse loginResponse) {
        if (!loginResponse.getStatus().equals("ok")) {
            LoginErrorFragment loginErrorFragment = new LoginErrorFragment();
            loginErrorFragment.show(getSupportFragmentManager(), "fragment_login_error");
        } else {
            Intent intent = new Intent(getBaseContext(), GeodataListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("USER_CODE", loginResponse.getCode());
            intent.putExtras(bundle);
            this.startActivity(intent);
        }
        setProgressBarVisible(false);
    }

    private void setProgressBarVisible(boolean isVisible) {
        ProgressBar progressBar = findViewById(R.id.loginProgressBar);
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    private void showErrorDialogFragment(String error) {
        Bundle bundle = new Bundle();
        bundle.putString("ERROR_TITLE", error);
        RequestErrorFragment requestErrorFragment = new RequestErrorFragment();
        requestErrorFragment.setArguments(bundle);
        requestErrorFragment.show(getSupportFragmentManager(), "fragment_request_error");
    }

    private boolean isLoginFromEmpty(String username, String password) {
        try {
            return username.equals("") || password.equals("");
        } catch (NullPointerException e) {
            return true;
        }
    }
}
