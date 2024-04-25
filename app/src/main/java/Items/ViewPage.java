package Items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import Adapters.ItemSetGet;
import Adapters.ItemsAdapter;
import Dashboard.Dashboard;
import UserProfile.UserDetails;

public class ViewPage extends AppCompatActivity {
    private ItemsAdapter adapter;
    private RecyclerView recyclerView;
    public static String choosen_material="";
    private static final int REQUEST_CODE_MATERIAL_REQUEST = 123;
    private static final long TIME_INTERVAL = 2000; // Time interval for double press in milliseconds
    private long mBackPressed;

    int x=10;
    List<ItemSetGet> itemlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        Button plasticbtn=findViewById(R.id.vp_platicBtn);
        Button metalbtn=findViewById(R.id.vp_metalBtn);
        Button woodbtn=findViewById(R.id.vp_woodBtn);
        Button allbtn=findViewById(R.id.vp_allBtn);
        TextView user_name=findViewById(R.id.vp_displayname);

        ImageView back_to_dashboard=findViewById(R.id.vp_back_navigation);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        user_name.setText(UserDetails.getFullName()+"");
//        searchbar

        EditText searchEditText = findViewById(R.id.vp_searchbar); 

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
                String query = s.toString().trim();
                searchMaterialDescription(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
            }
        });


        String materialType=getIntent().getStringExtra("Material type");
        if (materialType != null){
            choosen_material=materialType;
            switch (materialType){
                case "plastic":
                    plasticbtn.setBackgroundResource(R.drawable.card_bg);
                    plasticbtn.setTextColor(getResources().getColor(R.color.white));
                    DatabaseReference itemRefPlastic= FirebaseDatabase.getInstance().getReference()
                            .child("Uploads");
                    itemRefPlastic.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<ItemSetGet> itemSetGetsList = new ArrayList<>();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                                if (item_materialtype != null && item_materialtype.equalsIgnoreCase("Plastic")){
                                    String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                                    String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                                    String owner_location = dataSnapshot.child("Owner Notification").getValue(String.class);
                                    String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                                    String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                                    String item_materialtitle=dataSnapshot.child("Material Description").getValue(String.class);
                                    String item_materialunit=dataSnapshot.child("Material Unit").getValue(String.class);
                                    String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                                    // Get the first image URL from the ImageUrls child node
                                    String imageUrl = null;
                                    DataSnapshot imageUrlsSnapshot = dataSnapshot.child("ImageUrls").getChildren().iterator().next();
                                    if (imageUrlsSnapshot.exists()) {
                                        imageUrl = imageUrlsSnapshot.child("Image").getValue(String.class);
                                    }                                    String itemID=dataSnapshot.getKey().toString();
                                    // Create Item object with the details
                                    ItemSetGet itemConstructor = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email, item_materialtype, owner_ID,owner_location,owner_name,item_uploaddate,item_materialunit,imageUrl,itemID);

                                    itemSetGetsList.add(itemConstructor);

                                }else {
                                }



                            }

                            adapter.updateData(itemSetGetsList);
                            Collections.reverse(itemSetGetsList);
                            adapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapter);

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    break;
                    case "metal":
                    metalbtn.setBackgroundResource(R.drawable.card_bg);
                    metalbtn.setTextColor(getResources().getColor(R.color.white));
                        DatabaseReference itemRefMetal= FirebaseDatabase.getInstance().getReference()
                                .child("Uploads");
                        itemRefMetal.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<ItemSetGet> itemSetGetsList = new ArrayList<>();
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                                    if (item_materialtype != null && item_materialtype.equalsIgnoreCase("Metal")){
                                        String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                                        String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                                        String owner_location = dataSnapshot.child("Owner Notification").getValue(String.class);
                                        String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                                        String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                                        String item_materialtitle=dataSnapshot.child("Material Description").getValue(String.class);
                                        String item_materialunit=dataSnapshot.child("Material Unit").getValue(String.class);
                                        String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                                        // Get the first image URL from the ImageUrls child node
                                        String imageUrl = null;
                                        DataSnapshot imageUrlsSnapshot = dataSnapshot.child("ImageUrls").getChildren().iterator().next();
                                        if (imageUrlsSnapshot.exists()) {
                                            imageUrl = imageUrlsSnapshot.child("Image").getValue(String.class);
                                        }                                        String itemID=dataSnapshot.getKey().toString();
                                        // Create Item object with the details
                                        ItemSetGet itemConstructor = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email, item_materialtype, owner_ID,owner_location,owner_name,item_uploaddate,item_materialunit,imageUrl,itemID);

                                        itemSetGetsList.add(itemConstructor);

                                    }else {
                                    }



                                }

                                adapter.updateData(itemSetGetsList);
                                Collections.reverse(itemSetGetsList);
                                adapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapter);

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;
                    case "wood":
                    woodbtn.setBackgroundResource(R.drawable.card_bg);
                    woodbtn.setTextColor(getResources().getColor(R.color.white));
                        DatabaseReference itemRefWood= FirebaseDatabase.getInstance().getReference()
                                .child("Uploads");
                        itemRefWood.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<ItemSetGet> itemSetGetsList = new ArrayList<>();
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                                    if (item_materialtype != null && item_materialtype.equalsIgnoreCase("Wood")){
                                        String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                                        String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                                        String owner_location = dataSnapshot.child("Owner Notification").getValue(String.class);
                                        String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                                        String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                                        String item_materialtitle=dataSnapshot.child("Material Description").getValue(String.class);
                                        String item_materialunit=dataSnapshot.child("Material Unit").getValue(String.class);
                                        String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                                        // Get the first image URL from the ImageUrls child node
                                        String imageUrl = null;
                                        DataSnapshot imageUrlsSnapshot = dataSnapshot.child("ImageUrls").getChildren().iterator().next();
                                        if (imageUrlsSnapshot.exists()) {
                                            imageUrl = imageUrlsSnapshot.child("Image").getValue(String.class);
                                        }                                        String itemID=dataSnapshot.getKey().toString();
                                        // Create Item object with the details
                                        ItemSetGet itemConstructor = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email, item_materialtype, owner_ID,owner_location,owner_name,item_uploaddate,item_materialunit,imageUrl,itemID);

                                        itemSetGetsList.add(itemConstructor);

                                    }else {
                                    }



                                }

                                adapter.updateData(itemSetGetsList);
                                Collections.reverse(itemSetGetsList);
                                adapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapter);

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;
                default:
                    break;
            }
        }
        plasticbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plasticbtn.setBackgroundResource(R.drawable.card_bg);
                plasticbtn.setTextColor(getResources().getColor(R.color.white));

                metalbtn.setBackgroundResource(R.drawable.round_button);
                metalbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                allbtn.setBackgroundResource(R.drawable.round_button);
                allbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                woodbtn.setBackgroundResource(R.drawable.round_button);
                woodbtn.setTextColor(getResources().getColor(R.color.blue_mod));

                DatabaseReference itemRef= FirebaseDatabase.getInstance().getReference()
                        .child("Uploads");
                itemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ItemSetGet> itemSetGetsList = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                            if (item_materialtype != null && item_materialtype.equalsIgnoreCase("Plastic")){
                                String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                                String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                                String owner_location = dataSnapshot.child("Owner Notification").getValue(String.class);
                                String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                                String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                                String item_materialtitle=dataSnapshot.child("Material Description").getValue(String.class);
                                String item_materialunit=dataSnapshot.child("Material Unit").getValue(String.class);
                                String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                                // Get the first image URL from the ImageUrls child node
                                String imageUrl = null;
                                DataSnapshot imageUrlsSnapshot = dataSnapshot.child("ImageUrls").getChildren().iterator().next();
                                if (imageUrlsSnapshot.exists()) {
                                    imageUrl = imageUrlsSnapshot.child("Image").getValue(String.class);
                                }                                String itemID=dataSnapshot.getKey().toString();
                                // Create Item object with the details
                                ItemSetGet itemConstructor = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email, item_materialtype, owner_ID,owner_location,owner_name,item_uploaddate,item_materialunit,imageUrl,itemID);

                                itemSetGetsList.add(itemConstructor);

                            }else {
                            }



                        }

                        adapter.updateData(itemSetGetsList);
                        Collections.reverse(itemSetGetsList);
                        adapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapter);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        metalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metalbtn.setBackgroundResource(R.drawable.card_bg);
                metalbtn.setTextColor(getResources().getColor(R.color.white));

                plasticbtn.setBackgroundResource(R.drawable.round_button);
                plasticbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                allbtn.setBackgroundResource(R.drawable.round_button);
                allbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                woodbtn.setBackgroundResource(R.drawable.round_button);
                woodbtn.setTextColor(getResources().getColor(R.color.blue_mod));

                DatabaseReference itemRef= FirebaseDatabase.getInstance().getReference()
                        .child("Uploads");
                itemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ItemSetGet> itemSetGetsList = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                            if (item_materialtype != null && item_materialtype.equalsIgnoreCase("Metal")){
                                String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                                String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                                String owner_location = dataSnapshot.child("Owner Notification").getValue(String.class);
                                String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                                String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                                String item_materialtitle=dataSnapshot.child("Material Description").getValue(String.class);
                                String item_materialunit=dataSnapshot.child("Material Unit").getValue(String.class);
                                String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                                // Get the first image URL from the ImageUrls child node
                                String imageUrl = null;
                                DataSnapshot imageUrlsSnapshot = dataSnapshot.child("ImageUrls").getChildren().iterator().next();
                                if (imageUrlsSnapshot.exists()) {
                                    imageUrl = imageUrlsSnapshot.child("Image").getValue(String.class);
                                }                                String itemID=dataSnapshot.getKey().toString();
                                // Create Item object with the details
                                ItemSetGet itemConstructor = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email, item_materialtype, owner_ID,owner_location,owner_name,item_uploaddate,item_materialunit,imageUrl,itemID);

                                itemSetGetsList.add(itemConstructor);

                            }else {
                            }



                        }

                        adapter.updateData(itemSetGetsList);
                        Collections.reverse(itemSetGetsList);
                        adapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapter);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        woodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woodbtn.setBackgroundResource(R.drawable.card_bg);
                woodbtn.setTextColor(getResources().getColor(R.color.white));

                metalbtn.setBackgroundResource(R.drawable.round_button);
                metalbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                allbtn.setBackgroundResource(R.drawable.round_button);
                allbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                plasticbtn.setBackgroundResource(R.drawable.round_button);
                plasticbtn.setTextColor(getResources().getColor(R.color.blue_mod));


                DatabaseReference itemRef= FirebaseDatabase.getInstance().getReference()
                        .child("Uploads");
                itemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ItemSetGet> itemSetGetsList = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                            if (item_materialtype != null && item_materialtype.equalsIgnoreCase("Wood")){
                                String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                                String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                                String owner_location = dataSnapshot.child("Owner Notification").getValue(String.class);
                                String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                                String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                                String item_materialtitle=dataSnapshot.child("Material Description").getValue(String.class);
                                String item_materialunit=dataSnapshot.child("Material Unit").getValue(String.class);
                                String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                                // Get the first image URL from the ImageUrls child node
                                String imageUrl = null;
                                DataSnapshot imageUrlsSnapshot = dataSnapshot.child("ImageUrls").getChildren().iterator().next();
                                if (imageUrlsSnapshot.exists()) {
                                    imageUrl = imageUrlsSnapshot.child("Image").getValue(String.class);
                                }
                                String itemID=dataSnapshot.getKey().toString();
                                // Create Item object with the details
                                ItemSetGet itemConstructor = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email, item_materialtype, owner_ID,owner_location,owner_name,item_uploaddate,item_materialunit,imageUrl,itemID);

                                itemSetGetsList.add(itemConstructor);

                            }else {
                            }



                        }

                        adapter.updateData(itemSetGetsList);
                        Collections.reverse(itemSetGetsList);
                        adapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapter);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });
        allbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allbtn.setBackgroundResource(R.drawable.card_bg);
                allbtn.setTextColor(getResources().getColor(R.color.white));

                metalbtn.setBackgroundResource(R.drawable.round_button);
                metalbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                plasticbtn.setBackgroundResource(R.drawable.round_button);
                plasticbtn.setTextColor(getResources().getColor(R.color.blue_mod));
                woodbtn.setBackgroundResource(R.drawable.round_button);
                woodbtn.setTextColor(getResources().getColor(R.color.blue_mod));


                DatabaseReference itemRef= FirebaseDatabase.getInstance().getReference().child("Uploads");
                itemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ItemSetGet> itemSetGetsList = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                            String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                            String owner_location = dataSnapshot.child("Owner Location").getValue(String.class);
                            String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                            String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                            String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                            String item_materialtitle=dataSnapshot.child("Material Description").getValue(String.class);
                            String item_materialunit=dataSnapshot.child("Material Unit").getValue(String.class);
                            String owner_ID=dataSnapshot.child("Owner ID").getValue(String.class);
                            String itemID=dataSnapshot.getKey().toString();

                            // Get the first image URL from the ImageUrls child node
                            String imageUrl = null;
                            DataSnapshot imageUrlsSnapshot = dataSnapshot.child("ImageUrls").getChildren().iterator().next();
                            if (imageUrlsSnapshot.exists()) {
                                imageUrl = imageUrlsSnapshot.child("Image").getValue(String.class);
                            }

                            // Create Item object with the details
                            ItemSetGet itemConstructor = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email, item_materialtype, owner_ID, owner_location=" ", owner_name, item_uploaddate, item_materialunit, imageUrl, itemID);
                            itemSetGetsList.add(itemConstructor);
                        }

                        adapter.updateData(itemSetGetsList);
                        Collections.reverse(itemSetGetsList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });



            }
        });
        back_to_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewPage.this, Dashboard.class));
            }
        });


        adapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ItemSetGet itemSetGet) {

                            Intent intent=new Intent(ViewPage.this,MaterialRequest.class);
                            intent.putExtra("ownername",itemSetGet.getItemOwner());
                            intent.putExtra("ownernumber",itemSetGet.getOwnerPhoneNumber());
                            intent.putExtra("owneremail",itemSetGet.getOwnerEmail());
                            intent.putExtra("location",itemSetGet.getItemLocation());
                            intent.putExtra("itemtype",itemSetGet.getItemType());
                            intent.putExtra("itemtitle1",itemSetGet.getItemName());
                            intent.putExtra("uploaddate",itemSetGet.getItemUploadDate());
                            intent.putExtra("materialunit",itemSetGet.getItemWeight());
                            intent.putExtra("ownerID",itemSetGet.getOwnerID());
                            intent.putExtra("itemID",itemSetGet.getItemUniqueID());
                            intent.putExtra("imageurl",itemSetGet.getItemImage());
                            intent.putExtra("username", UserDetails.getFullName());
                            intent.putExtra("userpNumber", UserDetails.getPhoneNumber());
                            intent.putExtra("email", UserDetails.getEmail());
                            startActivityForResult(intent, REQUEST_CODE_MATERIAL_REQUEST);
            }
        });

    }

    private void searchMaterialDescription(String query) {
        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child("Uploads");

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ItemSetGet> searchResults = new ArrayList<>();
                boolean foundMatch = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Retrieve data from Firebase
                    String item_materialtype = dataSnapshot.child("Material Type").getValue(String.class);
                    String owner_name = dataSnapshot.child("Owner Name").getValue(String.class);
                    String owner_email = dataSnapshot.child("Owner Email").getValue(String.class);
                    String owner_location = dataSnapshot.child("Owner Notification").getValue(String.class);
                    String owner_phonenumber = dataSnapshot.child("Owner PhoneNumber").getValue(String.class);
                    String item_uploaddate = dataSnapshot.child("Upload Date").getValue(String.class);
                    String item_materialtitle = dataSnapshot.child("Material Description").getValue(String.class);
                    String item_materialunit = dataSnapshot.child("Material Unit").getValue(String.class);
                    String owner_ID = dataSnapshot.child("Owner ID").getValue(String.class);
                    String imageUrl = dataSnapshot.child("ImageUrl").getValue(String.class);
                    String itemID=dataSnapshot.getKey().toString();

                    // Concatenate Material Description fields
                    StringBuilder concatenatedMaterialDescription = new StringBuilder();
                    if (item_materialtitle != null) {
                        concatenatedMaterialDescription.append(item_materialtitle.toLowerCase());
                    }

                    // Check if concatenated Material Description matches the query
                    if (concatenatedMaterialDescription.toString().contains(query.toLowerCase())) {
                        foundMatch = true;
                        // Create ItemSetGet object
                        ItemSetGet item = new ItemSetGet(item_materialtitle, owner_phonenumber, owner_email,
                                item_materialtype, owner_ID, owner_location, owner_name, item_uploaddate,
                                item_materialunit, imageUrl,itemID);
                        // Add to search results
                        searchResults.add(item);
                    }
                }

                // Update RecyclerView with search results
                if (searchResults.isEmpty()) {
                    // No matching items found
                    adapter.setClickable(false);
                    showNoMatchingItemsMessage();
                    // Make adapter unclickable
                } else {
                    adapter.updateData(searchResults);
                    Collections.reverse(searchResults);
                    adapter.setClickable(true); // Make adapter clickable
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


    private void showNoMatchingItemsMessage() {

//        recyclerView.setVisibility(View.GONE);
        // Display a toast message indicating no matching items found
        Toast.makeText(ViewPage.this, "Item does not exist!", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MATERIAL_REQUEST && resultCode == RESULT_OK) {
            // Handle the result from MaterialRequest activity if needed
            // For example, you can perform any actions or updates required
            // in the ViewPage activity based on the result from MaterialRequest activity.
            // This block will execute when MaterialRequest activity finishes successfully.
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