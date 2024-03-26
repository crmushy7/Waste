package Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmeaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.regex.Pattern;

import Dashboard.Dashboard;
import UserProfile.Userprofile;

public class RegistrationMod extends AppCompatActivity {
    public static String firstName="";
    public static String lastName="";
    public static String userEmail="";
    public static String phoneNumber="";
    public static String password="";
    public static String passwordLogin="";
    public static String emailLogin="";
    public static String confirmpassword="";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Handler handler;
    ProgressDialog progressDialog;
    private  AlertDialog dialog;
    Button nextName, nextEmail,signUpPass,backEmail,backPass,signIn;
    EditText etFirst,etLast,etEmail,etPhone,etPassword,etConfirmP,etemaillogin,etpassswdlogin;
    TextView header,alreadyhave,donthave,forgotpassword;
    LinearLayout Names,EmailPh,Passw,login;
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    Pattern pat = Pattern.compile(emailRegex);
    private static final long TIME_INTERVAL = 2000; // Time interval for double press in milliseconds
    private long mBackPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_mod);

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        handler=new Handler(Looper.getMainLooper());
//        buttons
        nextName=findViewById(R.id.btn_nextfirstandlastname);
        nextEmail=findViewById(R.id.btn_next_emailandphonenumber);
        backEmail=findViewById(R.id.btn_previous_emailandphonenumber);
        backPass=findViewById(R.id.btn_previous_passwordandconfirm);
        signUpPass=findViewById(R.id.btn_next_passwordandconfirm);
        signIn=findViewById(R.id.btn_signin);
//        layouts naziunganisha
        Names=findViewById(R.id.firstandlast_names);
        EmailPh=findViewById(R.id.emailandphoneNumber);
        Passw=findViewById(R.id.passwordandconfirmpassword);
        login=findViewById(R.id.loginlayout);
//        edittexts zilizotumika
        etemaillogin=findViewById(R.id.tv_emailLogin);
        etpassswdlogin=findViewById(R.id.tv_passwordLogin);
        etFirst=findViewById(R.id.tv_firstName);
        etLast=findViewById(R.id.tv_lastName);
        etEmail=findViewById(R.id.tv_email);
        etPhone=findViewById(R.id.tv_phoneNumber);
        etPassword=findViewById(R.id.tv_password);
        etConfirmP=findViewById(R.id.tv_confirmPassword);
//        textviews
        header=findViewById(R.id.tv_signUp);
        alreadyhave=findViewById(R.id.alreadyhaveaccount);
        donthave=findViewById(R.id.donthaveaccount);
        forgotpassword=findViewById(R.id.forgotpassword);

        handler.post(() -> {
            progressDialog = new ProgressDialog(RegistrationMod.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);

        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordReset();
            }
        });
        donthave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Names.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                header.setText("Sign up");
            }
        });
        alreadyhave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.VISIBLE);
                Names.setVisibility(View.GONE);
                header.setText("Sign in");
            }
        });
        nextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName=etFirst.getText().toString().trim();
                String lName=etLast.getText().toString();
                if (fName.isEmpty()){
                    etFirst.setError("Fill your firstname");
                } else if (lName.isEmpty()) {
                    etLast.setError("Fill your last name");
                }else{
                    firstName=fName;
                    lastName=lName;
                    Names.setVisibility(View.GONE);
                    EmailPh.setVisibility(View.VISIBLE);
                }

            }
        });
        nextEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmail.getText().toString().trim();
                String pNumber=etPhone.getText().toString();
                if (email.isEmpty()){
                    etEmail.setError("Fill your email");
                }else if (!pat.matcher(email).matches()) {
                    etEmail.setError("Please Enter a valid Email");
                    return;
                } else if (pNumber.isEmpty()) {
                    etPhone.setError("Fill your phonenumber");
                }else{
                    userEmail=email;
                    phoneNumber=pNumber;
                    EmailPh.setVisibility(View.GONE);
                    Passw.setVisibility(View.VISIBLE);
                }

            }
        });
        backPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailPh.setVisibility(View.VISIBLE);
                Passw.setVisibility(View.GONE);
            }
        });
        backEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Names.setVisibility(View.VISIBLE);
                EmailPh.setVisibility(View.GONE);
            }
        });
        signUpPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=etPassword.getText().toString();
                String confP=etConfirmP.getText().toString();
                if (pass.isEmpty()){
                    etPassword.setError("Fill your password");
                } else if (confP.isEmpty()) {
                    etConfirmP.setError("Fill your password");
                } else if (!(pass.equals(confP))) {
                    etConfirmP.setError("password does not match!");
                    TextView toastText=new TextView(getApplicationContext());
                    toastText.setText("Password does not match");
                    toastText.setTextColor(Color.WHITE);
                    toastText.setPadding(16,16,16,16);
                    toastText.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));

                    Toast toast=new Toast(getApplicationContext());
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastText);
                    toast.show();
