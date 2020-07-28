package com.MrAndMiss.admin.mrmrspu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by ADMIN on 4/6/2017.
 */

public class about extends Activity {
    TextView txtvw4,txtvw6,txtvw9,txtvw10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abot);

    txtvw4=(TextView)findViewById(R.id.textView4);

        txtvw6=(TextView)findViewById(R.id.textView6);


        txtvw9=(TextView)findViewById(R.id.textView9);
        txtvw10=(TextView)findViewById(R.id.textView10);
    }



}


