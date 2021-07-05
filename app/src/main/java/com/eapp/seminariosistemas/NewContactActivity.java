package com.eapp.seminariosistemas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class NewContactActivity extends AppCompatActivity {
    private static final String EXTRA_NEW_CONTACT_NAME = "EXTRA_CONTACT_NAME";
    private static final String EXTRA_NEW_CONTACT_ADDRESS = "EXTRA_CONTACT_ADDRESS";
    private static final String EXTRA_NEW_CONTACT_PHONE = "EXTRA_CONTACT_PHONE";
    TextView profileNameText, profileAddressText, profilePhoneText, profileEmailText;
    ImageView mainImageView;
    //int image = R.drawable.jedi;

    String image = "";

    EditText editTextName, editTextPhone, editTextAddress;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Button lectorQrButton;
    int count = 0;

    public static Intent newIntent(Context packageContext, Contact contact) {
        Intent intent = new Intent(packageContext, NewContactActivity.class);
        intent.putExtra(EXTRA_NEW_CONTACT_NAME, contact.getName());
        intent.putExtra(EXTRA_NEW_CONTACT_ADDRESS, contact.getAddress());
        intent.putExtra(EXTRA_NEW_CONTACT_PHONE, contact.getPhone());
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        String contactName = getIntent().getStringExtra(EXTRA_NEW_CONTACT_NAME);
        String contactAddress = getIntent().getStringExtra(EXTRA_NEW_CONTACT_ADDRESS);
        String contactPhone = getIntent().getStringExtra(EXTRA_NEW_CONTACT_PHONE);

        mainImageView = findViewById(R.id.mainImageViewUser);
        editTextAddress = findViewById(R.id.editTextTextAddress);
        editTextName = findViewById(R.id.editTextTextName);
        editTextPhone = findViewById(R.id.editTextTextPhone);
        profilePhoneText = findViewById(R.id.profilePhoneText);
        profileAddressText = findViewById(R.id.profileAddressText);
        profileNameText = findViewById(R.id.profileNameText);
        profileEmailText = findViewById(R.id.emailProfileText);
        lectorQrButton = findViewById(R.id.buttonLectorQr);

        sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        profileEmailText.setText(sharedPref.getString("email", ""));
        mainImageView.setImageResource(sharedPref.getInt("image", 0));
        profileNameText.setText(sharedPref.getString("fullName", ""));
        profileAddressText.setText(sharedPref.getString("address", ""));
        profilePhoneText.setText(sharedPref.getString("phone", ""));

        editTextName.setText(contactName);
        editTextAddress.setText(contactAddress);
        editTextPhone.setText(contactPhone);

        lectorQrButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LectorQrActivity.class);
            startActivity(intent);
        });
    }


    public void onClickSave(View v) {
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String address = editTextAddress.getText().toString();
        Contact newContact = new Contact(image, name, phone, address);
        newContact.save();
        long newId = newContact.getId();
        Log.d("NEW_CONTACT", String.valueOf(newId));
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
        finish();
    }

//    public void onClickSelectImage(View view) {
//        switch (view.getId()) {
//            case R.id.imageViewUser1:
//                image = R.drawable.usuario1;
//                break;
//            case R.id.imageViewUser3:
//                image = R.drawable.usuario3;
//                break;
//            case R.id.imageViewUser5:
//                image = R.drawable.usuario5;
//                break;
//        }
//        mainImageView.setImageResource(image);
//    }

    // Implementacion de camara
    public void onClickTakePic(View v){
        switch (v.getId()){
            case R.id.buttonCamera:
                count = sharedPref.getInt("image",0);
                count++;
                image = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + "fotouser"+count+".jpg";
                System.out.println(image);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                editor.putInt("imagen",count);
                editor.commit();

                //Uri output = Uri.fromFile(new File(photo));
                Uri output = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".fileprovider"
                        , new File(image));


                intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                startActivityForResult(intent, 0);
                break;
            case R.id.buttonGallery:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == this.RESULT_OK) {
                    File fileTemp = new File(image);
                    if (!fileTemp.exists()) {
                        Toast.makeText(this,
                                "No se ha realizado la foto", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        mainImageView.setImageBitmap(BitmapFactory.decodeFile(image));
                    }
                }

                break;
            case 1:
                if (resultCode == this.RESULT_OK) {
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(uri, projection,
                            null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    image = cursor.getString(columnIndex); // returns null
                    cursor.close();
                    mainImageView.setImageBitmap(BitmapFactory.decodeFile(image));
                }
                break;
        }

    }


}