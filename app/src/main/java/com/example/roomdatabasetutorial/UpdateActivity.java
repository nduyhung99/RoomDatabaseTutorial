package com.example.roomdatabasetutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomdatabasetutorial.database.UserDatabase;

public class UpdateActivity extends AppCompatActivity {

    private EditText txtUsername, txtAddress;
    private Button btnUpdateUser;

    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        txtAddress=findViewById(R.id.txtAddress);
        txtUsername=findViewById(R.id.txtUsername);
        btnUpdateUser=findViewById(R.id.btnUpdateUser);

        mUser= (User) getIntent().getExtras().get("OBJECT_USER");
        if (mUser!=null){
            txtAddress.setText(mUser.getAddress());
            txtUsername.setText(mUser.getUsername());
        }

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String strUsername = txtUsername.getText().toString().trim();
        String strAddress = txtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }

        mUser.setUsername(strUsername);
        mUser.setAddress(strAddress);

        UserDatabase.getInstance(this).userDAO().updateUser(mUser);
        Toast.makeText(this,"Update user successfully",Toast.LENGTH_SHORT).show();

        Intent intentResult = new Intent();
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }
}