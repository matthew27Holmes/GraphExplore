package com.example.mp2_holmes.graphexplorer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;


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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class companiesRequest  extends AsyncTask<Void,Void, String> {

    private WeakReference<Context> contextRef;
    private  WeakReference<RecyclerView> recList;
    private  String query;


    companiesRequest(Context context, RecyclerView recyclerView, String Query) {
        contextRef = new WeakReference<>(context);
        recList = new WeakReference<>(recyclerView);
        query = Query;
        makeRequest();
    }

    private void makeRequest()
    {
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
                        nodeAdapter ca = new nodeAdapter(createList(response),contextRef.get());
                        recList.get().setAdapter(ca);
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

    private List<nodeInfo> createList( JSONObject response) {

        List<nodeInfo> result = new ArrayList<nodeInfo>();
        JSONArray Companies = null;
        try {
            Companies = response.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0; i <= Companies.length()-1; i++) {
            nodeInfo ci = new nodeInfo();
            try {
                JSONObject company = Companies.getJSONObject(i);
                Log.d("company" + String.valueOf(i),company.toString());
                ci.title = company.getString("title");
                ci.number = company.getString("company_number");
                ci.status = company.getString("company_status");
                ci.address = company.getString("address_snippet");
            } catch (JSONException e) {
                //e.printStackTrace();
                Log.d("error",e.toString());
            }
            result.add(ci);
        }
        return result;
    }

    @Override
    protected String doInBackground(Void... voids) {
        // make request here
        String Result = query;// this could be a json object
        return Result;
    }
    protected void onPostExecute(String result) {
        //Title.get().setText(result);
    }
};