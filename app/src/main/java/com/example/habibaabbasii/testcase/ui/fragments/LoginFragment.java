package com.example.habibaabbasii.testcase.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.habibaabbasii.testcase.Constants;
import com.example.habibaabbasii.testcase.R;
import com.example.habibaabbasii.testcase.databinding.FragmentLoginBinding;
import com.example.habibaabbasii.testcase.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    FragmentLoginBinding binding;

    //AnimationUtils animationUtils;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    ProgressDialog progressDialog;
Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference(Constants.USERS);

        progressDialog = new ProgressDialog(getContext());


        setUpListeners();
//        animationUtils= AnimationUtils.loadAnimation(LoginFragment.this,R.anim.slide_in_left);
//        binding.login.startAnimation(animationUtils);
        return binding.getRoot();
    }


    private void setUpListeners() {

        binding.login.setOnClickListener(this);
        binding.signup.setOnClickListener(this);
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



        if (view == binding.login) {

            progressDialog.setMessage("Signing in");
            progressDialog.show();

            if (binding.email.length() > 0 &&
                    binding.password.length() > 0) {

                login(binding.email.getText().toString(), binding.password.getText().toString());
            } else {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        } else if (view == binding.signup) {
            mListener.onLoginFragmentInteraction(OnFragmentInteractionListener.SIGNUP);
        }
    }

    private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //checking user type
                            mRef.orderByChild("uId")
                                    .equalTo(mAuth.getCurrentUser().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            progressDialog.dismiss();
                                            if (dataSnapshot.exists()) {

                                                Iterable<DataSnapshot> child = dataSnapshot.getChildren();
                                                UserModel user = null;
                                                for (DataSnapshot ch : child)
                                                    user = ch.getValue(UserModel.class);

                                                System.out.println(dataSnapshot.getValue());

                                                mListener.onLoginFragmentInteraction(OnFragmentInteractionListener.HOME);

                                            } else {
                                                Toast.makeText(getContext(), "User doesnot exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            progressDialog.dismiss();
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public interface OnFragmentInteractionListener {
        int HOME = 0, SIGNUP = 1;

        // TODO: Update argument type and name
        void onLoginFragmentInteraction(int i);
    }
}
