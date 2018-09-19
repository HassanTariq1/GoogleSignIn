package com.itpvt.googlesignin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    TextView Name, Email;
    private Button  signout;
    private SignInButton signin;

    ImageView imgg;
    private LinearLayout layout;

    GoogleApiClient google;
     private  static  final int REQ= 9001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

layout=(LinearLayout) findViewById(R.id.linear);
Name=(TextView) findViewById(R.id.name);
Email=(TextView) findViewById(R.id.email);
signout=(Button) findViewById(R.id.logbtn);
signin=(SignInButton) findViewById(R.id.sign_in_button);
imgg=(ImageView) findViewById(R.id.img);

signin.setOnClickListener(this);
signout.setOnClickListener(this);
layout.setVisibility(View.GONE);
        GoogleSignInOptions googlee= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        google= new GoogleApiClient.Builder(    this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API,googlee).build();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.sign_in_button:
    SignIn();
break;

            case R.id.logbtn:
                SignOut();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void SignIn(){
        Intent i = Auth.GoogleSignInApi.getSignInIntent(google);
        startActivityForResult(i,REQ );

    }
    private void  SignOut(){

        Auth.GoogleSignInApi.signOut(google).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

updateUI(false);

            }
        });


    }
    private  void  handleResult(GoogleSignInResult result){

        if(result.isSuccess()){

            GoogleSignInAccount account= result.getSignInAccount();
            String name= account.getDisplayName();
            String email=account.getEmail();
            String img= account.getPhotoUrl().toString();

Name.setText(name);
Email.setText(email);
            Glide.with(this).load(img).into(imgg);
            updateUI(true);

        }
        else {

            updateUI(false);
        }


    }
    private void  updateUI(boolean isLogin){

if(isLogin){
    layout.setVisibility(View.VISIBLE);
    signin.setVisibility(View.GONE);

}
else{


    layout.setVisibility(View.GONE);
    signin.setVisibility(View.VISIBLE);


}

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
           GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           handleResult(result);

        }
    }

}
