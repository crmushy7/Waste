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
    private static final long TIME_INTERVAL = 2000; // Time interval for double press in milliseconds
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

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(() -> {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
//                    progressDialog.show();
                });

                // Get the item owner's user ID
                String ownerId = getIntent().getStringExtra("ownerID");

                // Retrieve the FCM token of the item owner from Firebase Database
                DatabaseReference ownerRef = FirebaseDatabase.getInstance().getReference().child("All Users").child(ownerId).child("Details").child("FCM Token");
                ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String ownerFCMToken = snapshot.getValue(String.class);
                            // Prepare the notification message
//                            Toast.makeText(MaterialRequest.this, ownerFCMToken+"", Toast.LENGTH_SHORT).show();
                            String notificationMessage = "You have a new request for the item.";

                            // Send the notification to the owner
                            sendNotificationToUser(ownerFCMToken, notificationMessage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                    }
                });
            }

            private void sendNotificationToUser(String ownerFCMToken, String notificationMessage) {
//                AsyncTask.execute(() -> {
//                    try {
//                        URL url = new URL("https://fcm.googleapis.com/v1/projects/wmea-app-4917c/messages:send");
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestProperty("Authorization", "key=AAAAVB2OlrU:APA91bH6x8CXoGs6VcqpwRvsxbOFgcucfAl6EwDdhwB7F5Iy27IngoNGOHdGIAb0KwHQT1GefgR7tpDI2o0FyQHFOoXHSK3BheRp2bkOOilAGwrMu87mGn1g4rMNgrXwiievZbcJfJJa");
//                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                        conn.setRequestMethod("POST");
//                        conn.setDoOutput(true);
//
//                        JSONObject jsonRequest = new JSONObject();
//                        jsonRequest.put("notification", new JSONObject()
//                                .put("title", "New Request")
//                                .put("body", notificationMessage));
//                        jsonRequest.put("to", ownerFCMToken);
//
//                        OutputStream outputStream = conn.getOutputStream();
//                        outputStream.write(jsonRequest.toString().getBytes("UTF-8"));
//                        outputStream.close();
//
//                        int responseCode = conn.getResponseCode();
//                        Log.d("FCM_DEBUG", "Response Code: " + responseCode);
//                        if (responseCode == HttpURLConnection.HTTP_OK) {
//                            InputStream inputStream = conn.getInputStream();
//                            // Process the response if needed
//                        } else {
//                            InputStream errorStream = conn.getErrorStream();
//                            Log.e("FCM_DEBUG", "Error Stream: " + errorStream);
//                            // Handle the error
//                        }
//
//                        conn.disconnect();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("FCM_DEBUG", "Exception: " + e.getMessage());
//                    }
//                });
                String targetUserToken = ownerFCMToken+"";

                // Create an FCM message and send it to the target user
                // Use the Firebase Admin SDK or FCM API here
                // Example FCM message creation and sending logic
                HashMap<String,String> map=new HashMap<>();
                map.put("title","Notification Title");
                map.put("body","Notification body");
                FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(targetUserToken)
                        .setMessageId("1")
                        .setData(map)
                        .build());
                Log.d("FCM_DEBUG", "Response Code: " + ownerFCMToken);
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
