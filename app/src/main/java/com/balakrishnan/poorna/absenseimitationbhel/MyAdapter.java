package com.balakrishnan.poorna.absenseimitationbhel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Contacts> {
    String staff="",ename2="",dep2="",emailB_Number="",mobile="",abtype2="",abday2="",from2="",to2="",visitp2="",cancel="not reached";
    ArrayList<Contacts> copyresult=new ArrayList<Contacts>();
    private Context context;
    private ArrayList<Contacts> list;
    public MyAdapter(Context context,ArrayList<Contacts> list,String staffno,String ename,String dep,ArrayList<Contacts> copyres){
        super(context, R.layout.mylayout,list);
        this.context=context;
        this.list=list;
        staff=staffno;
        ename2=ename;
        dep2=dep;
        copyresult.addAll(copyres);
    }

    View curr;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView =inflater.inflate(R.layout.mylayout,parent,false);
        final TextView textView=(TextView)rowView.findViewById(R.id.textView15);
        textView.setText(list.get(position).getName());
        Button button=(Button)rowView.findViewById(R.id.button5);
        button.setBackground(list.get(position).getId());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                curr=v;

                String[] lines = textView.getText().toString().split("\\n");
                //String status=lines[9];
               // String status2=status.substring(status.indexOf(":")+2,status.length());
                String abtype=lines[0];
                abtype2=abtype.substring(abtype.indexOf(":")+2,abtype.length());
                String visitp=lines[1];
                visitp2=visitp.substring(visitp.indexOf(":")+2,visitp.length());
                String abday=lines[2];
                abday2=abday.substring(abday.indexOf(":")+2,abday.length());
                String from=lines[3];
                from2=from.substring(from.indexOf(":")+2,from.length());
                String to=lines[4];
                to2=to.substring(to.indexOf(":")+2,to.length());

                String serialno=textView.getText().toString().substring(0,textView.getText().toString().indexOf(")"));
                new MyTask66().execute(serialno);
                 lines[9]="  Status: C";
                 String text="";
                for(int i=0;i<10;i++){
                    text+=(lines[i]+"\n");

                }
                textView.setText(text);
            }
        });
        return rowView;

    }

    class MyTask66 extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.e(" doInBackground"," doInBackground inside");
            String result=null;
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            NameValuePair np1=new BasicNameValuePair("SerialNo",strings[0]);
            list.add(np1);

            try{
                UrlEncodedFormEntity urlData=new UrlEncodedFormEntity(list);
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/update");
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
            Log.e("Result onPostExecute",s);
            cancel=s;
            Button btn=(Button)curr;

            if(s.equals("Intimation cancelled")){

                new MyTask33().execute(staff);
                Toast.makeText(getContext(),cancel,Toast.LENGTH_LONG).show();
               // btn.setEnabled(false);


            }
           else if(s.equals("Already cancelled")){

                Toast.makeText(getContext(),cancel,Toast.LENGTH_LONG).show();

               // Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();

            }

        }
    }
    class MyTask33 extends AsyncTask<String,String,String> {//getting email and number of boss

        @Override
        protected String doInBackground(String... strings) {
            Log.e(" doInBackground"," doInBackground inside");
            String result=null;
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            NameValuePair np1=new BasicNameValuePair("Staffno",strings[0]);
            list.add(np1);

            try{
                UrlEncodedFormEntity urlData=new UrlEncodedFormEntity(list);
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/boss");
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
            Log.e("Result onPostExecute",s);

            emailB_Number=s;

            mobile=emailB_Number.substring(emailB_Number.indexOf(" ")+1,emailB_Number.length());

            String LONG_TEXT="Sir/Madam,\nMy absence intimation to proceed to "+visitp2+
                    " on "+abtype2+" for "+abday2+" days,from "+from2+" to "+to2+
                    " is cancelled."+
                    "\nRegards\n"+ename2+"\n"+dep2;
            SmsManager sm = SmsManager.getDefault();
            ArrayList<String> parts =sm.divideMessage(LONG_TEXT);
            sm.sendMultipartTextMessage(mobile,null, parts,null, null);





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



}



