package Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmeaapp.ChatBotActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.crmushi.wmeaapp.R;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapters.ItemsAdapter;
import Adapters.Material;
import Items.UploadPage;
import Items.ViewPage;
import Other.Notifications;
import Service.AdController;
import UserProfile.UserDetails;
import UserProfile.Userprofile;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback {

    private static final long TIME_INTERVAL = 200000; // Time interval for double press in milliseconds
    private long mBackPressed;
    ImageView notificationicon;
    AlertDialog dialog;
    private GoogleMap gMap;
    public static Context context;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Material> materialList = new ArrayList<>();
    private LocationManager locationManager;
    TextView locationText;
    public static String LOCATION="";


    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        UserDetails.init(getApplicationContext());
        context=Dashboard.this;
        AdController.initAd(getApplicationContext());
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());


        locationText=findViewById(R.id.displayRegion);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }

        container = findViewById(R.id.banner_layout);
        AdController.largeBannerAd(Dashboard.this,container);

        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapID);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        materialList = new ArrayList<>();


        LinearLayout profileBtn = findViewById(R.id.ll_profileBtn);
        LinearLayout chatbot = findViewById(R.id.ll_chatbot);
        RelativeLayout uploadBtn = findViewById(R.id.rl_uploadButton);
        LinearLayout viewPagePlastic = findViewById(R.id.ll_plasticBtn);
        LinearLayout viewPagemetal = findViewById(R.id.ll_metalBtn);
        LinearLayout viewPagewood = findViewById(R.id.ll_woodBtn);
        TextView displayUsername = findViewById(R.id.displayUserName);
        notificationicon=findViewById(R.id.notificationIcon);
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, ChatBotActivity.class));
            }
        });
        notificationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Notifications.class));
            }
        });
        AlertDialog.Builder builderchatboat=new AlertDialog.Builder(Dashboard.this);
        View viewChat= LayoutInflater.from(Dashboard.this).inflate(R.layout.chatbot_alert, null);
        Button later=viewChat.findViewById(R.id.chatbot_later);
        Button viewChatbot=viewChat.findViewById(R.id.chatbot_view);
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        viewChatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(Dashboard.this, ChatBotActivity.class));
            }
        });

        builderchatboat.setView(viewChat);
        dialog=builderchatboat.create();
        dialog.show();
        dialog.setCancelable(false);


        if (!areNotificationsEnabled()) {
            // Notifications are not enabled, show a dialog to the user
            showNotificationEnableDialog();
        }
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
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @SuppressLint("NewApi")
    private boolean areNotificationsEnabled() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            return notificationManager.areNotificationsEnabled();
        }
        return false;
    }

    private void showNotificationEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);

        titleTextView.setText("Notifications Disabled");
        messageTextView.setText("Notifications are disabled for this app. Enable notifications to receive updates.");

        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(view -> {
            // Open notification settings
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(view -> {
            // Cancel button clicked
            Toast.makeText(this, "Notifications are disabled. You may miss important updates.", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });

        dialog.show();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                LocationRequest locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                // Create a location callback
                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (locationResult != null) {
                            Location currentLocation = locationResult.getLastLocation();
                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            map.addMarker(new MarkerOptions().position(latLng).title("My Location"));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            DatabaseReference databaseReferenceUpld = FirebaseDatabase.getInstance().getReference().child("Uploads");

                            databaseReferenceUpld.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String matLocation = dataSnapshot.child("Material Location").getValue(String.class);
                                            String materialName=dataSnapshot.child("Material Description").getValue(String.class);
                                            String ownerPhone=dataSnapshot.child("Owner PhoneNumber").getValue(String.class);

                                            String[] locBoth=matLocation.split(",");
                                            double latitude=Double.parseDouble(locBoth[0]);
                                            double longitude=Double.parseDouble(locBoth[1]);
                                            Material material=new Material(materialName,latitude,longitude);
                                            materialList.add(material);
                                            LatLng latLng1 = new LatLng(latitude, longitude);
                                            map.addMarker(new MarkerOptions()
                                                    .position(latLng1)
                                                    .title(materialName)
                                                    .snippet("Owner Phone: " + ownerPhone)
                                            );
                                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                                        }

                                        Log.d("Material",materialList+"");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                        }
                    }
                };

                // Request location updates
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                // Handle the case where no last known location is available
                Toast.makeText(this, "Retrieving current location...", Toast.LENGTH_SHORT).show();
