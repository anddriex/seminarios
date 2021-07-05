package com.eapp.seminariosistemas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String EXTRA_CONTACT_NAME = "EXTRA_CONTACT_NAME";
    private static final String EXTRA_CONTACT_ADDRESS = "EXTRA_CONTACT_ADDRESS";
    private static final String EXTRA_CONTACT_PHONE = "EXTRA_CONTACT_PHONE";
    private static final String EXTRA_CONTACT_IMAGE = "EXTRA_CONTACT_IMAGE";

    private GoogleMap mMap;
    double[] latLong = new double[2];

    public static Intent newIntent(Context packageContext, Contact contact){
        Intent intent = new Intent(packageContext, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT_NAME, contact.getName());
        intent.putExtra(EXTRA_CONTACT_ADDRESS, contact.getAddress());
        intent.putExtra(EXTRA_CONTACT_PHONE, contact.getPhone());
        intent.putExtra(EXTRA_CONTACT_IMAGE, contact.getImage());

        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        String contactName = getIntent().getStringExtra(EXTRA_CONTACT_NAME);
        String contactAddress = getIntent().getStringExtra(EXTRA_CONTACT_ADDRESS);
        String  contactPhone = getIntent().getStringExtra(EXTRA_CONTACT_PHONE);
        String contactImage = getIntent().getStringExtra(EXTRA_CONTACT_IMAGE);

        TextView mAddresTextView = findViewById(R.id.contactAddresTextView);
        TextView mPhoneTextView = findViewById(R.id.contactPhoneTextView);
        TextView mNameTextView = findViewById(R.id.contactNameTextView);
        ImageView mImageView = findViewById(R.id.contactImageView);

        mAddresTextView.setText(contactAddress);
        mPhoneTextView.setText(contactPhone);
        mNameTextView.setText(contactName);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(contactImage));

        latLong = addressToLatlong(contactAddress);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public double[] addressToLatlong(String address) {
        double[] latLong = new double[2];
        return latLong;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latLong[0], latLong[1]);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}