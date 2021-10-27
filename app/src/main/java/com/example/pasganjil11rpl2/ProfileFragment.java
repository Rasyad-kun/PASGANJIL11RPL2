package com.example.pasganjil11rpl2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    String username, email;
    Bundle bundle;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    TextView txt_username, txt_email;
    ImageView editprofile1, editprofile2, profilepic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(getContext()).enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        txt_username = v.findViewById(R.id.txt_username);
        txt_email = v.findViewById(R.id.txt_email);
        profilepic = v.findViewById(R.id.profilepic);

        bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            username = bundle.getString("username");
            email = bundle.getString("email");
        }

        txt_username.setText(username);
        txt_email.setText(email);

        return v;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInresult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            txt_username.setText(account.getDisplayName());
            txt_email.setText(account.getEmail());

            Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profilepic);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if(opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInresult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInresult(googleSignInResult);
                }
            });
        }
    }
}
