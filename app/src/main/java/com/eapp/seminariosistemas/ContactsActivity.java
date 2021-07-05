package com.eapp.seminariosistemas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private static final String TAG = "ContactsActivity";
    RecyclerView recyclerViewContacts;
    TextView profileNameText, profileAddressText, profilePhoneText, profileEmailText;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        SharedPreferences sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("image", R.drawable.jedi);
        editor.putString("fullName", getString(R.string.fullName));
        editor.putString("email", getString(R.string.email));
        editor.putString("phone", getString(R.string.phone));
        editor.putString("address", getString(R.string.address));
        editor.apply();
        profilePhoneText = findViewById(R.id.profilePhoneText);
        profileAddressText = findViewById(R.id.profileAddressText);
        profileNameText = findViewById(R.id.profileNameText);
        profileImageView = findViewById(R.id.profileImageView);
        profileEmailText = findViewById(R.id.emailProfileText);

        profileEmailText.setText(sharedPref.getString("email", ""));
        profileImageView.setImageResource(sharedPref.getInt("jedi", R.drawable.jedi));
        profileNameText.setText(sharedPref.getString("fullName", ""));
        profileAddressText.setText(sharedPref.getString("address", ""));
        profilePhoneText.setText(sharedPref.getString("phone", ""));

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewContacts.setLayoutManager(layoutManager);
        List<Contact> contactsList = Contact.listAll(Contact.class);
        ContactsAdapter adapter = new ContactsAdapter(contactsList);
        recyclerViewContacts.setAdapter(adapter);

        adapter.setOnClickListener(new AdapterCommunication() {
            @Override
            public void removeItem(long itemId, int position) {
                Contact contactToRemove = Contact.findById(Contact.class, itemId);
                contactToRemove.delete();
            }
        });
    }

    protected class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Contact mContact;

        public ContactHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.layout_row, parent, false));
            itemView.setOnClickListener(this);
        }

        public void bind(Contact contact) {
            mContact = contact;
            TextView mNameTextView = itemView.findViewById(R.id.textViewRowContactName);
            TextView mPhoneTextView = itemView.findViewById(R.id.textViewRowContactPhone);
            ImageView mContactImage = itemView.findViewById(R.id.imageViewRowContact);
            mNameTextView.setText(mContact.getName());
            mContactImage.setImageBitmap(BitmapFactory.decodeFile(mContact.getImage()));
            mPhoneTextView.setText(mContact.getPhone());
        }

        @Override
        public void onClick(View view) {
            Intent intent = ContactDetailActivity.newIntent(itemView.getContext(), mContact);
            startActivity(intent);
        }
    }

    private class ContactsAdapter extends RecyclerView.Adapter<ContactHolder> {
        private final List<Contact> listaContacts;
        private AdapterCommunication mListener;

        public void setOnClickListener(AdapterCommunication listener) {
            mListener = listener;
        }

        public ContactsAdapter(List<Contact> listaContacts) {
            this.listaContacts = listaContacts;
        }

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new ContactHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            Contact contact = listaContacts.get(position);
            holder.itemView.findViewById(R.id.deleteContactImageButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.removeItem(contact.getId(), position);
                    listaContacts.remove(position);
                    notifyItemRemoved(position);
                }
            });
            holder.bind(contact);
        }

        @Override
        public int getItemCount() {
            return listaContacts.size();
        }
    }

    public void onClickNewContact(View v) {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivity(intent);
        finish();
    }
}