//            }

        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }


    private void fetchMaterialsAndMarkOnMap() {
        DatabaseReference databaseReferenceUpld = FirebaseDatabase.getInstance().getReference().child("Uploads");

        databaseReferenceUpld.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String matLocation = dataSnapshot.child("Material Location").getValue(String.class);
                        String materialName=dataSnapshot.child("Material Description").getValue(String.class);
                        Log.d("Location", matLocation);
                        String[] locBoth=matLocation.split(",");
                        double latitude=Double.parseDouble(locBoth[0]);
                        double longitude=Double.parseDouble(locBoth[1]);
                        Material material=new Material(materialName,latitude,longitude);
                        materialList.add(material);
                    }

                    Log.d("Material",materialList+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void getLocation() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(@NonNull Location location) {
//                    Log.d("TAG", "Location obtained: " + location.toString());
//                    new Thread(() -> {
//                        getPlusCode(location);
//                    }).start();
//                    locationManager.removeUpdates(this); // Remove updates to prevent multiple calls
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//                    Log.d("TAG", "Status changed: " + status);
//                }
//
//                @Override
//                public void onProviderEnabled(@NonNull String provider) {
//                    Log.d("TAG", "Provider enabled: " + provider);
//                }
//
//                @Override
//                public void onProviderDisabled(@NonNull String provider) {
//                    Log.d("TAG", "Provider disabled: " + provider);
//                }
//            });
//        }
//    }
//
//    private void getPlusCode(Location location) {
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        Log.d("TAG", "Getting Plus Code for coordinates: " + latitude + ", " + longitude);
//
//        String apiKey = "AIzaSyDXe65LV0wWlF66xuGe2JdSgzlpjAYMy6I"; // Replace with your Google Maps API Key
//        String apiUrl = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s", latitude, longitude, apiKey);
//
//        try {
//            URL url = new URL(apiUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            int responseCode = connection.getResponseCode();
//            Log.d("TAG", "Response Code: " + responseCode);
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//            Log.d("TAG", "Response: " + response.toString());
//            parseGeocodeResponse(response.toString());
//        } catch (Exception e) {
//            Log.e("TAG", "Error in getPlusCode", e);
//        }
//    }
//
//    private void parseGeocodeResponse(String response) {
//        try {
//            JSONObject jsonResponse = new JSONObject(response);
//            JSONArray results = jsonResponse.getJSONArray("results");
//            if (results.length() > 0) {
//                JSONObject result = results.getJSONObject(0);
//                JSONObject plusCode = result.getJSONObject("plus_code");
//                String plusCodeString = plusCode.getString("global_code");
//                String city = null;
//
//                JSONArray addressComponents = result.getJSONArray("address_components");
//                for (int i = 0; i < addressComponents.length(); i++) {
//                    JSONObject component = addressComponents.getJSONObject(i);
//                    JSONArray types = component.getJSONArray("types");
//                    for (int j = 0; j < types.length(); j++) {
//                        if ("locality".equals(types.getString(j))) {
//                            city = component.getString("long_name");
//                            break;
//                        }
//                    }
//                }
//
//                if (city != null) {
//                    String locationString = String.format("(%s,%s)", plusCodeString, city);
//                    Log.d("TAG", "Location String: " + locationString);
//                    runOnUiThread(() -> {
//                        // Update your UI here with locationString
//                        Toast.makeText(this, locationString, Toast.LENGTH_LONG).show();
//                    });
//                } else {
//                    Log.d("TAG", "City not found in response.");
//                }
//            } else {
//                Log.d("TAG", "No results found in geocode response.");
//            }
//        } catch (Exception e) {
//            Log.e("TAG", "Error in parseGeocodeResponse", e);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLocation();
//            } else {
//                // Handle permission denial
//                Log.d("TAG", "Location permission denied.");
//            }
//        }
//    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    new Thread(() -> {
                        getLocationDetails(location);
                    }).start();
                    locationManager.removeUpdates(this); // Remove updates to prevent multiple calls
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(@NonNull String provider) {}

                @Override
                public void onProviderDisabled(@NonNull String provider) {}
            });
        }
    }

    private void getLocationDetails(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String locality = address.getSubLocality();
                String city = address.getLocality();
                if (locality == null) {
                    locality = address.getFeatureName(); // fallback if sub-locality is null
                }
                String locationString = "(" + locality + "," + city + ")";
                Log.d("Location111", locationString);
                // Update UI with the location string if needed
                runOnUiThread(() -> {
                    // Update your UI here with locationString
                    locationText.setText(locationString);
                    LOCATION=locationString;
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                // Handle permission denial
            }
        }
    }
    private void addMarkers(List<Material> materialList){

    }

}