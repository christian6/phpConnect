package com.example.phpconnect;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class main extends Activity {

	 TextView t;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t  = (TextView) findViewById(R.id.text1);
        //String uri = "http://172.16.0.80/app-android/app.php";
        new httpcarga().execute("http://172.16.0.80/app-android/app.php");
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @SuppressWarnings("finally")
	private static String convertStreamToString(InputStream is) {
    	/*
    	* To convert the InputStream to String we use the
    	* BufferedReader.readLine() method. We iterate until the BufferedReader
    	* return null which means there's no more data to read. Each line will
    	* appended to a StringBuilder and returned as String.
    	*/
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	StringBuilder sb = new StringBuilder();

    	String line = null;
    	try {
	    	while ((line = reader.readLine()) != null) {
	    		sb.append(line + "\n");
	    	}
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} finally {
		    	try {
		    		is.close();
		    	} catch (final IOException e) {
		    		e.printStackTrace();
		    	}
		    	return sb.toString();
	    	}
    }
    	
     public String httpstatus(String url){
    	 HttpClient client = new DefaultHttpClient();
    	 HttpPost post = new HttpPost(url);
    	 String result = "";
    	 try {
	    	 HttpResponse response = client.execute(post);
	    	 StatusLine statusLine = response.getStatusLine();
	    	 System.err.println(statusLine.getStatusCode());
	    	 if (statusLine.getStatusCode() == 200) {
	    	   HttpEntity entity = response.getEntity();
	    	   InputStream instream = entity.getContent();
	    	   result = convertStreamToString(instream);
	    	   instream.close();
	    	 } 
	    	 return result;
    	 } catch (Exception e) {
    	   result = "ERROR "+e.getMessage();
    	 }
		return result;
    }
    
    class httpcarga extends AsyncTask<String,String,String>{
    	protected void onPreExecute(){
    		// message pre load
    		t.setText("Cargando Http");
    	}
    	
    	protected String doInBackground(String... urls){
    		return httpstatus(urls[0]);
    	}
    	
    	protected void onPostExecute(String result){
    		t.setText( result );
    	}

    }
    
}


