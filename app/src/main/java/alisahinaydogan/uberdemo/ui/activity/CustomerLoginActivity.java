package alisahinaydogan.uberdemo.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alisahinaydogan.uberdemo.ui.Fragment.DriverMapActivity;
import alisahinaydogan.uberdemo.R;


public class CustomerLoginActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLogin,mRegisteration;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mEmail=(EditText)findViewById(R.id.email);
        mPassword=(EditText)findViewById(R.id.password);
        mLogin=(Button) findViewById(R.id.login);
        mRegisteration=(Button) findViewById(R.id.register);




        mAuth=FirebaseAuth.getInstance();
        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){

                    Intent intent= new Intent(CustomerLoginActivity.this,DriverMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };



        mRegisteration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(CustomerLoginActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id).child("name");
                            current_user_db.setValue(email);
                        }
                    }
                });
            }
        });




        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){

                            Toast.makeText(CustomerLoginActivity.this, "Sign in Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
}
