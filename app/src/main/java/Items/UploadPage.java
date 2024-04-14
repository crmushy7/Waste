package Items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wmeaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Nullable;

import Adapters.ImageAdapter;
import Adapters.ImageModel;
import Authentication.RegistrationMod;
import Dashboard.Dashboard;
import UserProfile.UserDetails;
import UserProfile.Userprofile;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


public class UploadPage extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    String[] dropDownData={"Plastic","Metal","Wood"};
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private ImageView imageView;
    private Uri imageUri;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static String username;
    public static String useremail;
    public static String phonenumber;
    public static String materialType;
    public static String materialDescription;
    public static String materialUnit;
    public static String uploadDate;
    private static final long TIME_INTERVAL = 2000; // Time interval for double press in milliseconds
    private long mBackPressed;
    private RecyclerView recyclerView;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private ImageAdapter adapter;
    Handler handler;
    ProgressDialog progressDialog;
    EditText materialT,materialU;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);
        handler=new Handler(Looper.getMainLooper());

        handler.post(() -> {
            progressDialog = new ProgressDialog(UploadPage.this);
            progressDialog.setMessage("Please wait, make sure you have stable internet connection...");
            progressDialog.setCancelable(false);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageAdapter(getApplicationContext(),imageUris);
        recyclerView.setAdapter(adapter);

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        handler=new Handler(Looper.getMainLooper());
         materialT=findViewById(R.id.upl_materialTitle);
         materialU=findViewById(R.id.upl_materialUnit);
        TextView usernametv=findViewById(R.id.upl_displayuser);

        Spinner spinner=findViewById(R.id.materialtype_spinner);
        LinearLayout chooseFromFilebtn=findViewById(R.id.upl_choosefromFile);
        LinearLayout takeCamerabtn=findViewById(R.id.upl_choosefromCamera);
        Button uploadBtn=findViewById(R.id.upl_uploadbtn);

        usernametv.setText(UserDetails.getFullName()+"");
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mat_title=materialT.getText().toString().trim();
                String mat_unit=materialU.getText().toString().trim();
                if (mat_title.isEmpty()){
                    materialT.setError("Fill title");
                    materialT.requestFocus();
                } else if (mat_unit.isEmpty()) {
                    materialU.setError("Fill here");
                    materialU.requestFocus();
                }else{
                    materialDescription=mat_title+"";
                    materialUnit=mat_unit+"";
                    Calendar calendar=Calendar.getInstance();
                    String currentdate= DateFormat.getDateInstance().format(calendar.getTime());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String formattedTime = simpleDateFormat.format(new Date());
                    uploadDate=currentdate+" "+formattedTime;
                    username= UserDetails.getFullName();
                    useremail=UserDetails.getEmail();
                    phonenumber=UserDetails.getPhoneNumber();
                    uploadToFirestore(v);

                }

            }
        });

        String backTo=getIntent().getStringExtra("backTo");
        ImageView backNavbtn=findViewById(R.id.upl_back_navigation);
        chooseFromFilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromFileManager(v);
            }
        });
        takeCamerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });
        backNavbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backTo.equals("dashboard")){
                    startActivity(new Intent(UploadPage.this, Dashboard.class));
                }else {
                    String fullName = UserDetails.getFullName();
                    String phoneNumber = UserDetails.getPhoneNumber();
                    String email = UserDetails.getEmail();

                    Intent intent = new Intent(UploadPage.this, Userprofile.class);
                    intent.putExtra("name", fullName);
                    intent.putExtra("pNumber", phoneNumber);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,dropDownData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption=dropDownData[position];
                materialType=selectedOption;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void chooseFromFileManager(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple selection
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    public void takePhoto(View view) {
        // Check if the camera permission is not granted yet
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, proceed with capturing image
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
            } else if (data.getClipData() != null) {
                // Multiple images selected
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }
            }
            adapter.notifyDataSetChanged();
            // Update UI to display selected images, e.g., add to a GridView or RecyclerView
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            // Handle camera pics
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri imageUri = getImageUri(this, photo);
            imageUris.add(imageUri);
            adapter.notifyDataSetChanged(); // Update RecyclerView
        }
    }

    public Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
        return Uri.parse(path);
    }

    public void uploadToFirestore(View view) {
        if (imageUris != null && !imageUris.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            AtomicInteger uploadCount = new AtomicInteger(0); // Initialize upload count
            int totalUploads = imageUris.size(); // Store the total number of uploads
            progressDialog.show();
            materialT.setText("");
            materialU.setText("");

            // Iterate over each image URI in the list
            for (Uri imageUri : imageUris) {
                // Upload each image to Firebase Storage and collect image URLs

                uploadImageToStorage(imageUri, imageUrls, uploadCount, totalUploads);
            }
        } else {
            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImageToStorage(Uri imageUri, List<String> imageUrls, AtomicInteger uploadCount, int totalUploads) {
        if (imageUri != null) {
            // Create a reference to Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final String uploadKey = UUID.randomUUID().toString();
            StorageReference imagesRef = storageRef.child("images/" + uploadKey);

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imagesRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Image download URL retrieved
                    String imageUrl = uri.toString();
                    imageUrls.add(imageUrl); // Add image URL to the list

                    // Check if all uploads are completed
                    if (uploadCount.incrementAndGet() == totalUploads) {
                        // All uploads completed, save image URLs and additional data
                        saveImageUrlToFirestore(imageUrls);
                        saveAdditionalDataToDatabase(imageUrls);
                        imageUris.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
            }).addOnFailureListener(e -> {
                // Image upload failed
                Toast.makeText(UploadPage.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUrlToFirestore(List<String> imageUrls) {
        // Add code to save imageUrls to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Replace "collectionName" with your actual collection name
        db.collection("images")
                .add(new ImageModel(imageUrls))
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(UploadPage.this, "Image URLs saved to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UploadPage.this, "Error saving image URLs to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveAdditionalDataToDatabase(List<String> imageUrls) {
        // Add additional data to Firebase Realtime Database
        DatabaseReference databaseReferenceUpld = FirebaseDatabase.getInstance().getReference().child("Uploads");
        DatabaseReference upload = databaseReferenceUpld.push();
        upload.child("Owner Name").setValue(getIntent().getStringExtra("name")+"");
        upload.child("Owner Location").setValue("iyumbu,Dodoma");
        upload.child("Owner Email").setValue(getIntent().getStringExtra("email")+"");
        upload.child("Owner PhoneNumber").setValue(getIntent().getStringExtra("pNumber")+"");
        upload.child("Upload Date").setValue(uploadDate);
        upload.child("Material Type").setValue(materialType);
        upload.child("Material Description").setValue(materialDescription);
        upload.child("Material Unit").setValue(materialUnit);
        upload.child("Owner ID").setValue(FirebaseAuth.getInstance().getUid().toString());

        // Store all image URLs in a child node
        DatabaseReference imageUrlsRef = upload.child("ImageUrls");
        for (String imageUrl : imageUrls) {
            imageUrlsRef.push().child("Image").setValue(imageUrl);
        }
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
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