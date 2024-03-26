package Service;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import Dashboard.Dashboard;
import UserProfile.Userprofile;

public class Redundant {
//    data za kucheki mtandao

//     if (isNetworkConnected()) {
//                    new InternetCheckTask().execute();
//                } else {
//                    Intent intent = new Intent(Dashboard.this, Userprofile.class);
//                    intent.putExtra("name", "Connect to internet");
//                    intent.putExtra("pNumber", "#############");
//                    intent.putExtra("email", "email");
//                    startActivity(intent);
//                    finish();
//                }

//    private boolean isNetworkConnected() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//        return activeNetwork != null && activeNetwork.isConnected();
//    }
//
//    private class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            try {
//                HttpURLConnection urlConnection = (HttpURLConnection) (new URL("https://www.google.com")).openConnection();
//                urlConnection.setRequestProperty("User-Agent", "Android");
//                urlConnection.setRequestProperty("Connection", "close");
//                urlConnection.setConnectTimeout(1000);
//                urlConnection.connect();
//                return (urlConnection.getResponseCode() == 200);
//            } catch (IOException e) {
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean isConnected) {
//            super.onPostExecute(isConnected);
//            if (isConnected) {
//                openUserProfile();
//            } else {
//                Toast.makeText(Dashboard.this, "No active internet connection", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        private void openUserProfile() {
//            DatabaseReference userdbRef = FirebaseDatabase.getInstance().getReference()
//                    .child("All Users")
//                    .child(FirebaseAuth.getInstance().getUid())
//                    .child("Details");
//
//            userdbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    // Retrieve user details directly from the snapshot
//                    String fullName = snapshot.child("Fullname").getValue(String.class);
//                    String phoneNumber = snapshot.child("PhoneNumber").getValue(String.class);
//                    String email = snapshot.child("username").getValue(String.class);
//
//                    Intent intent = new Intent(Dashboard.this, Userprofile.class);
//                    intent.putExtra("name", fullName);
//                    intent.putExtra("pNumber", phoneNumber);
//                    intent.putExtra("email", email);
//                    startActivity(intent);
//                    finish();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle error
//                }
//            });
//        }
//    }
}
