package com.iybus.busnotification;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by intern on 6/11/13.
 */
public class RestTask extends AsyncTask<String, String, String> {
    private static String TRANSLOC = "http://api.transloc.com/1.1/";
    private MainActivity myContext;

    public RestTask(MainActivity context) {
        myContext= context;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        String endpoint = TRANSLOC + uri[0];
        try {
            response = httpclient.execute(new HttpGet(endpoint));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try{
            JSONObject resultObj= new JSONObject(result);
            TextView first = (TextView) myContext.findViewById(R.id.textView);
            JSONObject data = (JSONObject) resultObj.get("data");
            Iterator routeIt = data.keys();
            ArrayList<String> routeList = new ArrayList<String>();
            while(routeIt.hasNext()){
                JSONArray agency= (JSONArray) data.get(routeIt.next().toString());
                for(int i=0; i<agency.length(); i++){
                    JSONObject route = (JSONObject) agency.get(i);
                    routeList.add(route.getString("long_name")+ " " + route.getString("route_id"));
                }
            }

            first.setText(TextUtils.join(",", routeList));
        } catch (JSONException e){
            e.printStackTrace();
        }
        TextView text = (TextView) myContext.findViewById(R.id.textView2);
        text.setText(result);
        //Do anything with response..
    }


}
