package com.example.testwhatatrip;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private CircleImageView profilePic;
    String Email,FullName,phoneNumber;
    public Uri imageUri;
    Button Update_Profile,Reset_Password;
    AlertDialog.Builder resetAlert;
    LayoutInflater inflater;
    FirebaseUser user;
    TextInputEditText email,fullName,Phone;
    DatabaseReference databaseReference;
    TextView BesidePic;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    final String UID =FirebaseAuth.getInstance().getUid().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profilePic = findViewById(R.id.profilepic);
        fullName = findViewById(R.id.Full_Name);
        resetAlert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = findViewById(R.id.Email_Profile);
        Phone = findViewById(R.id.Phone_Number);
        Update_Profile = findViewById(R.id.UpdateProfile);
        Reset_Password = findViewById(R.id.ResetPassword);
        BesidePic = findViewById(R.id.Sidepic);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images/profile/"+ UID);
        updateImage();
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePictures();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                Email =  snapshot.child("email").getValue(String.class);
                FullName = snapshot.child("full_name").getValue(String.class);
                phoneNumber = snapshot.child("phone").getValue(String.class);
                fullName.setText(FullName);
                email.setText(Email);
                Phone.setText(phoneNumber);
                BesidePic.setText("Hii "+ FullName);
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(UserProfile.this, "Not retrived", Toast.LENGTH_SHORT).show();
            }
        });
        Reset_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = inflater.inflate(R.layout.reset_password,null);
                resetAlert.setTitle("Reset Password").setMessage("Enter The New Password To Reset The Password")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText reset_profile_password = view.findViewById(R.id.reset_password_profile);
                                if (reset_profile_password.getText().toString().isEmpty()){
                                    reset_profile_password.setError("This field is required");
                                    return;
                                }
                                else{
                                    user.updatePassword(reset_profile_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(UserProfile.this, "Password Reset Success", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Toast.makeText(UserProfile.this, "Password Reset Failed"+e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("cancle",null).setView(view).show();
            }
        });
        Update_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFullnameChanged() || isEmailChanged() || isPhoneChanged()){
                    Toast.makeText(UserProfile.this, "Data Has Been Changed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UserProfile.this, "Data is same and cannot be changed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPhoneChanged() {
        if (!phoneNumber.equals(Phone.getText().toString())){
            databaseReference.child("phone").setValue(Phone.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!Email.equals(email.getText().toString())){
            user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UserProfile.this, "Email Updated", Toast.LENGTH_SHORT).show();
                    databaseReference.child("email").setValue(email.getText().toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(UserProfile.this, "Email not Updated"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isFullnameChanged() {
        if (!FullName.equals(fullName.getText().toString())){
            databaseReference.child("full_name").setValue(fullName.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }


    private void choosePictures() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            UploadPicture();
        }
    }

    private void UploadPicture() {
        StorageReference profileref  = storageReference;
        profileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserProfile.this, "Image uploaded Successfully", Toast.LENGTH_LONG).show();
                updateImage();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(UserProfile.this, "Failed To upload"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

// While the file names are the same, the references point to different files
//        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
//        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
    }

    private void updateImage() {
        try {
            File localFile = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profilePic.setImageBitmap(bitmap);
                    Toast.makeText(UserProfile.this, "Success Fully Downloaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}