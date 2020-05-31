package com.miryanova.memories.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.miryanova.memories.R;
import com.miryanova.memories.controller.CustomException;
import com.miryanova.memories.controller.RegistrationController;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText login;
    private EditText pswd;
    private EditText email;
    static RegistrationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        login = findViewById(R.id.login);
        pswd = findViewById(R.id.password);
        email = findViewById(R.id.email);
        controller = new RegistrationController(this.getApplicationContext());
    }

    public void registrate(View view) {
        String _login = login.getText().toString();
        String _pswd = pswd.getText().toString();
        String _email = email.getText().toString();
        try {
            controller.registrate(_login, _pswd, _email);
        } catch (CustomException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            login.setText("");
            pswd.setText("");
            email.setText("");
            return;
        }
        Toast.makeText(view.getContext(), "Success registration", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Authorization.class);
        startActivity(intent);
    }
}
