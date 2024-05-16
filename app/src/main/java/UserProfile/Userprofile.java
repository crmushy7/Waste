package UserProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crmushi.wmeaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapters.ItemSetGet;
import Authentication.RegistrationMod;
import Dashboard.Dashboard;
import Items.UploadPage;
import Other.AboutUs;
import Other.FAQ;
import Other.Notifications;

public class Userprofile extends AppCompatActivity {

    private static final long TIME_INTERVAL = 2000; // Time interval for double press in milliseconds
    private long mBackPressed;
    private  AlertDialog dialog;
    Handler handler;
    ProgressDialog progressDialog;
    public static TextView userName,userphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        RelativeLayout uploadBtn=findViewById(R.id.rl_uploadButton);
        LinearLayout homeBtn=findViewById(R.id.up_home_navigation);
        LinearLayout logOutbtn=findViewById(R.id.up_logoutbtn);
        LinearLayout update_name=findViewById(R.id.userp_updatename);
        LinearLayout update_password=findViewById(R.id.userp_updatepassword);
        LinearLayout update_phone=findViewById(R.id.userp_updatephonenumber);
        LinearLayout aboutUs=findViewById(R.id.up_aboutUs);
        LinearLayout faq=findViewById(R.id.up_faq);
        LinearLayout notifications=findViewById(R.id.up_notifications);
        userName=findViewById(R.id.up_username);
        TextView useremail=findViewById(R.id.up_userEmail);
        userphone=findViewById(R.id.up_userPhone);
        handler=new Handler(Looper.getMainLooper());

