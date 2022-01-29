package de.pme.eventhunt.model.utilities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.repositories.UserRepository;

public class Image {

    private String downloadUrlSmall;
    private String downloadUrlLarge;

    private Bitmap bitmapSmall;
    private Bitmap bitmapLarge;


    private boolean uploadStarted = false;

    public void StartUpload()
    {
        UploadImage(bitmapSmall, 0);
        UploadImage(bitmapLarge, 1);
    }

    public void UploadProfileImage(User user, Activity activity)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String referenceId = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(referenceId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapLarge.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        uploadStarted = true;

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploadStarted = false;
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url = uri.toString();
                        Log.e("TAG:", "the url is: " + url);

                        String ref = imageRef.getName();
                        Log.e("TAG:", "the ref is: " + ref);

                        downloadUrlSmall = url;
                        user.setImageSmallRef(downloadUrlSmall);
                        UserRepository userRepository = new UserRepository();
                        userRepository.createUser(user);

                        Toast.makeText(activity, "Registering user successful!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }
    public void editProfileImage(User user, Activity activity)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String referenceId = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(referenceId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapLarge.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        uploadStarted = true;

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploadStarted = false;
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url = uri.toString();
                        Log.e("TAG:", "the url is: " + url);

                        String ref = imageRef.getName();
                        Log.e("TAG:", "the ref is: " + ref);

                        downloadUrlSmall = url;
                        UserRepository userRepository = new UserRepository();
                        userRepository.updateUserImage(user,downloadUrlSmall);



                    }
                });
            }
        });

    }
    public void CreateBitmapSmall(Uri imageUri)
    {
        if(bitmapSmall == null)
        {
            Picasso.get()
                    .load(imageUri)
                    .resize(200, 200)
                    .centerCrop()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            bitmapSmall = bitmap;
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    public void CreateBitmapLarge(Uri imageUri)
    {
        if(bitmapLarge == null)
        {
            Picasso.get()
                    .load(imageUri)
                    .resize(1080, 720)
                    .centerCrop()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            bitmapLarge = bitmap;
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    public void CreateBitmapLarge(Uri imageUri, ImageView imageView)
    {
        if(bitmapLarge == null)
        {
            Picasso.get()
                    .load(imageUri)
                    .resize(1080, 720)
                    .centerCrop()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            bitmapLarge = bitmap;
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    private void UploadImage(Bitmap bitmap, int smallOrLarge)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String referenceId = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(referenceId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        uploadStarted = true;

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploadStarted = false;
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url = uri.toString();
                        Log.e("TAG:", "the url is: " + url);

                        String ref = imageRef.getName();
                        Log.e("TAG:", "the ref is: " + ref);

                        if(smallOrLarge == 0)
                        {
                            downloadUrlSmall = url;
                        }
                        else if(smallOrLarge == 1)
                        {
                            downloadUrlLarge = url;
                        }
                    }
                });
            }
        });
    }

    public Boolean IsFinished()
    {
        if(downloadUrlLarge == null || downloadUrlSmall == null) return false;
        else return true;
    }

    public String getDownloadUrlSmall() {
        return downloadUrlSmall;
    }

    public String getDownloadUrlLarge() {
        return downloadUrlLarge;
    }

    public Bitmap getBitmapSmall() {
        return bitmapSmall;
    }

    public Bitmap getBitmapLarge() {
        return bitmapLarge;
    }

    public boolean isUploadStarted() {
        return uploadStarted;
    }
}
