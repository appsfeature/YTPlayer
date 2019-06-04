//package com.droidhelios.ytplayer.subscribe;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.droidhelios.ytplayer.R;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//public class GoogleSignInActivity2 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
//
//    private static final int RC_SIGN_IN = 1212;
//    protected static final String USER_EMAIL = "userEmailId";
//    private FirebaseAuth mAuth;
//    private GoogleApiClient mGoogleApiClient;
//    private SignInButton btnSignIn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_subscribe);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        btnSignIn = (SignInButton) findViewById(R.id.B_subscribe);
//        // Customizing G+ button
//        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
//
//        findViewById(R.id.B_subscribe).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and send to next activity accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            startActivity(new Intent(this, YouTubeSubscribeActivity.class));
//            finish();
//        }
//
//    }
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }else {
//            // check internet connection, display a message to the user.
////                Log.d("Error",result.getStatus().getStatusMessage());
//            Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            firebaseAuthWithGoogle(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }
//
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(GoogleSignInActivity2.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
//    }
//
//
//    private void updateUI(FirebaseUser currentUser) {
//        if (currentUser != null) {
//            startActivity(new Intent(this, YouTubeSubscribeActivity.class).putExtra(USER_EMAIL, currentUser.getEmail()));
//            finish();
//        }
//    }
//
//}