//                    Toast toast=Toast.makeText(RegistrationMod.this, "Password does not match", Toast.LENGTH_SHORT);
//                    View view=toast.getView();
//                    if (view!=null){
//                        view.getBackground().setColorFilter(ContextCompat.getColor(RegistrationMod.this,R.color.green), PorterDuff.Mode.SRC_IN);
//                        TextView textView=view.findViewById(android.R.id.message);
//                        textView.setTextColor(ContextCompat.getColor(RegistrationMod.this,R.color.white_mod));
//                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,15);
//                        toast.show();
//                    }else {
//                        Toast.makeText(RegistrationMod.this, "ggggggg", Toast.LENGTH_SHORT).show();
//                    }

                } else{
                    password=pass;
                    confirmpassword=confP;
                    userRegistration();
                    Toast.makeText(RegistrationMod.this, "NAME: "+firstName+" "+lastName+"\n"+"Email: "+userEmail+"\n"+"Phone: "+phoneNumber+" \n"+"Password: "+password, Toast.LENGTH_LONG).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etemaillogin.getText().toString().trim();
                String password=etpassswdlogin.getText().toString().trim();
                if (email.length()==0){
                    etemaillogin.setError("Write your email");
                } else if (password.length()==0) {
                    etpassswdlogin.setError("fill password");
                }
                else{

                    handler.post(() -> {
                        progressDialog = new ProgressDialog(RegistrationMod.this);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    });
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        etemaillogin.setText("");
                                        etpassswdlogin.setText("");


//        getting deviceID ili tutume notifications
                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful() && task2.getResult() != null) {
                                                        String fcmToken = task2.getResult();
//                        sending it to firebase ya user
                                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("All Users")
                                                                .child(FirebaseAuth.getInstance().getUid())
                                                                .child("Details");
                                                        userRef.child("FCM Token").setValue(fcmToken);
                                                    }
                                                });

                                        fetchAndStoreUserData();

                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(RegistrationMod.this, "Failed to log in", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void userRegistration() {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Fullname",firstName+" "+lastName);
        hashMap.put("username",userEmail);
        hashMap.put("PhoneNumber",phoneNumber);
        hashMap.put("Password",password);

        handler.post(() -> {
            progressDialog = new ProgressDialog(RegistrationMod.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        });
        firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();


                            Toast.makeText(RegistrationMod.this, "Success", Toast.LENGTH_SHORT).show();
                            //our database operations here
                            databaseReference.child("All Users")
                                    .child(firebaseAuth.getUid().toString())
                                    .child("Details")
                                    .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(RegistrationMod.this, "Successful", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(RegistrationMod.this, "User Registered!", Toast.LENGTH_LONG).show();
                                                etFirst.setText("");
                                                etLast.setText("");
                                                etPhone.setText("");
                                                etPassword.setText("");
                                                etConfirmP.setText("");
                                                login.setVisibility(View.VISIBLE);
                                                Passw.setVisibility(View.GONE);
                                                header.setText("Sign in");
                                            } else {
                                                Toast.makeText(RegistrationMod.this, "Failed", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistrationMod.this, "Fail! User not registered!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationMod.this, "Unsuccessful! User not registered!", Toast.LENGTH_LONG).show();

                            // If sign in fails, check the exception and handle specific errors
                            if (task.getException() != null && task.getException() instanceof FirebaseAuthException) {
                                FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                                String errorCode = firebaseAuthException.getErrorCode();
                                Toast.makeText(RegistrationMod.this, errorCode, Toast.LENGTH_SHORT).show();
                                etEmail.setError("Email already in use");
                            }
                        }
                    }
                });


    }

    private void passwordReset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationMod.this);
        View popupView = LayoutInflater.from(RegistrationMod.this).inflate(R.layout.activity_resetpassword, null);

        EditText email_et = popupView.findViewById(R.id.rp_userEmail);
        Button proceedtoReset = popupView.findViewById(R.id.rp_resetButton);

        proceedtoReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String my_email=email_et.getText().toString().trim();
                if (my_email.isEmpty()){
                    progressDialog.dismiss();
                    email_et.setError("Email required");
                }else if (!pat.matcher(my_email).matches()) {
                    progressDialog.dismiss();
                    email_et.setError("Invalid email!");
                }else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(my_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationMod.this, "Reset email sent successful to "+my_email, Toast.LENGTH_SHORT).show();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationMod.this, "Error reseting, retry later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        builder.setView(popupView);
        dialog = builder.create();
        dialog.show();
    }

    private void fetchAndStoreUserData() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("All Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Details");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve user details from Firebase snapshot
                    String fullName = snapshot.child("Fullname").getValue(String.class);
                    String phoneNumber = snapshot.child("PhoneNumber").getValue(String.class);
                    String email = snapshot.child("username").getValue(String.class);
                    String password = snapshot.child("Password").getValue(String.class);
                    String FCM_Token = snapshot.child("FCM Token").getValue(String.class);

                    // Store user data locally using SharedPreferences
                    storeUserDataLocally(fullName, phoneNumber, email,password,FCM_Token);

                    // Proceed to main app functionality (e.g., dashboard)
                    startMainActivity();
                } else {
                    // Handle case where user data doesn't exist
                    Toast.makeText(RegistrationMod.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(RegistrationMod.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserDataLocally(String fullName, String phoneNumber, String email,String password, String FCM_Token) {
        // Get SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("User_data", Context.MODE_PRIVATE);

        // Get SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store user data in SharedPreferences
        editor.putString("full_name", fullName);
        editor.putString("phone_number", phoneNumber);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("FCM_Token", FCM_Token);

        // Commit the changes
        editor.apply();
    }

    private void startMainActivity() {
        // Start your main activity here
        progressDialog.dismiss();
        Toast.makeText(RegistrationMod.this, "Successfull", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(RegistrationMod.this,Dashboard.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent intent=new Intent(RegistrationMod.this, Dashboard.class);
            startActivity(intent);
            finish();
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