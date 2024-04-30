package Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmeaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import Adapters.ImagePagerAdapter;
import Adapters.NotificationAdapter;
import Adapters.NotificationSetGet;
import Dashboard.Dashboard;
import Items.MaterialRequest;
import Notification.Api;
import Notification.User;
import UserProfile.UserDetails;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Notifications extends AppCompatActivity implements NotificationAdapter.OnItemLongClickListener{

    private List<NotificationSetGet> notificationlist=new ArrayList<>();
    RecyclerView recyclerView;
    public static String itemSoldStatus="";
    NotificationAdapter adapter;
    private ViewPager viewPager;
    AlertDialog dialog;
    Handler handler;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        recyclerView=findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
        adapter=new NotificationAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemLongClickListener(Notifications.this);
        handler = new Handler(Looper.getMainLooper());
        progressDialog = new ProgressDialog(Notifications.this);
        handler.post(() -> {
            progressDialog = new ProgressDialog(Notifications.this);
            progressDialog.setMessage("Please wait.....Make sure you have a stable internet connection!");
            progressDialog.setCancelable(false);
        });
        adapter.setOnItemLongClickListener(new NotificationAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, NotificationSetGet itemSetGet) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Notifications.this);
                View viewChat = LayoutInflater.from(Notifications.this).inflate(R.layout.delete_notification, null);
                Button cancelbtn = viewChat.findViewById(R.id.cancelNotification);
                Button deletebtn = viewChat.findViewById(R.id.deleteNotification);
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        dialog.dismiss();
                        DatabaseReference deleteNotification= FirebaseDatabase.getInstance().getReference().child("Received Notifications")
                                .child(FirebaseAuth.getInstance().getUid().toString()).child(itemSetGet.getNotificationID());
                        deleteNotification.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    deleteNotification.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Notifications.this, "deleted successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Notifications.this, "Notification already deleted", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Notifications.this, "Try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setView(viewChat);
                dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);

            }
        });


        DatabaseReference uploadNotification= FirebaseDatabase.getInstance().getReference().child("Received Notifications")
                .child(FirebaseAuth.getInstance().getUid().toString());
        uploadNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    notificationlist.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String title=dataSnapshot.child("Notification Title").getValue(String.class);
                        String message=dataSnapshot.child("Notification Message").getValue(String.class);
                        String time=dataSnapshot.child("Notification Sent Time").getValue(String.class);
                        String status=dataSnapshot.child("Notification Status").getValue(String.class);
                        String iemInNotID=dataSnapshot.child("ItemID").getValue(String.class);
                        String matName=dataSnapshot.child("Item Name").getValue(String.class);
                        String matType=dataSnapshot.child("Item Type").getValue(String.class);
                        String collecName=dataSnapshot.child("Collector").getValue(String.class);
                        String collecNo=dataSnapshot.child("Contact").getValue(String.class);
                        String itemRequestStatus=dataSnapshot.child("Request Status").getValue(String.class);
                        String collector_id=dataSnapshot.child("CollectorID").getValue(String.class);
                        String fcmt=dataSnapshot.child("Collector FCM Token").getValue(String.class);
                        String notID=dataSnapshot.getKey().toString();

                        NotificationSetGet notificationSetGet=new NotificationSetGet(title,message,status,time,collector_id,collecNo,fcmt,matType,matName,notID,iemInNotID,collecName,itemRequestStatus);
                        notificationlist.add(notificationSetGet);
                    }
                    adapter.updateData(notificationlist);
                    Collections.reverse(notificationlist);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(Notifications.this, "No notification!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Notifications.this, "failed due to "+error+"", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, NotificationSetGet itemSetGet) {

                if (itemSetGet.getItemrequestStatus().equals("Not sold") && itemSetGet.getNotificationStatus().equals("Unread")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Notifications.this);
                    View viewChat = LayoutInflater.from(Notifications.this).inflate(R.layout.confirm_notification, null);
                    Button decline = viewChat.findViewById(R.id.declinebtn);
                    Button accept = viewChat.findViewById(R.id.acceptbtn);
                    TextView materialType = viewChat.findViewById(R.id.nt_itemType);
                    TextView materialName1 = viewChat.findViewById(R.id.nt_itemname);
                    TextView materialName2 = viewChat.findViewById(R.id.nt_itemName2);
                    TextView materialCollector = viewChat.findViewById(R.id.nt_itemCollector);
                    TextView collectorPhone = viewChat.findViewById(R.id.collectorNumber);
                    TextView cancelbtn = viewChat.findViewById(R.id.cancelDialog);
                    materialCollector.setText(itemSetGet.getCollectorName());
                    materialName1.setText(itemSetGet.getItemName());
                    materialName2.setText(itemSetGet.getItemName());
                    materialType.setText(itemSetGet.getItemType());
                    collectorPhone.setText(itemSetGet.getCollectorPhone() + "");
                    String PHONE_NUMBER = collectorPhone.getText().toString();

                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    // Retrieve the DatabaseReference for the specific item pics
                    DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference()
                            .child("Uploads").child(itemSetGet.getItemID()).child("ImageUrls");

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
                            viewPager = viewChat.findViewById(R.id.viewPager);
                            ImagePagerAdapter adapter1 = new ImagePagerAdapter(Notifications.this, imageUrls, viewPager);
                            viewPager.setAdapter(adapter1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle errors here
                        }
                    });


                    collectorPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
                            startActivity(intent);
                        }
                    });
                    decline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            sendNotification(itemSetGet, "Declined");
                        }
                    });
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            progressDialog.show();
                            sendNotification(itemSetGet, "Accepted");
                        }
                    });

                    builder.setView(viewChat);
                    dialog = builder.create();
                    dialog.show();
                    dialog.setCancelable(false);
                }else{
                    if (itemSetGet.getNotificationStatus().equals("Accepted")) {
                        Toast.makeText(Notifications.this, "Material already sold! hold to delete this notification", Toast.LENGTH_SHORT).show();
                    } else if (!itemSetGet.getCollectorID().equals(FirebaseAuth.getInstance().getUid().toString())&& itemSetGet.getNotificationStatus().equals("Declined")) {
                        Toast.makeText(Notifications.this, "you declined to sell this to "+itemSetGet.getCollectorName()+"! hold to delete!", Toast.LENGTH_LONG).show();

                    } else if (!itemSetGet.getCollectorID().equals(FirebaseAuth.getInstance().getUid().toString())&& itemSetGet.getNotificationStatus().equals("Accepted")) {
                        Toast.makeText(Notifications.this, "Material already sold! hold to delete this notification", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(Notifications.this, "your previous request was declined! consider requesting again! or hold to delete this notification", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private  void sendNotification(NotificationSetGet itemSetGet,String received){
        String fullname=itemSetGet.getCollectorName();
        String[] name=fullname.split(" ");
        String title ="From: "+ UserDetails.getFullName();
        String body = name[0]+", Your request of material("+itemSetGet.getItemName()+") was "+received+"!";

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

        Call<ResponseBody> call = api.sendNotification(itemSetGet.getCollectorFCM(), title, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        String currentdate = DateFormat.getInstance().format(calendar.getTime());
                        DatabaseReference uploadNotification=FirebaseDatabase.getInstance().getReference().child("Received Notifications")
                                .child(itemSetGet.getCollectorID()).push();
                        uploadNotification.child("Notification Title").setValue(title);
                        uploadNotification.child("Notification Message").setValue(body);
                        uploadNotification.child("Notification Status").setValue(received);
                        uploadNotification.child("Notification Sent Time").setValue(currentdate+" Hrs").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (received.equals("Accepted")){
                                    DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference()
                                            .child("Uploads").child(itemSetGet.getItemID());
                                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            itemRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    DatabaseReference modifyNotification= FirebaseDatabase.getInstance().getReference().child("Received Notifications")
                                                            .child(FirebaseAuth.getInstance().getUid().toString()).child(itemSetGet.getNotificationID());
                                                    modifyNotification.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()){
                                                                modifyNotification.child("Request Status").setValue("Sold");
                                                                modifyNotification.child("Notification Status").setValue(received);
                                                            }else{
                                                                progressDialog.dismiss();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            progressDialog.dismiss();
                                        }
                                    });
                                }else{
                                    DatabaseReference modifyNotification= FirebaseDatabase.getInstance().getReference().child("Received Notifications")
                                            .child(FirebaseAuth.getInstance().getUid().toString()).child(itemSetGet.getNotificationID());
                                    modifyNotification.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                modifyNotification.child("Notification Status").setValue(received);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(Notifications.this, "Notification sent!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    progressDialog.dismiss();
                    Log.d("What",response+"");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Notifications.this, "Owner not notified! try again later "+t+"", Toast.LENGTH_LONG).show();
                Log.d("error",t+"");
            }
        });

    }
    @Override
    public void onItemLongClick(int position, NotificationSetGet itemSetGet) {
        // Handle long click events here
        Toast.makeText(this, "Item long clicked at position " + position, Toast.LENGTH_SHORT).show();

        // Perform navigation or any other action based on long click
    }
}