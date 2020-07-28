package com.MrAndMiss.admin.mrmrspu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ADMIN on 3/10/2017.
 */

public class profile extends Fragment implements View.OnClickListener  {

    private Button mlogoutbtn;
    private Button uploadbutton,myalbm;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,mdatabse,userallphotos;
    private TextView usernametxt;

    private FirebaseUser currentuser;
    private StorageReference strgrf;
    private Button addphoto;
    private ImageView userphoto;
   private static final int GELLERY_INTENT = 20;
    ProgressDialog pdi;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile, container, false);
        strgrf = FirebaseStorage.getInstance().getReference();
pdi=new ProgressDialog(getContext());

        mAuth = FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        mdatabse = FirebaseDatabase.getInstance().getReference("usernames");
userallphotos=FirebaseDatabase.getInstance().getReference("singleuserallphoto");
        usernametxt = (TextView) rootView.findViewById(R.id.usernamedisp);
        uploadbutton = (Button)rootView.findViewById(R.id.uploadbtn);
        myalbm=(Button)rootView.findViewById(R.id.myalbum);
        //savebutton = (Button) rootView.findViewById(R.id.savebtn);
        FirebaseUser user = mAuth.getCurrentUser();
        mlogoutbtn = (Button) rootView.findViewById(R.id.loggedout);
        addphoto=(Button)rootView.findViewById(R.id.addphotobtn);
userphoto=(ImageView) rootView.findViewById(R.id.setphotoview);


        mlogoutbtn.setOnClickListener(this);
        addphoto.setOnClickListener(this);
        uploadbutton.setOnClickListener(this);
        myalbm.setOnClickListener(this);
        mdatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentuser.getUid())){

                    final String usernamesh= dataSnapshot.child(currentuser.getUid()).child("username").getValue().toString().trim();
                    usernametxt.setText(usernamesh);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

final String usernamefordb=usernametxt.getText().toString().trim();
        if(requestCode==GELLERY_INTENT && resultCode==RESULT_OK){
           // beginCrop(data.getData());
Uri imageUri=data.getData();
            CropImage.activity(imageUri).setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON).start(getContext(),this);

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
           // handleCrop(resultCode,data);
CropImage.ActivityResult result=CropImage.getActivityResult(data);

                     if(resultCode==RESULT_OK){

                         Uri resulturi=result.getUri();

                         Picasso.with(getContext()).load(resulturi).into(userphoto);
                     }else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
Exception error=result.getError();
Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
                     }
        }

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CropImage.ActivityResult result=CropImage.getActivityResult(data);

                Uri uri = result.getUri();
                pdi.setTitle("uploading...");
                pdi.setCancelable(false);
                pdi.setProgress(0);
                pdi.show();
                final Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final String formattedDate = df.format(c.getTime());
                StorageReference photopath = strgrf.child("photos").child(formattedDate).child(String.valueOf(c.getTimeInMillis())).child((uri.getLastPathSegment()));


                //Uri newuri= Uri.parse(uri.getLastPathSegment()+counter);

                photopath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUri = taskSnapshot.getDownloadUrl();


                        DatabaseReference newpost = databaseReference.push();
                        DatabaseReference userpost=userallphotos.child(currentuser.getUid()).push();

                        newpost.child("image").setValue(downloadUri.toString());
                        newpost.child("name").setValue(usernamefordb);
                        newpost.child("userid").setValue(currentuser.getUid());

                        Toast.makeText(getContext(),"Photo uploaded Successfully.",Toast.LENGTH_SHORT).show();
                        pdi.dismiss();
                        userpost.child("Time").setValue(String.valueOf(c.getTimeInMillis()));
                        userpost.child("username").setValue(usernamefordb);
                        userpost.child("image").setValue(downloadUri.toString());

                    }


                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        pdi.setMessage("Uploaded " + (String.valueOf(progress) ) + "%...");
                    }
                });


            }
        });



    }



    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onClick(View v) {
        if (v == mlogoutbtn) {

            mAuth.signOut();


        }
        if (v == addphoto) {


           Intent gallery_intent=new Intent();
            gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
            gallery_intent.setType("image/*");
            startActivityForResult(gallery_intent,GELLERY_INTENT);

            //userphoto.setImageDrawable(null);

            //Crop.pickImage(getContext(), this);

        }

        if(v==myalbm){

            Intent intent =new Intent(getContext(),profilephoto.class);
              intent.putExtra("userid", mAuth.getCurrentUser().getUid());
            startActivity(intent);
        }
    }














}


