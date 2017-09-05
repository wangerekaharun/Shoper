package com.example.joseph.shoper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignIn extends AppCompatActivity {
    public static final String BASE_URL="";
    Toolbar mToolBar;
    AVLoadingIndicatorView mAvi;
    LinearLayout mSignInForm;
    RelativeLayout mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mAvi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        mSignInForm= (LinearLayout) findViewById(R.id.sign_in_form);
        mLoadingIndicator= (RelativeLayout) findViewById(R.id.loading_indicator);
        setSupportActionBar(mToolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void signIn(View view){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mSignInForm.setVisibility(View.GONE);
        mAvi.show();
        startActivity(new Intent(view.getContext(),Shops.class));
    }

    public void signUp(View view) {
        startActivity(new Intent(view.getContext(),SignUp.class));
    }
    public Map<String,String> signInUser(Map<String,String> userForm){
        String email=userForm.get("email");
        String password=userForm.get("password");
        Map<String,String> statusMap=new HashMap<>();
        OkHttpClient okHttpClient=new OkHttpClient();
        String json="{\"email\":\""+email+"\",\"password\":\""+password+"\"}";
        Request request=new Request.Builder().url(BASE_URL).addHeader("json",json).build();
        Response response=null;
        try {
            response=okHttpClient.newCall(request).execute();
            String responseBody=response.body().string();
            //// TODO: 9/4/17 convert response body to json and extract the status
            JSONObject jsonObject=new JSONObject(responseBody);
            String status=jsonObject.getString("status");
            String reason=jsonObject.getString("reason");
            String authorisationToken=response.header("token");
            statusMap.put("status",status);
            statusMap.put("reason",reason);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return statusMap;
    }
    public class MyBackgroundTask extends AsyncTask<Map<String,String>,Void,Map<String,String>>{

        @Override
        protected Map<String, String> doInBackground(Map<String, String>... maps) {
            return signInUser(maps[0]);
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
//            super.onPostExecute(stringStringMap);
            startActivity(new Intent(SignIn.this,Shops.class));

        }
    }
}