package Items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wmeaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import Dashboard.Dashboard;

public class MaterialRequest extends AppCompatActivity {

    public static String fcmToken;
    Handler handler;
    ProgressDialog progressDialog;
    private static final long TIME_INTERVAL = 200000; // Time interval for double press in milliseconds
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_request);
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
        ImageView itemimage = findViewById(R.id.mr_itemImage);
        Button request = findViewById(R.id.mr_requestbutton);
        handler = new Handler(Looper.getMainLooper());
        progressDialog = new ProgressDialog(MaterialRequest.this);

        Glide.with(MaterialRequest.this)
                .load(getIntent().getStringExtra("imageurl"))
                .into(itemimage);


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
                            .child("Uploads");
                    itemRefPlastic.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                String imageurlExisting=dataSnapshot.child("ImageUrl").getValue(String.class);
                                if (imageurlExisting != null && imageurlExisting.equalsIgnoreCase(getIntent().getStringExtra("imageurl"))){
                                    itemRefPlastic.child(dataSnapshot.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                }else {
                                }



                            }

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
}
