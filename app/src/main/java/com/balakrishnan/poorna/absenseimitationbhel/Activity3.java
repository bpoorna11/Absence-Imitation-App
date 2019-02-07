package com.balakrishnan.poorna.absenseimitationbhel;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Activity3 extends AppCompatActivity {

    ListView lv;
    MyAdapter arrayAdapter;
    ArrayList<Contacts> result=new ArrayList<Contacts>();
    String staffno="",ename="",dep="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        lv=findViewById(R.id.listview);
        Intent in=getIntent();
        staffno=in.getStringExtra("Staff");
        ename=in.getStringExtra("ename");
        dep=in.getStringExtra("dept");
        new MyTask5().execute(staffno);
    }

    class MyTask5 extends AsyncTask<String, Void, ArrayList<Contacts>> {

        @Override
        protected ArrayList<Contacts> doInBackground(String... strings) {
            Log.e(" doInBackground"," doInBackground inside");
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            NameValuePair np1=new BasicNameValuePair("Staffno",strings[0]);
            list.add(np1);

            try{
                UrlEncodedFormEntity urlData=new UrlEncodedFormEntity(list);
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/showall");
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

                String res=InputStreamConvertor(is);
                Log.e("String res",res);
                String str=res.replaceAll("[\"]","");
                String str1=str.replaceAll("[,]","");
                String str2=str1.replace("[","");
                String str3=str2.replace("]","");
                String str4=str3.replace("\\n","\\\n");
                String str5=str4.replace("\\","");

                String[] lines = str5.split("\\n");

                Contacts con1=new Contacts();
                int k=9;
                String con="";
                for(int i=0;i<lines.length;i+=10){
                    con="";
                    for(int j=i;j<i+10;j++) {
                        con+=lines[j]+"\n";

                    }
                    String status=lines[k].substring(lines[k].indexOf(":")+2,lines[k].length());
                    if(lines[k].substring(lines[k].indexOf(":")+1,lines[k].length()).equals(" C"))
                        con1=new Contacts(con,null);
                    else if(status.equals("S"))
                        con1=new Contacts(con,getApplicationContext().getResources().getDrawable(R.drawable.cancel));
                    k+=10;
                    //con1=new Contacts(con,getApplicationContext().getResources().getDrawable(R.drawable.red));
                    result.add(con1);
                }
               // ObjectMapper ob=new ObjectMapper();
               // result=ob.readValue(res, new TypeReference<ArrayList<Contacts>>(){});
                Log.e("Result",result.toString());
            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.println(result);
            return result;
        }
        @Override
        protected void onPostExecute(final ArrayList<Contacts> result){
            // Log.e("Result onPostExecute",s);
            if(result.isEmpty()){

                customDialog("No records!","No absence Imitation records",null,"methodyes");
            }
            arrayAdapter=new MyAdapter(getApplicationContext(),result,staffno,ename,dep,result);
            lv.setAdapter(arrayAdapter);


                   // customDialog1("Cancellation Query","Are you sure you want to cancel your Absence Intimation?","methodno","methodyes");


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
    public void customDialog(String title,String message,final String cancelMethod,final String okMethod){
        final android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(this);
        builderSingle.setIcon(R.mipmap.ic_noti);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);
        builderSingle.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(okMethod.equals("methodyes"))
                    finish();

            }
        });
        builderSingle.show();
    }

}
