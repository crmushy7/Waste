package Items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmeaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapters.ImagePagerAdapter;
import Dashboard.Dashboard;
import Notification.Api;
import Notification.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MaterialRequest extends AppCompatActivity {

    public static String fcmToken;
    Handler handler;
    ProgressDialog progressDialog;
    private static final long TIME_INTERVAL = 200000; // Time interval for double press in milliseconds
    private long mBackPressed;
    private ViewPager viewPager;
    public static final String CHANNEL_ID = "simplified_coding";
    private static final String CHANNEL_NAME = "Simplified Coding";
    private static final String CHANNEL_DESC = "Simplified Coding Notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_request);
        final User user = (User) getIntent().getSerializableExtra("user");
        handler = new Handler(Looper.getMainLooper());
        progressDialog = new ProgressDialog(MaterialRequest.this);

        handler.post(() -> {
            progressDialog = new ProgressDialog(MaterialRequest.this);
            progressDialog.setMessage("Please wait.....Make sure you have a stable internet connection!");
            progressDialog.setCancelable(false);
        });


// Retrieve the DatabaseReference for the specific item
        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference()
                .child("Uploads").child(getIntent().getStringExtra("itemID")).child("ImageUrls");

// Add a ValueEventListener to fetch the image URLs
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> imageUrls = new ArrayList<>();
                // Iterate through the child nodes to get each image URL
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String imageUrl = dataSnapshot.child("Image").getValue(String.class);
                    imageUrls.add(imageUrl);
                }
                // Now you have the list of image URLs, you can use it to initialize your ViewPager
                viewPager = findViewById(R.id.viewPager);
                ImagePagerAdapter adapter = new ImagePagerAdapter(MaterialRequest.this, imageUrls,viewPager);
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });



        ImageView backNavbtn = findViewById(R.id.mr_back_navigation);
        TextView itemname = findViewById(R.id.mr_itemName);
        TextView itemname2 = findViewById(R.id.mr_itemName2);
        TextView itemowner = findViewById(R.id.mr_itemOwner);
        TextView ownernumber = findViewById(R.id.mr_ownerNumber);
        TextView itemunit = findViewById(R.id.mr_itemUnit);
        TextView itemtype = findViewById(R.id.mr_itemType);
        TextView itemuploaddate = findViewById(R.id.mr_itemUploaddate);
        TextView itemlocation = findViewById(R.id.mr_itemLocation);
        TextView user_name = findViewById(R.id.mr_displayname);
//        ImageView itemimage = findViewById(R.id.mr_itemImage);
        Button request = findViewById(R.id.mr_requestbutton);


//        Glide.with(MaterialRequest.this)
//                .load(getIntent().getStringExtra("imageurl"))
//                .into(itemimage);


        String ownerId = getIntent().getStringExtra("ownerID");
        itemname.setText(getIntent().getStringExtra("itemtitle1") + "");
        itemname2.setText(getIntent().getStringExtra("itemtitle1") + "");
        itemowner.setText(getIntent().getStringExtra("username") + "");
        user_name.setText(getIntent().getStringExtra("username") + "");
        ownernumber.setText(getIntent().getStringExtra("userpNumber") + "");
        itemunit.setText(getIntent().getStringExtra("materialunit") + "");
        itemuploaddate.setText(getIntent().getStringExtra("uploaddate") + "");
        itemtype.setText(getIntent().getStringExtra("itemtype") + "");
        itemlocation.setText(getIntent().getStringExtra("location") + "");

        backNavbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        if (ownerId.equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
            request.setText("Delete");

            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.post(() -> {
                        progressDialog = new ProgressDialog(MaterialRequest.this);
                        progressDialog.setMessage("Deleting, Please wait.....Make sure you have a stable internet connection!");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    });
                    DatabaseReference itemRefPlastic= FirebaseDatabase.getInstance().getReference()
                            .child("Uploads").child(getIntent().getStringExtra("itemID"));
                    itemRefPlastic.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    itemRefPlastic.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MaterialRequest.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MaterialRequest.this,Dashboard.class));
                                                }
                                            },5000);
                                        }
                                    });




                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }else{
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    DatabaseReference userr=FirebaseDatabase.getInstance().getReference().child("All Users")
                                    .child(getIntent().getStringExtra("ownerID")).child("Details");
                    userr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String token=snapshot.child("FCM Token").getValue(String.class);
                            User user1=new User("any",token+"");
                            sendNotification(user1,token);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
        }
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
    private  void sendNotification(User user, String token){
        String title = "WMEA App";
        String body = "Your uploaded material was requested!";

        if(title.isEmpty()){

            return;
        }

        if(body.isEmpty()){

            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dcts.staffgenie.co.tz/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.sendNotification(token, title, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        progressDialog.dismiss();
                        Toast.makeText(MaterialRequest.this, "The owner was notified!", Toast.LENGTH_LONG).show();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("What",response+"");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MaterialRequest.this, "Owner not notified! try again later", Toast.LENGTH_LONG).show();
                Log.d("error",t+"");
            }
        });

    }
}
