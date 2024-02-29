package com.example.tradingview

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tradingview.APIAdapter.APIAdapter
import com.example.tradingview.APIAdapter.APIAdapter.SymbolsListCallback
import com.example.tradingview.SingletonFileManager.SingletonFileManager
import org.json.JSONArray
import java.util.Locale


class StocksList : ComponentActivity() {
    private var currencyRV: RecyclerView? = null
    private var searchEdt: EditText? = null
    private var currencyModalArrayList: ArrayList<CurrencyModal>? = null
    private var currencyRVAdapter: CurrencyRVAdapter? = null
    private var loadingPB: ProgressBar? = null
    private var apiAdapter: APIAdapter = APIAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stockslist) // Ensure you have activity_main.xml in your layout resources

        searchEdt = findViewById(R.id.idEdtCurrency)
        loadingPB = findViewById(R.id.idPBLoading)
        currencyRV = findViewById(R.id.idRVcurrency)
        currencyModalArrayList = ArrayList()
        currencyRVAdapter = CurrencyRVAdapter(currencyModalArrayList, this, object : CurrencyRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val symbol = currencyModalArrayList!![position].symbol
                // Assuming FileManager has been correctly instantiated and set up for Kotlin usage
                SingletonFileManager.getInstance().appendToFile(applicationContext, symbol)
                // Proceed with any other action you want to follow the click
            }
        })
        currencyRV?.layoutManager = LinearLayoutManager(this)
        currencyRV?.adapter = currencyRVAdapter

        populateList()

        searchEdt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }


    private fun filter(text: String) {
        val filteredList = ArrayList<CurrencyModal>()
        currencyModalArrayList?.filterTo(filteredList) {
            it.name.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No currency found..", Toast.LENGTH_SHORT).show()
        } else {
            currencyRVAdapter?.filterList(filteredList)
        }
    }

    //    private fun getData() {
//        val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
//
//        val queue: RequestQueue = Volley.newRequestQueue(this)
//
//        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
//            Response.Listener { response ->
//                loadingPB?.visibility = View.GONE
//                try {
//                    val dataArray = response.getJSONArray("data")
////                    val filteredSymbols = listOf("BTC", "ETH", "XRP", "SOL")
//                    for (i in 0 until dataArray.length()) {
//                        val dataObj = dataArray.getJSONObject(i)
//                        val symbol = dataObj.getString("symbol")
////                        if (symbol in filteredSymbols) { // Check if the symbol is one of the filtered ones
//                            val name = dataObj.getString("name")
//                            val quote = dataObj.getJSONObject("quote")
//                            val USD = quote.getJSONObject("USD")
//                            val price = USD.getDouble("price")
//                            val percent_change_24h = USD.getDouble("percent_change_24h") // Get the volume change
//                            currencyModalArrayList?.add(CurrencyModal(name, symbol, price, percent_change_24h)) // Pass it to the constructor
////                        }
//                    }
//                    currencyRVAdapter?.notifyDataSetChanged()
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
//                }
//            },
//            Response.ErrorListener { error ->
//                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
//            }) {
//            override fun getHeaders(): Map<String, String> {
//                val headers = HashMap<String, String>()
//                headers["X-CMC_PRO_API_KEY"] = "335008f7-957b-4453-9595-1cbfdf377afd" // Make sure to use your actual API key
//                return headers
//            }
//        }
//        queue.add(jsonObjectRequest)
//    }
    private fun populateList() {
        apiAdapter.getSymbolsList(object : SymbolsListCallback {
            override fun onSuccess(symbolsList: JSONArray) {
                for (i in 0 until symbolsList.length()) {
                    val item = symbolsList.getJSONObject(i)
                    val symbol = item.getString("symbol")
                    val name = item.getString("name")
                    val price = -1.0 // Use empty string for price
                    val percentChange24h = -1.0 // Use empty string for percent_change_24h

                    // Assuming currencyModalArrayList is a list of CurrencyModal objects
                    // and CurrencyModal constructor matches the parameters order: name, symbol, price, percent_change_24h
                    currencyModalArrayList?.add(CurrencyModal(name, symbol, price, percentChange24h))
                }
                currencyRVAdapter?.refresh()
            }

            override fun onError(errorMessage: String) {
                // Handle the error here, for example, print error message to console
                System.err.println(errorMessage)
            }
        })
    }
}