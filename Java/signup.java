package com.MrAndMiss.admin.mrmrspu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static com.MrAndMiss.admin.mrmrspu.MainActivity.isNetworkAvaliable;

/**
 * Created by ADMIN on 3/11/2017.
 */

public class signup extends Activity implements View.OnClickListener{

    private DatabaseReference databaseReference;
private EditText username;
private EditText passwordt,cnfmpswd;
   private Button signup,signin;
    private TextView txt1,txt2,txt3;
FirebaseAuth firebaseAuth;
   // private FirebaseUser currentuser;
    //DatabaseReference mdatabase;
private ProgressDialog ProgressDi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ProgressDi=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
       // currentuser=firebaseAuth.getCurrentUser();
       // mdatabase = FirebaseDatabase.getInstance().getReference("users");
        databaseReference = FirebaseDatabase.getInstance().getReference("usernames");

    username=(EditText)findViewById(R.id.usernamesignup);
        passwordt=(EditText)findViewById(R.id.passwordsignup);
        cnfmpswd=(EditText)findViewById(R.id.conformpassword);
        signup=(Button)findViewById(R.id.signupbtn);
        signin=(Button)findViewById(R.id.signinbtn);
        txt1=(TextView)findViewById(R.id.textView);
        txt2=(TextView)findViewById(R.id.textView5);
        txt3=(TextView)findViewById(R.id.textView12);
        signup.setOnClickListener(this);
signin.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == signup) {

            final String email= username.getText().toString().trim();
            final String password=passwordt.getText().toString().trim();
  String conform=cnfmpswd.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                username.setError("enter Email address.");
                //Toast.makeText(this,"enter email eddress",Toast.LENGTH_SHORT).show();
            }
           else if (TextUtils.isEmpty(password)) {
                //Toast.makeText(this,"enter password",Toast.LENGTH_SHORT).show();
                //return;
                passwordt.setError("enter password.");

            }
            else if (TextUtils.isEmpty(conform)) {
                //Toast.makeText(this,"enter password",Toast.LENGTH_SHORT).show();
                //return;
                cnfmpswd.setError("enter password.");

            }
            else if(!Objects.equals(password, conform)){
                cnfmpswd.setError("password does not match!");
            }



            else {

                signupmthd();
            }
        }
        if(v==signin){

            startActivity(new Intent(this,MainActivity.class));
        }

    }

    private void signupmthd() {

        final String email= username.getText().toString().trim();
        final String password=passwordt.getText().toString().trim();

        ProgressDi.setMessage("Creating your Account.Please Wait...");
        ProgressDi.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                        ProgressDi.cancel();
                        username.setError("User with this Email already exist.");
                    }
                    else{


                        username.setError("Enter valid Email Address.");

                    }

                }
                if(isNetworkAvaliable(getApplicationContext()) == false){
                    ProgressDi.cancel();
                    Toast.makeText(getApplicationContext(), "Internet connection required.", Toast.LENGTH_SHORT).show();
                }
                if(task.isSuccessful()){


                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("firstname").setValue(getIntent().getStringExtra("firstname"));
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("lastname").setValue(getIntent().getStringExtra("lastname"));
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("username").setValue(getIntent().getStringExtra("username"));
                    ProgressDi.cancel();
                    startActivity(new Intent(getApplicationContext(),tabbed.class));


                }




            }
        });

    }
}
