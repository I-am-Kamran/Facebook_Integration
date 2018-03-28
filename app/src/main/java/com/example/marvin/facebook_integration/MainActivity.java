package com.example.marvin.facebook_integration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private TextView textView;
    private AccessTokenTracker accessTokenTracker;
    public ProfileTracker profileTracker;
    LoginButton  loginButton;
    ImageView imageView;
    FacebookCallback<LoginResult> callback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton=findViewById(R.id.fb_button);
        textView=findViewById(R.id.text);
        imageView=findViewById(R.id.image);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {
            }
        };

        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                displayMessage(currentProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                displayMessage(profile);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };

        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager,callback);
    }

    private void displayMessage(Profile profile)
    {
        if (profile!=null)
        {
            textView.setText(profile.getName());
            String url=profile.getProfilePictureUri(150,150).toString();

            Glide.with(MainActivity.this).load(url).asBitmap()
                    .into(new BitmapImageViewTarget(imageView)
                    {
                        @Override
                        protected void setResource(Bitmap resource)
                        {
                            super.setResource(resource);
                            RoundedBitmapDrawable cd= RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(),resource);
                            cd.setCircular(true);
                            imageView.setImageDrawable(cd);
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Profile profile=Profile.getCurrentProfile();
        displayMessage(profile);
    }


}
