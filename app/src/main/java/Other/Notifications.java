package Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.wmeaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapters.NotificationAdapter;
import Adapters.NotificationSetGet;

public class Notifications extends AppCompatActivity {

    private List<NotificationSetGet> notificationlist=new ArrayList<>();
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        recyclerView=findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
        adapter=new NotificationAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
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
                        NotificationSetGet notificationSetGet=new NotificationSetGet(title,message,status,time);
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
    }
}