package com.balakrishnan.poorna.absenseimitationbhel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText ed1,ed9;
TextView tv12;
Button b7,b8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv12=findViewById(R.id.textView12);
        if(!isNetworkAvailable(getApplicationContext())){
            customDialog("Connection error!","No internet connection,Please check your connection",null,"ok");


        }
        if(isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(),"Connected to internet",Toast.LENGTH_SHORT).show();
            }
        ed1=findViewById(R.id.editText);
        ed1.requestFocus();
        ed9=findViewById(R.id.editText9);
        b7=findViewById(R.id.button7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().equals("") && ed9.getText().toString().equals(""))
                {
                    tv12.setText("Staffno or password is empty");
                    tv12.setTextColor(Color.DKGRAY);
                }
                else if(ed1.getText().toString().equals("")){
                    tv12.setText("Staffno cannot be empty");
                    tv12.setTextColor(Color.DKGRAY);
                }
                else if(ed9.getText().toString().equals("")){
                    tv12.setText("Password cannot be empty");
                    tv12.setTextColor(Color.DKGRAY);
                }
                else {
                    new MyTask3().execute(ed1.getText().toString(), ed9.getText().toString());
                }
            }
        });
       // b8=findViewById(R.id.button8);
      /*  b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });*/
    }
    public void customDialog(String title,String message,final String cancelMethod,final String okMethod){
        final android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(this);
        builderSingle.setIcon(R.mipmap.ic_warn);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);
      /*  builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(okMethod.equals("ok")){
                    finish();
                }
            }
        });
        builderSingle.show();
    }
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    class MyTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.e(" doInBackground"," doInBackground inside");
            String result="no result";

            try{
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpGet hp=new HttpGet("http://192.168.43.31:9090/loginpage/login/logme/poo2");

                Log.e(" httpget"," httpget url set");
                HttpResponse hr=hc.execute(hp);
                Log.e(" httpResponse"," httpresponse get");
                StatusLine sl=hr.getStatusLine();
                Log.e("StatusLLine","StatusLine check");
                int codestatus=sl.getStatusCode();
                Log.e("code status",""+codestatus);
                HttpEntity he=hr.getEntity();
                Log.e(" httpEntity"," httpEntity got");
                InputStream is=he.getContent();
                Log.e(" getcontent","got content");
                result=InputStreamConvertor(is);
                Log.e("Result",result);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(result);
            return result;
        }
        @Override
        protected void onPostExecute(String s){
            Log.e("Result onPostExecute",s);
            tv12.setText(s);

        }
    }
    public String InputStreamConvertor(InputStream is) throws IOException {
        InputStreamReader in=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(in);
        StringBuffer sb=new StringBuffer();
        String s="";
        while ((s=br.readLine())!=null){
            Log.e("Result in loop",s);
            sb.append(s);
        }
        return sb.toString();
    }
    class MyTask3 extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.e(" doInBackground"," doInBackground inside");
            String result="Server not responding";
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            NameValuePair np1=new BasicNameValuePair("Staffno",strings[0]);
            NameValuePair np2=new BasicNameValuePair("Password",strings[1]);
            list.add(np1);
            list.add(np2);
            try{
                UrlEncodedFormEntity urlData=new UrlEncodedFormEntity(list);
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/find");
                Log.e(" httpget"," httpget url set");
                hp.setEntity(urlData);
                HttpResponse hr=hc.execute(hp);
                Log.e(" httpResponse"," httpresponse get");
                StatusLine sl=hr.getStatusLine();
                Log.e("StatusLLine","StatusLine check");
                int codestatus=sl.getStatusCode();
                Log.e("code status",""+codestatus);
                HttpEntity he=hr.getEntity();
                Log.e(" httpEntity"," httpEntity got");
                InputStream is=he.getContent();
                Log.e(" getcontent","got content");
                result=InputStreamConvertor(is);
                Log.e("Result",result);
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s){
          //  Log.e("Result onPostExecute",s);

            if(s.contains("Server")){
                //tv12.setTextColor(Color.RED);
                customDialog("Server Error","Server not responding,Please try again later",null,"ok");
            }else{
                tv12.setText(s);

             if(tv12.getText().toString().contains("password")){

                tv12.setTextColor(Color.RED);
            }
            else if(tv12.getText().toString().contains("succesfully")){
                tv12.setTextColor(Color.BLUE);
                Intent in=new Intent(getApplicationContext(),Activity2.class);
                in.putExtra("Staff",ed1.getText().toString());
                startActivity(in);
            }
            ed1.setText("");
            ed9.setText("");
        }
        }
    }
}
