package com.example.habibaabbasii.testcase.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.habibaabbasii.testcase.Constants;
import com.example.habibaabbasii.testcase.Manifest;
import com.example.habibaabbasii.testcase.R;
import com.example.habibaabbasii.testcase.databinding.FragmentEditProfileBinding;
import com.example.habibaabbasii.testcase.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public static final int GALLERY_CODE = 100;
    public static final int PERMISSION_CODE = 200;

    Uri imagePath = null;

    FragmentEditProfileBinding binding;

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    StorageReference mStorage;
    String permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_edit_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference(Constants.USERS);
        mStorage = FirebaseStorage.getInstance().getReference(Constants.PHOTOS);

        setupListener();
        return binding.getRoot();
    }

    private void setupListener() {
        binding.menu.setOnClickListener(this);
        binding.image.setOnClickListener(this);
        binding.edit.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        if (view == binding.menu) {
            mListener.onEditProfileFragmentInteraction(OnFragmentInteractionListener.MENU);
        } else if (view == binding.image) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                        Snackbar.make(getActivity().findViewById(android.R.id.content), "App needs to access your external storage",
                                Snackbar.LENGTH_INDEFINITE).setAction("Enable", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSION_CODE);

                            }
                        }).show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSION_CODE);
                    }
                }

            } else
                openGallery();

        } else if (view == binding.edit) {

            if (binding.name.length() > 0 &&
                    binding.contact.length() > 0 &&
                    imagePath != null) {

                updateProfile(binding.name.toString(), binding.contact.toString());
            } else {
                Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void updateProfile(final String name, final String contact) {

        mStorage.child(imagePath.getLastPathSegment()).putFile(imagePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imagePath = taskSnapshot.getDownloadUrl();

                        //updating database
                        mRef.orderByChild("uId")
                                .equalTo(mAuth.getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        System.out.println("ok");
                                        Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                                        UserModel user = null;
                                        String key = null;
                                        for (DataSnapshot ch : child) {
                                            user = ch.getValue(UserModel.class);
                                            key = ch.getKey();
                                        }

                                        user.setImage(imagePath.toString());
                                        user.setContact(contact);
                                        user.setUserNAme(name);

//                                        Map<String, Object> map = new HashMap<>();
//                                        map.put("contact", contact);
//                                        map.put("userNAme", name);
//                                        map.put("image", imagePath);

                                        Toast.makeText(getContext(), "YEs", Toast.LENGTH_SHORT).show();
                                        mRef.child(key).setValue(user);
//                                        mRef.child(key)
//                                                .updateChildren(map)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                });

    }

    void openGallery() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {

            imagePath = data.getData();

            binding.image.setImageURI(imagePath);
        }
    }

    public interface OnFragmentInteractionListener {
        int MENU = 0;

        // TODO: Update argument type and name
        void onEditProfileFragmentInteraction(int i);
    }
}
