package com.example.tradingview.APIAdapter;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIAdapter {
    private Context context; // Context is needed for Volley request queue and Toasts

    public APIAdapter(Context context) {
        this.context = context;
    }

    @NotNull
    public Object getSymbolQuoteSuspend(@Nullable String symbol) {
        return null;
    }

    public interface SymbolsListCallback {
        void onSuccess(JSONArray symbolsList);
        void onError(String errorMessage);
    }

    public interface SymbolsDataCallback {
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
                callback.onError("Failed to fetch data");
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void getSymbolQuote(final SymbolsDataCallback callback, String symbol) {
//        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=ET7CN2ILLZU2FNW8"; // Liors key
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=NIBOSC9JZJFNJG2M"; // Nitsans key
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject globalQuote = response.getJSONObject("Global Quote");
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(globalQuote); // Wrap the JSONObject into a JSONArray
                        callback.onSuccess(jsonArray); // Pass the JSONArray to the callback
                    } catch (JSONException e) {
                        callback.onError("Failed to parse data");
                        Toast.makeText(context, "Failed to parse data", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            callback.onError("Failed to fetch data");
            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        });

        queue.add(jsonObjectRequest);
    }
}