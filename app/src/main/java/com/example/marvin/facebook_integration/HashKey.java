package com.example.marvin.facebook_integration;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * Created by MARVIN on 28-03-2018.
 */

public class HashKey extends Application
{
    @Override
    public void onCreate() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.marvin.facebook_integration",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e)
        {

        }
        super.onCreate();
    }
}
