package com.example.collegeappadmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.collegeappadmin.R;
import com.example.collegeappadmin.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.proceesDiloag_Title));
        dialog.setMessage(getString(R.string.proceesDiloag_descr));
        dialog.setCancelable(false);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.rollTextMain.getText().toString().isEmpty())
                {
                    binding.rollTextMain.setError("Please Enter your roll Number :-(");
                    return ;
                }
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                signIn();
                //Gmail SignIn
            }
        });
        
    }
    private void signIn() {


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                String email = account.getEmail();
                if(email.contains("@iiitmanipur"))
                {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
                else
                {
                    mGoogleSignInClient.signOut();
                    Toast.makeText(this, "Please Use college Official Gmail ID", Toast.LENGTH_SHORT).show();
                    throw new Exception("Wrong Id");
                }


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                dialog.dismiss();
                Log.w("Aryan", "Google sign in failed", e);
            }
            catch (Exception e)
            {
                dialog.dismiss();
            }

        }
    }



    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            dialog.dismiss();

                            Intent intent = new Intent(MainActivity.this,EmptyFragmentActivity.class);
                            intent.putExtra("rollNum",binding.rollTextMain.getText().toString());
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Aryan", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(MainActivity.this,bottom_ShowDetailActivity.class));
            finishAffinity();
        }

    }
}