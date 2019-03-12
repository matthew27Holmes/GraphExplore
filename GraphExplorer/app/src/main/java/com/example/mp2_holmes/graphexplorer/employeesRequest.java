package com.example.mp2_holmes.graphexplorer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.example.mp2_holmes.graphexplorer.NodeGraphView.customView;


public class employeesRequest  extends AsyncTask<Void,Void, String> {

    private WeakReference<Context> contextRef;
    private WeakReference<customView> customViewRef;
    private  String CompanyNum;
    private  String RequestStatus = "";


    employeesRequest(Context context, customView cView, String Query) {
        contextRef = new WeakReference<>(context);
        customViewRef = new WeakReference<>(cView);
        CompanyNum = Query;

        makeRequest();
    }

    private void makeRequest()
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextRef.get());
        String url = "https://api.companieshouse.gov.uk/company/"+ CompanyNum +"/officers?q=";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        RequestStatus = "finished successfully ";
                        createNodeGraph(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        RequestStatus = "error";
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

    private void createNodeGraph(JSONObject response)
    {
        try {
            JSONArray officers = response.getJSONArray("items");
            //int m_NumNode = employees.length();
            Log.d("officers",officers.toString());
            customViewRef.get().setOfficers(officers);// change function in custome view to use post in vaildate
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //loop through employes here
            // draw circle
            //connect circles
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.d("response",RequestStatus);
        return RequestStatus;
    }
    protected void onPostExecute(String result) {
    }
};