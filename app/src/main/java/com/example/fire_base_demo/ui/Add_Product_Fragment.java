package com.example.fire_base_demo.ui;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fire_base_demo.Model.Product_Data;
import com.example.fire_base_demo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;


public class Add_Product_Fragment extends Fragment {

    EditText name,price,des;
    Button addData;
    ImageView img;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference mainBucket,storageReference;
    Bitmap imageBitmap;
    @SuppressLint("WrongThread")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        name=view.findViewById(R.id.add_name);
        price=view.findViewById(R.id.add_price);
        des=view.findViewById(R.id.add_des);
        addData=view.findViewById(R.id.register);
        img = view.findViewById(R.id.add_img);

        final CharSequence[] items = {"Camera", "Gallery"};


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Camera")) {
                            launchCamera();

                        } else if (items[which].equals("Gallery")) {

                            Intent GalleryIntent = null;
                            GalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            GalleryIntent.setType("image/*");
                            GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(GalleryIntent, 0);
                        }
                    }
                });
                builder.show();

                if (ContextCompat.checkSelfPermission(Add_Product_Fragment.this.getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // If not granted, request the CAMERA permission
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 100);
                }
            }

        });


        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance(); // initializing object of database
                myRef = database.getReference("Product").push(); // Creating main (parent) reference
                Log.d("TTT", "testCode2: "+myRef);
                String idd = myRef.getKey();
                Log.d("TTT", "testCode2 id=: "+idd);


                storageReference = FirebaseStorage.getInstance().getReference();
                //storageReference=FirebaseStorage.getInstance().getReference("images"+imagename);
                String imageName = "Image" + new Random().nextInt(10000) + ".jpg";

                StorageReference imgRef = storageReference.child("Images/"+imageName);

                // While the file names are the same, the references point to different files
                imgRef.getName().equals(imgRef.getName());    // true
                imgRef.getPath().equals(imgRef.getPath());    // false

                img.setDrawingCacheEnabled(true);
                img.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imgRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("UUU", "onFailure: Failed to upload");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Log.d("UUU", "onSuccess: Image uploaded");

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return imgRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String imgUrl= String.valueOf(downloadUri);

                                    database = FirebaseDatabase.getInstance(); // initializing object of database
                                    myRef = database.getReference("Products").push(); // Creating main (parent) reference
                                    Log.d("TTT", "testCode2: "+myRef);
                                    String id = myRef.getKey();

                                    Product_Data dataModel = new Product_Data(idd,name.getText().toString(), price.getText().toString(), des.getText().toString(),imgUrl);
                                    myRef.setValue(dataModel);

                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });

                    }
                });

            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for take image from gallery
        if (resultCode == Activity.RESULT_OK) {

            Log.i("GalleryCode", "" + requestCode);
            Uri ImageURI = data.getData();
            img.setImageURI(ImageURI);

        }

        // for take image from camera
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Get the captured image from the intent's data
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            // Do something with the captured image
            img.setImageBitmap(imageBitmap);
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with launching the camera
                launchCamera();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }
}