        handler.post(() -> {
            progressDialog = new ProgressDialog(Userprofile.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
        });
       userName.setText(getIntent().getStringExtra("name")+"");
       useremail.setText(getIntent().getStringExtra("email")+"");
       userphone.setText(getIntent().getStringExtra("pNumber")+"");

       faq.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Userprofile.this, FAQ.class));
           }
       });
       notifications.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Userprofile.this, Notifications.class));
           }
       });
       aboutUs.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Userprofile.this, AboutUs.class));
           }
       });

       update_name.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updateUser("Fullname");
           }
       });
       update_phone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updateUser("PhoneNumber");
           }
       });
       update_password.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updateUser("Password");
           }
       });
        logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Userprofile.this);
                builder.setTitle("Loading")
                        .setMessage("You are about to log out,are you sure?")
                        .setCancelable(false)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent=new Intent(Userprofile.this, RegistrationMod.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                builder.show();
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Userprofile.this, Dashboard.class));
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = UserDetails.getFullName();
                String phoneNumber = UserDetails.getPhoneNumber();
                String email = UserDetails.getEmail();

                Intent intent = new Intent(Userprofile.this, UploadPage.class);
                intent.putExtra("name", fullName);
                intent.putExtra("pNumber", phoneNumber);
                intent.putExtra("email", email);
                intent.putExtra("backTo", "userprofile");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            mBackPressed = System.currentTimeMillis();
        }
        ;

    }
    private void updateUser(String updateType){

        AlertDialog.Builder builder = new AlertDialog.Builder(Userprofile.this);
        View popupView = LayoutInflater.from(Userprofile.this).inflate(R.layout.profile_update, null);

        EditText name_et = popupView.findViewById(R.id.profile_nameet);
        EditText number_et = popupView.findViewById(R.id.profile_phoneet);
        EditText password_et = popupView.findViewById(R.id.profile_passwordet);
        TextView nametv = popupView.findViewById(R.id.profile_nametv);
        TextView numbertv = popupView.findViewById(R.id.profile_phonetv);
        TextView passwordtv = popupView.findViewById(R.id.profile_passwordtv);
        Button proceedtoUpdate = popupView.findViewById(R.id.profile_updateButton);
        builder.setView(popupView);
        dialog = builder.create();
        dialog.show();
        if (updateType=="Fullname"){
            numbertv.setVisibility(View.GONE);
            number_et.setVisibility(View.GONE);
            passwordtv.setVisibility(View.GONE);
            password_et.setVisibility(View.GONE);
        } else if (updateType=="Password") {
            numbertv.setVisibility(View.GONE);
            number_et.setVisibility(View.GONE);
            nametv.setVisibility(View.GONE);
            name_et.setVisibility(View.GONE);
        } else if (updateType=="PhoneNumber") {
            nametv.setVisibility(View.GONE);
            name_et.setVisibility(View.GONE);
            passwordtv.setVisibility(View.GONE);
            password_et.setVisibility(View.GONE);

        }
        proceedtoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (updateType=="Fullname"){
                    String new_userName=name_et.getText().toString();
                    if (new_userName.isEmpty()){
                        progressDialog.dismiss();
                        name_et.setError("Fill this!");
                    }else{
                        updateToFirebase(new_userName,updateType);
                    }
                } else if (updateType=="Password") {
                    String new_password=password_et.getText().toString();
                    if (new_password.isEmpty()){
                        progressDialog.dismiss();
                        password_et.setError("Fill this!");
                    } else if (new_password.length()<=5) {
                        progressDialog.dismiss();
                        password_et.setError("password too short(must be atleast 6 characters)!");

                    } else{
                        updateToFirebase(new_password,updateType);
                    }
                } else if (updateType=="PhoneNumber") {
                    String new_phonenumber=number_et.getText().toString();
                    if (new_phonenumber.isEmpty()){
                        progressDialog.dismiss();
                        number_et.setError("Fill this!");
                    } else if (new_phonenumber.length()<10) {
                        number_et.setError("Numbers must be 10");
                        progressDialog.dismiss();
                    } else{
                        updateToFirebase(new_phonenumber,updateType);
                    }
                }
            }
        });
    }
    private void updateToFirebase(String newData,String updateChild){
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference().child("All Users")
                .child(FirebaseAuth.getInstance().getUid().toString())
                .child("Details");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    userRef.child(updateChild).setValue(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (updateChild=="Password"){


                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    AuthCredential credential = EmailAuthProvider.getCredential(UserDetails.getEmail(), UserDetails.getPassword());
                                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                if (user !=null) {
                                                    user.updatePassword(newData + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(Userprofile.this, "Updated successfully", Toast.LENGTH_LONG).show();
                                                                SharedPreferences sharedPreferences=getSharedPreferences("User_data",MODE_PRIVATE);
                                                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                                                editor.putString("password",newData+"");
                                                                editor.apply();
                                                                UserDetails.init(getApplicationContext());
                                                                refresh();
                                                                dialog.dismiss();
                                                                progressDialog.dismiss();
                                                            }else{
                                                                progressDialog.dismiss();
                                                                Exception exception = task.getException();
                                                                Log.d("TAG", "Error updating password"+exception);
                                                            }
                                                        }
                                                    });
                                                }else {
                                                    Toast.makeText(Userprofile.this, "Try again", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(Userprofile.this, "Password wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else if(updateChild=="Fullname"){
                                    updateUserItems("Fullname",newData);
                                    Toast.makeText(Userprofile.this, "Updated successfully", Toast.LENGTH_LONG).show();
                                    SharedPreferences sharedPreferences=getSharedPreferences("User_data",MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    editor.putString("full_name",newData+"");
                                    editor.apply();
                                    UserDetails.init(getApplicationContext());
                                    refresh();
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                }else {
                                    DatabaseReference itemRefPlastic= FirebaseDatabase.getInstance().getReference()
                                            .child("Uploads");
                                    itemRefPlastic.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                                                if (owner_ID != null && owner_ID.equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
                                                    itemRefPlastic.child(dataSnapshot.getKey()).child("Owner PhoneNumber").setValue(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                        }
                                                    });
                                                }else {
                                                }



                                            }

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(Userprofile.this, "Updated successfully", Toast.LENGTH_LONG).show();
                                    SharedPreferences sharedPreferences=getSharedPreferences("User_data",MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    editor.putString("phone_number",newData+"");
                                    editor.apply();
                                    UserDetails.init(getApplicationContext());
                                    refresh();
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Userprofile.this, "Update failed,please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(Userprofile.this, "Database error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateUserItems(String childName ,String newData){
        DatabaseReference itemRefPlastic= FirebaseDatabase.getInstance().getReference()
                .child("Uploads");
        itemRefPlastic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                    if (owner_ID != null && owner_ID.equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
                        itemRefPlastic.child(dataSnapshot.getKey()).child("Owner Name").setValue(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                    }else {
                    }



                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void refresh(){
        userName=findViewById(R.id.up_username);
        userphone=findViewById(R.id.up_userPhone);
        userName.setText(UserDetails.getFullName());
        userphone.setText(UserDetails.getPhoneNumber());
    }

}