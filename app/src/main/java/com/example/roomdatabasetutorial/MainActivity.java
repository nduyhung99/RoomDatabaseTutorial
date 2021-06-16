package com.example.roomdatabasetutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomdatabasetutorial.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 10;
    private EditText txtUsername, txtAddress;
    private Button btnAddUser;
    private RecyclerView rcvUser;

    private UserAdapter userAdapter;
    private List<User> mListUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        userAdapter=new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }
        });
        mListUser= new ArrayList<>();
        userAdapter.setData(mListUser);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(userAdapter);
        loadData();

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

    }

    private void clickUpdateUser(User user) {
        Intent intent= new Intent(MainActivity.this,UpdateActivity.class);
        Bundle bundle= new Bundle();
        bundle.putSerializable("OBJECT_USER",user);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void addUser() {
        String strUsername = txtUsername.getText().toString().trim();
        String strAddress = txtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }

        User user=new User(strUsername,strAddress);
        if (isUserExist(user)){
            Toast.makeText(this,"User exist",Toast.LENGTH_SHORT).show();
            return;
        }
        UserDatabase.getInstance(this).userDAO().insertUser(user);
        Toast.makeText(this,"Add user successfully",Toast.LENGTH_SHORT).show();

        txtAddress.setText("");
        txtUsername.setText("");
        hideSoftKeyboard();

        loadData();

    }

    private void addControls() {
        txtAddress=findViewById(R.id.txtAddress);
        txtUsername=findViewById(R.id.txtUsername);
        btnAddUser=findViewById(R.id.btnAddUser);
        rcvUser=findViewById(R.id.rcvUser);
    }

    public void hideSoftKeyboard(){
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    private void loadData(){
        mListUser=UserDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(mListUser);
    }

    private boolean isUserExist(User user){
        List<User> list= UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return list!=null && !list.isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode==Activity.RESULT_OK){
            loadData();
        }
    }
}