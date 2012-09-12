package com.virtica.dplayer;



import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;



public class DropboxAuthActivity extends Activity {
	
    final static private String APP_KEY = "y1y5e0jrint1cw8";
   
    final static private String APP_SECRET = "k2ktl37m3y33fle";
  
    final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

    private DropboxAPI<AndroidAuthSession> mDBApi;
    
    private ProgressDialog mProgress;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please wait...");
        mProgress.setTitle("Signing in");
        
        
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mProgress.show();
        mDBApi.getSession().startAuthentication(DropboxAuthActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                
                mDBApi.getSession().finishAuthentication();
                
                
                AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();
                
                SharedPreferences sp = getSharedPreferences("drop_box_auth", MODE_PRIVATE);
                Editor edit = sp.edit();
                edit.putString("access_token", tokens.key);
                edit.putString("access_token_secret", tokens.secret);
                edit.commit();
                
               
                TextView accessToken = (TextView) findViewById(R.id.access_token);
                TextView accessTokenSecret = (TextView) findViewById(R.id.access_token_secret);
                accessToken.setText(tokens.key);
                accessTokenSecret.setText(tokens.secret);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
            
           
        }
        
    }
}

