package com.MrAndMiss.admin.mrmrspu;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ADMIN on 4/14/2017.
 */

public class pop extends Activity {
ImageView imgview;
    TextView txtview,txtvw14;
   // private DatabaseReference mdatabase;
    private  String mpost_key=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poupwondow);
//mdatabase= FirebaseDatabase.getInstance().getReference().child("posts");
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.5));

        mpost_key=getIntent().getExtras().getString("image");
        imgview=(ImageView)findViewById(R.id.imageView);
        txtview=(TextView)findViewById(R.id.textView13);
        txtvw14=(TextView)findViewById(R.id.textView14);


Picasso.with(getApplicationContext()).load(mpost_key).placeholder(R.drawable.progress_animation).into(imgview);
       txtvw14.setText(getIntent().getStringExtra("name"));
      txtview.setText(getIntent().getStringExtra("likers")  +  "likes");


    }
}
