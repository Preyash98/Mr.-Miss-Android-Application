package com.MrAndMiss.admin.mrmrspu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ADMIN on 4/17/2017.
 */

public class userinfo extends Activity implements View.OnClickListener{
    EditText fstnm,lastnm,username;
    Button nxtbtn;
    TextView txtvw2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.userinfo);
    fstnm=(EditText)findViewById(R.id.firstname);
        lastnm=(EditText)findViewById(R.id.lastname);
        username=(EditText)findViewById(R.id.username);
    nxtbtn=(Button)findViewById(R.id.nexttosignup);
txtvw2= (TextView) findViewById(R.id.textView2);
    nxtbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v==nxtbtn);
       String  firstname=fstnm.getText().toString().trim();
        String lastname=lastnm.getText().toString().trim();
        String usernmae=username.getText().toString().trim();
        if(TextUtils.isEmpty(firstname)){
            fstnm.setError("Enter First-Name!");
        }
        if(TextUtils.isEmpty(lastname)){
            lastnm.setError("Enter Last-Name!");

        }
        if(TextUtils.isEmpty(usernmae)) {
            username.setError("UserName must required!");
        }
      else if(firstname.length()!=0 && lastname.length()!=0 && usernmae.length()!=0) {
            userinformationmethod();
        }
    }

    private void userinformationmethod() {
        String  firstname=fstnm.getText().toString();
        String lastname=lastnm.getText().toString().trim();
        String usernmae=username.getText().toString().trim();

        Intent intent=new Intent(getApplicationContext(),signup.class);

            intent.putExtra("firstname",firstname);
        intent.putExtra("lastname",lastname);
        intent.putExtra("username",usernmae);
    startActivity(intent);

    }
}
