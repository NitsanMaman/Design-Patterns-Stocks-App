package com.example.tradingview.APIAdapter;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

public class APIAdapter {
    private Context context; // Context is needed for Volley request queue and Toasts

    public APIAdapter(Context context) {
        this.context = context;
    }

    public interface SymbolsListCallback {
        void onSuccess(JSONArray symbolsList);
        void onError(String errorMessage);
    }

    public void getSymbolsList(final SymbolsListCallback callback) {
        String url = "https://api.iex.cloud/v1/data/core/REF_DATA?token=sk_f88d90be0c5b4a0cba93ba7bbddc3791&filter=symbol,name";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Use the callback to return the response
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Use the callback to pass the error message
                callback.onError("Failed to fetch data");
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }
}