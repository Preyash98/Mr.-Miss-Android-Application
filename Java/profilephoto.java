package com.MrAndMiss.admin.mrmrspu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by ADMIN on 4/26/2017.
 */

public class profilephoto extends Activity{
  RecyclerView rcview;
    FirebaseAuth mAuth;
    FirebaseUser muser;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.userallphotos);
        mAuth = FirebaseAuth.getInstance();
        muser=mAuth.getCurrentUser();

        mdatabase=  FirebaseDatabase.getInstance().getReference().child("singleuserallphoto");
        rcview=(RecyclerView)findViewById(R.id.rcview2);
        rcview.setLayoutManager(new LinearLayoutManager(this));
        rcview.setHasFixedSize(true);



    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<posts,BlogViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<posts, BlogViewHolder>(
                posts.class,
                R.layout.userallphotomodel,
                BlogViewHolder.class,
                mdatabase.child(muser.getUid()).orderByChild("Time")




        ){
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, posts model, int position) {
                viewHolder.setImage(getApplicationContext(),model.getImage());


                final String post_key=getRef(position).getKey();
                viewHolder.singlebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdatabase.child(mAuth.getCurrentUser().getUid()).child(post_key).removeValue();

                    }
                });
            }
        };


        rcview.setAdapter(firebaseRecyclerAdapter);

    }


    private static class BlogViewHolder extends RecyclerView.ViewHolder {
Button singlebtn;
        FirebaseAuth mAuth;
        DatabaseReference mdatabse;
         View mview;
        public BlogViewHolder(View itemView) {
            super(itemView);

        mview=itemView;
            mdatabse=FirebaseDatabase.getInstance().getReference().child("posts");
            mAuth=FirebaseAuth.getInstance();
singlebtn=(Button)mview.findViewById(R.id.removebtn);

        }



        void setImage(final Context ctx, final String image){

            final ImageView post_image=(ImageView) mview.findViewById(R.id.userallphotos);


            Picasso.with(ctx).load(image).into(post_image);

        }
    }
}
