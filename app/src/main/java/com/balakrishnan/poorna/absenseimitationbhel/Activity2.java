package com.balakrishnan.poorna.absenseimitationbhel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class Activity2 extends AppCompatActivity implements View.OnClickListener{
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> result=new ArrayList<>();
     String staff="no ress";
     String ename="no name";
     String dep="";
     String validdate="";
SmsManager smsManager;
String date1="",date2="";
Button b1,b2,b3,b4,b9;
Spinner spinner1,spinner2;
EditText ed2,ed3,ed4,ed5,ed6,ed7,ed8;
TextView tv13,tv14;
int yearC,monthC,dayC,mHour,mMinute;
String leaveType="",contactPerson="";
String emailB_Number="no email";
static final int Dialogid=0;
static final int Dialogid2=1;
    final Calendar cal=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        yearC=cal.get(Calendar.YEAR);
        monthC=cal.get(Calendar.MONTH);
        dayC=cal.get(Calendar.DAY_OF_MONTH);
        showDailogOnClick();
        tv13=findViewById(R.id.textView13);
        tv14=findViewById(R.id.textView14);
        Intent in=getIntent();
        staff=in.getStringExtra("Staff");
        new MyTask11().execute(staff);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        new MyTask22().execute(staff);
        b9=findViewById(R.id.button9);//submit to email,msg and insert
        b9.setTextColor(Color.WHITE);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new MyTask33().execute(staff);
                //new MyTask44().execute(staff,leaveType,ed2.getText().toString(),ed3.getText().toString(),ed4.getText().toString(),ed5.getText().toString(),ed6.getText().toString(),ed7.getText().toString(),ed8.getText().toString(),contactPerson);
                if(ed2.getText().toString().equals("") || ed3.getText().toString().equals("") || ed4.getText().toString().equals("") || ed5.getText().toString().equals("")
                        || ed6.getText().toString().equals("") || ed7.getText().toString().equals("") || ed8.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_LONG).show();
                }
                else {
                    customDialog("Absence Imitation Query", "Do you want to send an email and message to your head?", "methodno", "methodyes");
                }
            }
        });
        b3=findViewById(R.id.button3);//logout
        b3.setTextColor(Color.WHITE);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog("Logout", "Are you sure you want to logout?", "methodcancel", "methodlogout");

            }
        });

        ed2=findViewById(R.id.editText2);
        ed2.requestFocus();
        ed3=findViewById(R.id.editText3);
        ed4=findViewById(R.id.editText4);
        ed5=findViewById(R.id.editText5);
        ed6=findViewById(R.id.editText6);
        ed7=findViewById(R.id.editText7);
        ed8=findViewById(R.id.editText8);
        addListenerOnSpinnerItemSelection();
        b4=findViewById(R.id.button4);//list
        b4.setTextColor(Color.WHITE);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),Activity3.class);
                in.putExtra("Staff",staff);
                in.putExtra("ename",ename);
                in.putExtra("dept",dep);
                startActivity(in);
            }
        });

    }
    private void noMethod(){
        ed2.requestFocus();
        ed2.setText("");
        ed3.setText("");
        ed4.setText("");
        ed5.setText("");
        ed6.setText("");
        ed7.setText("");
        ed8.setText("");
        }
        private void methodcancel(){
            //nothing
        }
    private void yesMethod(){
        new MyTask55().execute(staff,ed4.getText().toString(),ed5.getText().toString());
            }

    private void logout(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void customDialog(String title,String message,final String cancelMethod,final String okMethod){
        final android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(this);
        builderSingle.setIcon(R.mipmap.ic_noti);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);
        builderSingle.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cancelMethod.equals("methodno"))
                        noMethod();
                else if(cancelMethod.equals("methodcancel"))
                        methodcancel();
                    }
        });
        builderSingle.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(okMethod.equals("methodyes"))
                    yesMethod();
                else if(okMethod.equals("methodlogout"))
                    logout();
            }
        });
        builderSingle.show();
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
    class MyTask11 extends AsyncTask<String,String,String> {

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
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/name");
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
            tv13.setText(s);
            ename=s.substring(s.indexOf(" ")+1,s.indexOf("D"));
            dep=s.substring(s.indexOf("D"),s.length());
        }
    }
    class MyTask22 extends AsyncTask<String, Void, ArrayList<String>>  {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e(" doInBackground"," doInBackground inside");
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            NameValuePair np1=new BasicNameValuePair("Staffno",params[0]);
            list.add(np1);

            try{
                UrlEncodedFormEntity urlData=new UrlEncodedFormEntity(list);
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/showdep");
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
                ObjectMapper ob=new ObjectMapper();
                result=ob.readValue(res, new TypeReference<ArrayList<String>>(){});
                //result.add(InputStreamConvertor(is));

                Log.e("Result",result.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(result);
            return result;
        }
        @Override
        protected void onPostExecute(ArrayList<String> result){
            // Log.e("Result onPostExecute",s);
            arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,result);
           // arrayAdapter.add("Poorna  22");
            spinner2.setAdapter(arrayAdapter);
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
           // tv14.setText(s);
            emailB_Number=s;
            String email=emailB_Number.substring(0,emailB_Number.indexOf(" "));
           // tv14.setText(email);
            String mobile=emailB_Number.substring(emailB_Number.indexOf(" ")+1,emailB_Number.length());
           // tv14.setText(mobile);

           String LONG_TEXT="Sir/Madam,\nI shall be proceeding to "+ed2.getText()+
                   " on "+leaveType+" for "+ed3.getText()+" days,from "+ed4.getText()+" to "+ed5.getText()+
                   ".My departure and arrival details are "+ed6.getText()+" and "+ed7.getText()+" respectively,purpose of leave is to attend a "+ed8.getText()+
                   "\nSubmitted for kind approval.\nRegards\n"+ename+"\n"+dep;
            SmsManager sm = SmsManager.getDefault();
            ArrayList<String> parts =sm.divideMessage(LONG_TEXT);
            sm.sendMultipartTextMessage(mobile,null, parts,null, null);

               // Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_LONG).show();

        }
    }
    class MyTask44 extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.e(" doInBackground"," doInBackground inside");
            String result=null;
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            NameValuePair np1=new BasicNameValuePair("Staffno",strings[0]);
            NameValuePair np2=new BasicNameValuePair("AbsenseType",strings[1]);
            NameValuePair np3=new BasicNameValuePair("VisitPlace",strings[2]);
            NameValuePair np4=new BasicNameValuePair("AbsenseDays",strings[3]);
            NameValuePair np5=new BasicNameValuePair("Fromdt",strings[4]);
            NameValuePair np6=new BasicNameValuePair("Todt",strings[5]);
            NameValuePair np7=new BasicNameValuePair("Departure",strings[6]);
            NameValuePair np8=new BasicNameValuePair("Arrival",strings[7]);
            NameValuePair np9=new BasicNameValuePair("Purpose",strings[8]);
            NameValuePair np10=new BasicNameValuePair("ContactPerson",strings[9]);
            NameValuePair np11=new BasicNameValuePair("Status",strings[10]);
            list.add(np1);
            list.add(np2);
            list.add(np3);
            list.add(np4);
            list.add(np5);
            list.add(np6);
            list.add(np7);
            list.add(np8);
            list.add(np9);
            list.add(np10);
            list.add(np11);
            try{

                UrlEncodedFormEntity urlData=new UrlEncodedFormEntity(list);
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/insert");
                Log.e(" httppost"," httppost url set");
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


        }
    }
    class MyTask55 extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.e(" doInBackground"," doInBackground inside");
            String result=null;
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            NameValuePair np1=new BasicNameValuePair("Staffno",strings[0]);
            NameValuePair np2=new BasicNameValuePair("Fromdt",strings[1]);
            NameValuePair np3=new BasicNameValuePair("Todt",strings[2]);
            list.add(np1);
            list.add(np2);
            list.add(np3);
            try{
                UrlEncodedFormEntity urlData=new UrlEncodedFormEntity(list);
                HttpClient hc=new DefaultHttpClient();
                Log.e(" httpclient ","http client set");
                HttpPost hp=new HttpPost("http://192.168.43.31:9090/loginpage/login/logme/date");
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
           // tv13.setText(s);
            validdate=s;
            if(validdate.equals("Valid Date") || validdate.equals("Valid entry")) {
                new MyTask33().execute(staff);
                new MyTask44().execute(staff, leaveType, ed2.getText().toString(), ed3.getText().toString(), ed4.getText().toString(), ed5.getText().toString(), ed6.getText().toString(), ed7.getText().toString(), ed8.getText().toString(), contactPerson,"S");
            }
            else if(validdate.equals("Not Valid Date")){
                //Toast.makeText(getApplicationContext(),"You already have an entry",Toast.LENGTH_LONG).show();
                tv14.setTextColor(Color.RED);
                tv14.setText("*You already have an entry");
                ed2.requestFocus();
                ed2.setText("");
                ed3.setText("");
                ed4.setText("");
                ed5.setText("");
                ed6.setText("");
                ed7.setText("");
                ed8.setText("");
            }
        }
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
       // spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener1());
    }
    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                leaveType= parent.getItemAtPosition(pos).toString();
               // Toast.makeText(parent.getContext(),
                               //"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                            //  Toast.LENGTH_SHORT).show();
      }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
    public class CustomOnItemSelectedListener1 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                contactPerson=parent.getItemAtPosition(pos).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
    @Override
    public void onClick(View v) {


    }


    public  void showDailogOnClick(){
        b1=findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialogid);
            }
        });
        b2=findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(Dialogid2);

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Dialog onCreateDialog(int id){
        if(id==Dialogid)
            return new DatePickerDialog(this,dpickerListner,yearC,monthC,dayC);
        else if(id==Dialogid2){
            return new DatePickerDialog(this,dpickerListner2,yearC,monthC,dayC);
        }
    return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListner=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            yearC=year;
            monthC=month+1;
            dayC=dayOfMonth;
            //Toast.makeText(getApplicationContext(),dayC+"/"+monthC+"/"+yearC,Toast.LENGTH_LONG).show();
               // ed4.setText("  "+dayC+"/"+monthC+"/"+yearC);
                date1=dayC+"/"+monthC+"/"+yearC;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = mdformat.format(calendar.getTime());
            try {
                Date currdate = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
                Date pickdate = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
                if(pickdate.compareTo(currdate)>=0){
                     ed4.setText("  "+dayC+"/"+monthC+"/"+yearC);
                     time1();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Choose a valid initial date",Toast.LENGTH_LONG).show();
                    ed4.setText("");
                    ed4.requestFocus();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    };
    private DatePickerDialog.OnDateSetListener dpickerListner2=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearC=year;
            monthC=month+1;
            dayC=dayOfMonth;
            //Toast.makeText(getApplicationContext(),dayC+"/"+monthC+"/"+yearC,Toast.LENGTH_LONG).show();
           // ed5.setText("  "+dayC+"/"+monthC+"/"+yearC);
            date2=dayC+"/"+monthC+"/"+yearC;
            try {
                Date fromdate = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
                Date pickdate = new SimpleDateFormat("dd/MM/yyyy").parse(date2);
                if(pickdate.compareTo(fromdate)>0){
                    ed5.setText("  "+dayC+"/"+monthC+"/"+yearC);
                    time2();
                }
                else{

                    Toast.makeText(getApplicationContext(),"Choose a valid final date",Toast.LENGTH_LONG).show();
                    ed5.setText("");
                    ed5.requestFocus();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    };
public void time1() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        ed4.setText(date1+"  "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    public void time2(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        ed5.setText(date2+"  "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
