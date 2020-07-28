package com.MrAndMiss.admin.mrmrspu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   private EditText usernamefield;
    private EditText passwordfield;
    private Button btsignin,txsignup,txtfrgtpswrd;
ImageView imgvwcrwn,logo;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mdatabase;
    private FirebaseUser user;

    //FirebaseUser user;

    private ProgressDialog ProgressDi;
    private static final long delay = 2000L;
    private boolean mRecentlyBackPressed = false;
    private Handler mExitHandler = new Handler();
    private Runnable mExitRunnable = new Runnable() {

        @Override
        public void run() {
            mRecentlyBackPressed=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDi = new ProgressDialog(this);
//        user=firebaseAuth.getCurrentUser();
mdatabase=FirebaseDatabase.getInstance().getReference().child("users");
        usernamefield = (EditText) findViewById(R.id.etusername);
        passwordfield = (EditText) findViewById(R.id.etpassword);
        btsignin = (Button) findViewById(R.id.signin);
        txsignup = (Button) findViewById(R.id.btn_signup);
        txtfrgtpswrd= (Button) findViewById(R.id.btn_reset_password);

        logo=(ImageView)findViewById(R.id.logoimage);
        txtfrgtpswrd.setOnClickListener(this);
        btsignin.setOnClickListener(this);
        txsignup.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
user=firebaseAuth.getCurrentUser();




        if (firebaseAuth.getCurrentUser() != null) {

            Intent i = new Intent(getApplicationContext(), tabbed.class);

        startActivity(i);

        }


    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    private void signinmthd() {

        String email= usernamefield.getText().toString().trim();
        String password=passwordfield.getText().toString().trim();





        ProgressDi.setMessage("Signing in.Please Wait...");
        ProgressDi.setCancelable(false);
        ProgressDi.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //chechuserexists();
                    ProgressDi.dismiss();

startActivity(new Intent(getApplicationContext(),tabbed.class));




                } else if(isNetworkAvaliable(getApplicationContext())==false) {
                    ProgressDi.dismiss();
                    Toast.makeText(MainActivity.this, "Cellular Data is Turned off.Turn on data or use wifi to access data.", Toast.LENGTH_SHORT).show();

               }
               else{
                    ProgressDi.dismiss();
                    Toast.makeText(MainActivity.this,"Make sure Email or Password is Correct.",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        if( v==btsignin ){

            String email= usernamefield.getText().toString().trim();
            String password=passwordfield.getText().toString().trim();


            if(TextUtils.isEmpty(email)){
                usernamefield.setError("enter Amail Address");

                // Toast.makeText(this,"enter email eddress",Toast.LENGTH_LONG).show();
            }
            if(TextUtils.isEmpty(password)){
                //Toast.makeText(this,"enter password",Toast.LENGTH_LONG).show();

                passwordfield.setError("enter password");
                //return;
            }
else {

                signinmthd();

            }

        }
        if (v==txsignup){



            Intent i = new Intent(MainActivity.this,userinfo.class);

            startActivity(i);

        }
        if(v==txtfrgtpswrd) {
            String email = usernamefield.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                usernamefield.setError("Enter Email address.");
                //Toast.makeText(getApplicationContext(), "Enter Email Address.", Toast.LENGTH_SHORT).show();
            } else if (!isNetworkAvaliable(getApplicationContext())) {

                Toast.makeText(getApplicationContext(), "Internet connection required.", Toast.LENGTH_SHORT).show();
            }
else {


                forgotpasswordmethod();

            }
        }

        // finish();
        //startActivity(new Intent(this,MainActivity.class));
    }

    private void forgotpasswordmethod() {

        String email = usernamefield.getText().toString().trim();
        ProgressDi.setMessage("Sending Email...");
          //ProgressDi.setMessage("Sending Email...");
        ProgressDi.show();


            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   ProgressDi.cancel();
                   Toast.makeText(getApplicationContext(),"Email Sent successFully.",Toast.LENGTH_SHORT).show();
               }

               else{
                   ProgressDi.cancel();
                    usernamefield.setError("enter valid Email address.");
                 //  Toast.makeText(getApplicationContext(),"Enter valid Email Address",Toast.LENGTH_SHORT).show();
               }
                }
            });







    }
}






