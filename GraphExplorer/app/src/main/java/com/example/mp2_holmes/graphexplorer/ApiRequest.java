package com.example.mp2_holmes.graphexplorer;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class ApiRequest extends AsyncTask <Void, Void, String>{

    private WeakReference<TextView> Title;
    private WeakReference<Context> contextRef;
    private  String query;



    ApiRequest(Context context,TextView tv, String Query) {
        Title = new WeakReference<>(tv);
        contextRef = new WeakReference<>(context);
        query = Query;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextRef.get());
        String url = "https://api.companieshouse.gov.uk/search/companies?q="+query;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        Title.get().setText(response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders()throws AuthFailureError
            {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "1bHYk-4PgtFfuT8HKjecJ2rWGpEJg22f8656497e");

                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(getRequest);


    }

    @Override
    protected String doInBackground(Void... voids) {
        // make request here
        String Result = Request(query);
        return Result;
    }
     String Request(String query)
     {


        return query;
     }
    protected void onPostExecute(String result) {
        Title.get().setText(result);
    }


};

