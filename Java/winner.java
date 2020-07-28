package com.MrAndMiss.admin.mrmrspu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by ADMIN on 3/12/2017.
 */

public class winner extends Fragment{
       private RecyclerView myresultlist;

    FirebaseAuth mAuth;
    FirebaseUser muser;
   // Query query;
    private DatabaseReference mdatabse;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.winner, container, false);
            myresultlist = (RecyclerView) rootView.findViewById(R.id.winnerrcview);
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            myresultlist.setHasFixedSize(true);
            LinearLayoutManager mManager = new LinearLayoutManager(getContext());

            mManager.setReverseLayout(true);
            mManager.setStackFromEnd(true);

            //LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
              //layoutManager.setReverseLayout(true);


            mAuth = FirebaseAuth.getInstance();
            muser=mAuth.getCurrentUser();
            myresultlist.setLayoutManager(mManager);

            mdatabse= FirebaseDatabase.getInstance().getReference().child("posts");
//query=mdatabse.orderByChild("likers");

          //mdatabaselike=FirebaseDatabase.getInstance().getReference().child("likes");
            mdatabse.keepSynced(true);
            return rootView;
}

    @Override
    public void onStart() {

        super.onStart();

       // String url="https://mr-miss-pu.firebaseio.com/";
              final    FirebaseRecyclerAdapter<posts, WinnerBlogViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<posts, WinnerBlogViewHolder>(
                posts.class,
                R.layout.winnermodel,
                WinnerBlogViewHolder.class,
                        mdatabse.orderByChild("likers").limitToLast(5)



        ) {

                //  private List<posts> images;
                  //private Context mContext;

                   //  @Override
                    //public posts getItem(int position) {
                    //  return super.getItem(position);
                   // }


                    @Override
            protected void populateViewHolder(WinnerBlogViewHolder viewHolder, final posts model, int position) {

               // final String post_key=getRef(position).getKey();

                        viewHolder.setName(model.getName());
                viewHolder.setImage(getContext(),model.getImage());
                viewHolder.setLikers(model.getLikers());
                          position=getItemCount()-1-(position);
                           position+=1;
                            String posi=Integer.toString(position);
                        if(position==1) {
                            viewHolder.positionnumber.setText(position+"st");
                            viewHolder.cupview.setImageResource(R.drawable.golden);
                        }
                        else if(position==2){
                            viewHolder.positionnumber.setText(position+"nd");
                            viewHolder.cupview.setImageResource(R.drawable.silver);
                        }
                        else if(position==3){
                            viewHolder.positionnumber.setText(position+"rd");
                            viewHolder.cupview.setImageResource(R.drawable.brown);
                        }
                        else{
                            viewHolder.positionnumber.setText(position+"th");
                        }


                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(),pop.class);
                                intent.putExtra("image",model.getImage());
                                intent.putExtra("name",model.getName());
                                intent.putExtra("likers",String.valueOf(model.getLikers()));
                               // intent.putExtra("position",post_key);
                                startActivity(intent);
                            }
                        });

            }


                 //    @Override
                   // public WinnerBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        // return super.onCreateViewHolder(parent, viewType);
                     //   WinnerBlogViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                       // viewHolder.setOnClickListener(new WinnerBlogViewHolder.ClickListener() {
                            //posts post=new posts();
                         //   @Override
                           // public void onItemClick(View view, int position) {
                                   //Toast.makeText(getActivity(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();

                             //   Intent intent=new Intent(getContext(),pop.class);
                                //intent.putExtra("image",images.get(position).getImage());
                                //intent.putExtra("name",images.get(position).getName());
                                //intent.putExtra("likers",images.get(position).getLikers());
                               // startActivity(intent);


                            //}
                        //});
                        //return viewHolder;


                    //}
                };

        myresultlist.setAdapter(firebaseRecyclerAdapter);

    }



    public static class WinnerBlogViewHolder extends RecyclerView.ViewHolder{

        private ImageView mlikesView;
               private ImageView cupview;
private  TextView positionnumber;
    View mview;




        public WinnerBlogViewHolder(View itemView) {

        super(itemView);




        mview=itemView;
        mlikesView=(ImageView)itemView.findViewById(R.id.winnerlikeview);
 positionnumber=(TextView)itemView.findViewById(R.id.positionno);
cupview=(ImageView)itemView.findViewById(R.id.cup);






    }





        public void setName(String name){
        TextView usernamemodel=(TextView) mview.findViewById(R.id.modelusername);

        usernamemodel.setText(name);

    }

    public void setImage(final Context ctx, final String image){

        final ImageView post_image=(ImageView) mview.findViewById(R.id.winneruserphoto);


        Picasso.with(ctx).load(image).into(post_image);

    }
        public void setLikers(long likers){
            TextView winnerlikes=(TextView)mview.findViewById(R.id.modeluserlikes);

             winnerlikes.setText(String.valueOf(likers));
        }


       // private WinnerBlogViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        //public interface ClickListener{
          //  public void onItemClick(View view, int position);

        //}

        //public void setOnClickListener(WinnerBlogViewHolder.ClickListener clickListener){
          //  mClickListener = clickListener;
        //}

    }






}