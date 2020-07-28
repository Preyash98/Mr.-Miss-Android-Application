package com.MrAndMiss.admin.mrmrspu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ADMIN on 3/12/2017.
 */

public class alluserphoto extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView mbloglist;
    FirebaseAuth mAuth;
    FirebaseUser muser;
SearchView sv;
private DatabaseReference mdatabse,msearcgdb,mdatabaselike;
    private boolean mprocessclick=false;
    ArrayList<posts>arrayList=new ArrayList<>();
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alluserphotos, container, false);
        mbloglist = (RecyclerView) rootView.findViewById(R.id.rcview);

         mbloglist.setHasFixedSize(true);
         mAuth = FirebaseAuth.getInstance();
        muser=mAuth.getCurrentUser();
         LinearLayoutManager mManager = new LinearLayoutManager(getContext());
         sv=(SearchView)rootView.findViewById(R.id.search);
         mManager.setReverseLayout(true);
         mManager.setStackFromEnd(true);

         mbloglist.setLayoutManager(mManager);
         mdatabse=FirebaseDatabase.getInstance().getReference().child("posts").child("").child("").child("");
        mdatabaselike=FirebaseDatabase.getInstance().getReference().child("likes");

         msearcgdb=FirebaseDatabase.getInstance().getReference().child("posts");
         mdatabse.keepSynced(true);
         mdatabaselike.keepSynced(true);

         return rootView;
    }

    @Override
    public void onStart() {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onStart();




                 if(isNetworkAvaliable(getContext())==false){
                     Toast.makeText(getContext(),"Device is not connected to Internet",Toast.LENGTH_LONG).show();
                 }

        FirebaseRecyclerAdapter<posts,BlogViewHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<posts, BlogViewHolder>
                (
                        posts.class,
                        R.layout.model,
                        BlogViewHolder.class,
                        mdatabse

                )

        {


            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, posts model, int position) {
       final String post_key=getRef(position).getKey();
          viewHolder.setName(model.getName());
          viewHolder.setImage(getContext(),model.getImage());
               viewHolder.setLikers(model.getLikers());
                viewHolder.setImgbtn(post_key);

               // final String countvalues=viewHolder.likers.getText().toString();
           viewHolder.imgbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   mprocessclick=true;



                   mdatabaselike.addValueEventListener(new ValueEventListener() {


                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           //BlogViewHolder.setNumLikes(dataSnapshot.child(post_key).getChildrenCount());
                          mdatabse.child(post_key).child("likers").setValue(dataSnapshot.child(post_key).getChildrenCount());

                           if(mprocessclick){

                           if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                              // int Intvalueforlikers= Integer.parseInt(countvalues);

                               //Intvalueforlikers--;

                               mdatabaselike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                               //viewHolder.likers.setText(String.valueOf(Intvalueforlikers));
                               mprocessclick = false;

                           } else {


                               mdatabaselike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("");

                               mprocessclick = false;
                           }
                           }

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });



               }
           });


mdatabse.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String post_uid=(String)dataSnapshot.child(post_key).child("userid").getValue();
        if(mAuth.getCurrentUser().getUid().equals(post_uid)){

            viewHolder.rmvbtn.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.rmvbtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
                viewHolder.rmvbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdatabse.child(post_key).removeValue();
                        mdatabaselike.child(post_key).removeValue();
                    }
                });

            }


        };

mbloglist.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public boolean onQueryTextSubmit(String query) {



        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Query search = msearcgdb.orderByChild("name").startAt(newText).endAt("~");
        final FirebaseRecyclerAdapter<posts, BlogViewHolder> adapter = new FirebaseRecyclerAdapter<posts, BlogViewHolder>(
                posts.class, R.layout.model, BlogViewHolder.class, search )
        {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final posts model, final int position)
            {
                viewHolder.setName(model.getName());
                viewHolder.setImage(getContext(),model.getImage());
                viewHolder.setLikers(model.getLikers());
                final String post_key=getRef(position).getKey();
                viewHolder.setImgbtn(post_key);



            }
        };

        mbloglist.setAdapter(adapter);

        return false;
    }


    private static class BlogViewHolder extends RecyclerView.ViewHolder {

        private  TextView mnumlikesView;
        View mview;
        ImageButton imgbtn;
//TextView mnumlikesView;
DatabaseReference daatabaselike,mdatabse;
        FirebaseAuth mAuth;
        Button rmvbtn;
        public BlogViewHolder(View itemView) {
            super(itemView);



        mview=itemView;
            rmvbtn=(Button)mview.findViewById(R.id.alluserphotosinglebtn);
imgbtn=(ImageButton)mview.findViewById(R.id.imageButton2);
            mnumlikesView=(TextView)itemView.findViewById(R.id.likecount);
            mdatabse=FirebaseDatabase.getInstance().getReference().child("posts");
       daatabaselike=FirebaseDatabase.getInstance().getReference().child("likes");
            mAuth=FirebaseAuth.getInstance();
            daatabaselike.keepSynced(true);
        }
     void setImgbtn(final String post_key){
daatabaselike.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //BlogViewHolder.setNumLikes(dataSnapshot.child(post_key).getChildrenCount());
       // mdatabse.child(post_key).child("likers").setValue(dataSnapshot.child(post_key).getChildrenCount());
if(mAuth.getCurrentUser() !=null){
    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
        imgbtn.setImageResource(R.drawable.afterlike);


    }else{
        imgbtn.setImageResource(R.drawable.first);

    }
}



    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

     }
        public void setName(String name){
TextView usernamemodel=(TextView) mview.findViewById(R.id.usernamemodel);

            usernamemodel.setText(name);

        }

void setImage(final Context ctx, final String image){

    final ImageView post_image=(ImageView) mview.findViewById(R.id.userallphotos);

Picasso.with(ctx).load(image)
        .networkPolicy(NetworkPolicy.OFFLINE).placeholder( R.drawable.progress_animation ).into(post_image, new Callback() {
    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {
     Picasso.with(ctx).load(image).placeholder( R.drawable.progress_animation ).into(post_image);
    }
});

}
        void setLikers(long likers){
            TextView likertext=(TextView)mview.findViewById(R.id.likecount);
            String suffix = likers ==  (1 )  ? " like" : " likes";
            //mnumlikesView.setText(numLikes + suffix);
            likertext.setText(likers+suffix);
        }

    }



    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager)ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED);
    }

}