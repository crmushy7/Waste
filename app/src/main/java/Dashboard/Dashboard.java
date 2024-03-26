package Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmeaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import Adapters.ItemsAdapter;
import Items.UploadPage;
import Items.ViewPage;
import UserProfile.UserDetails;
import UserProfile.Userprofile;

public class Dashboard extends AppCompatActivity {

    private static final long TIME_INTERVAL = 2000; // Time interval for double press in milliseconds
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        UserDetails.init(getApplicationContext());

        LinearLayout profileBtn = findViewById(R.id.ll_profileBtn);
        RelativeLayout uploadBtn = findViewById(R.id.rl_uploadButton);
        LinearLayout viewPagePlastic = findViewById(R.id.ll_plasticBtn);
        LinearLayout viewPagemetal = findViewById(R.id.ll_metalBtn);
        LinearLayout viewPagewood = findViewById(R.id.ll_woodBtn);
        TextView displayUsername = findViewById(R.id.displayUserName);


        String fullName = UserDetails.getFullName();
        if (fullName==null){
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        }else{
            String[] parts = fullName.split(" ");
            displayUsername.setText(parts[0]+"");
        }


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = UserDetails.getFullName();
                String phoneNumber = UserDetails.getPhoneNumber();
                String email = UserDetails.getEmail();

                Intent intent = new Intent(Dashboard.this, UploadPage.class);
                intent.putExtra("name", fullName);
                intent.putExtra("pNumber", phoneNumber);
                intent.putExtra("email", email);
                intent.putExtra("backTo", "dashboard");
                startActivity(intent);
                finish();

            }
        });

        viewPagePlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ViewPage.class);
                intent.putExtra("Material type", "plastic");
                startActivity(intent);
                finish();
            }
        });
        viewPagemetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ViewPage.class);
                intent.putExtra("Material type", "metal");
                startActivity(intent);
                finish();
            }
        });
        viewPagewood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ViewPage.class);
                intent.putExtra("Material type", "wood");
                startActivity(intent);
                finish();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = UserDetails.getFullName();
                String phoneNumber = UserDetails.getPhoneNumber();
                String email = UserDetails.getEmail();

                Intent intent = new Intent(Dashboard.this, Userprofile.class);
                intent.putExtra("name", fullName);
                intent.putExtra("pNumber", phoneNumber);
                intent.putExtra("email", email);
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


}